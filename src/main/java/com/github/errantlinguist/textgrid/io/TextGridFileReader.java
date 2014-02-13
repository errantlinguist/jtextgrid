/*
 * 	Copyright 2011--2014 Todd Shore
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.github.errantlinguist.textgrid.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.errantlinguist.io.AbstractBufferedInputStreamReader;
import com.github.errantlinguist.io.ParseException;
import com.github.errantlinguist.io.Parser;
import com.github.errantlinguist.textgrid.TextGridFile;
import com.github.errantlinguist.textgrid.TextGridListener;
import com.github.errantlinguist.textgrid.TierClass;

/**
 * A file reader which reads in <a
 * href="http://www.fon.hum.uva.nl/praat/">Praat</a> TextGrid files and parses
 * the data contained therein.
 * 
 * @param <D>
 *            The type representing each datum stored in the result
 *            <code>TextGridFile</code> object.
 * 
 * @since 2011-04-15
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 */
public class TextGridFileReader<D> extends
		AbstractBufferedInputStreamReader<Boolean, ParseException> implements
		TextGridListener<D> {

	/**
	 * An enumeration of section types in a <a
	 * href="http://www.fon.hum.uva.nl/praat/">Praat</a> TextGrid file.
	 * 
	 * @author Todd Shore
	 * @version 2014-02-06
	 * @since 2011-04-15
	 * 
	 */
	private static enum Section {
		FILE_END_TIME(END_TIME_PATTERN) {

			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyFileEndTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(FILE_TIER_COUNT);
			}

		},
		FILE_START_TIME(START_TIME_PATTERN) {

			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyFileStartTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(FILE_END_TIME);
			}

		},
		FILE_TIER_COUNT(Pattern.compile("size = (\\d+)\\s*")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyFileSize(Integer.parseInt(matcher.group(1)));
				reader.setCurrentSection(TIER_START);
			}

		},
		INTERVAL_DATA(Pattern.compile("^\\s*text = \"(.*)\"\\s*$")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) throws ParseException {
				reader.notifyIntervalData(matcher.group(1));
				reader.setCurrentSection(INTERVAL_START);
			}

		},
		INTERVAL_END_TIME(END_TIME_PATTERN) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyIntervalEndTime(Double.parseDouble(matcher
						.group(1)));
				reader.setCurrentSection(INTERVAL_DATA);

			}
		},
		INTERVAL_START(Pattern.compile("^\\s*intervals \\[(\\d+)\\]:\\s*")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyIntervalIndex(Integer.parseInt(matcher.group(1)));
				reader.setCurrentSection(INTERVAL_START_TIME);
			}

			@Override
			protected <D> void handleMismatch(final CharSequence line,
					final TextGridFileReader<D> reader) throws ParseException {
				final Matcher pointMatcher = Section.TIER_START.match(line);
				if (pointMatcher.matches()) {
					TIER_START.parse(line, reader);
				}

			}
		},
		INTERVAL_START_TIME(START_TIME_PATTERN) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyIntervalStartTime(Double.parseDouble(matcher
						.group(1)));
				reader.setCurrentSection(INTERVAL_END_TIME);
			}

		},
		POINT_DATA(Pattern.compile("^\\s*mark = \"(.*)\"\\s*$")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) throws ParseException {
				reader.notifyPointData(matcher.group(1));
				reader.setCurrentSection(POINT_START);
			}

		},
		POINT_START(Pattern.compile("^\\s*points \\[(\\d+)\\]:\\s*")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyPointIndex(Integer.parseInt(matcher.group(1)));
				reader.setCurrentSection(POINT_TIME);
			}

			@Override
			protected <D> void handleMismatch(final CharSequence line,
					final TextGridFileReader<D> reader) throws ParseException {
				final Matcher intervalMatcher = Section.TIER_START.match(line);
				if (intervalMatcher.matches()) {
					TIER_START.parse(line, reader);
				}
			}
		},
		POINT_TIME(Pattern.compile("^\\s*time = (\\d*\\.?\\d+)\\s*")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyPointTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(POINT_DATA);
			}

		},
		TIER_CLASS(Pattern.compile("^\\s*class = \"(.*)\"\\s*$")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				final TierClass tierClass = TierClass.getInstance(matcher
						.group(1));
				reader.notifyTierClass(tierClass);
				reader.setCurrentSection(TIER_NAME);
			}

		},
		TIER_END_TIME(END_TIME_PATTERN) {
			private Section getNextSection(final TierClass tierClass) {
				final Section newSection;

				if (tierClass == TierClass.INTERVAL) {
					newSection = TIER_INTERVAL_COUNT;
				} else if (tierClass == TierClass.TEXT) {
					newSection = TIER_POINT_COUNT;
				} else {
					newSection = null;
				}
				return newSection;
			}

			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyTierEndTime(Double.parseDouble(matcher.group(1)));

				final Section nextSection = getNextSection(reader
						.getCurrentTierClass());
				reader.setCurrentSection(nextSection);
			}

		},
		TIER_INTERVAL_COUNT(Pattern.compile("\\s*intervals: size = (\\d+)\\s*")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyTierIntervalCount(Integer.parseInt(matcher
						.group(1)));
				reader.setCurrentSection(INTERVAL_START);
			}

		},
		TIER_NAME(Pattern.compile("^\\s*name = \"(.*)\"\\s*$")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyTierName(matcher.group(1));
				reader.setCurrentSection(TIER_START_TIME);
			}

		},
		TIER_POINT_COUNT(Pattern.compile("\\s*points: size = (\\d+)\\s*")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyTierPointCount(Integer.parseInt(matcher.group(1)));
				reader.setCurrentSection(POINT_START);
			}

		},
		TIER_START(Pattern.compile("^\\s*item \\[(\\d+)\\]:\\s*$")) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyTierIndex(Integer.parseInt(matcher.group(1)));
				reader.setCurrentSection(TIER_CLASS);
			}

		},
		TIER_START_TIME(START_TIME_PATTERN) {
			@Override
			protected <D> void handleMatch(final Matcher matcher,
					final TextGridFileReader<D> reader) {
				reader.notifyTierStartTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(TIER_END_TIME);
			}
		};

		private final Pattern pattern;

		/**
		 * 
		 * @param pattern
		 *            The {@link Pattern} matching the given section.
		 */
		private Section(final Pattern pattern) {
			this.pattern = pattern;
		}

		/**
		 * Handles a given section match.
		 * 
		 * @param <D>
		 *            The type of object to be (later) returned denoting (a part
		 *            of) the file information.
		 * @param matcher
		 *            The {@link Matcher} object representing a section match.
		 * @param reader
		 *            The {@link TextGridFileReader} used to parse the file.
		 * @throws ParseException
		 *             If there is a parsing error.
		 * 
		 */
		protected abstract <D> void handleMatch(final Matcher matcher,
				final TextGridFileReader<D> reader) throws ParseException;

		/**
		 * Handles a section mismatch.
		 * 
		 * @param <D>
		 *            The type of object to be (later) returned denoting (a part
		 *            of) the file information.
		 * @param line
		 *            The {@link CharSequence} to be parsed.
		 * @param reader
		 *            The {@link TextGridFileReader} object used to parse the
		 *            file.
		 * @throws ParseException
		 *             If there is an otherwise unhandled error.
		 * 
		 */
		protected <D> void handleMismatch(final CharSequence line,
				final TextGridFileReader<D> reader) throws ParseException {
			// Do nothing by default
		}

		/**
		 * Matches a {@link CharSequence} input to the section {@link Pattern}.
		 * 
		 * @param line
		 *            The <code>CharSequence</code> to match.
		 * @return A {@link Matcher} object matching the
		 *         <code>CharSequence</code> to the section <code>Pattern</code>
		 *         .
		 */
		protected final Matcher match(final CharSequence line) {
			final Matcher matcher = pattern.matcher(line);
			return matcher;

		}

		/**
		 * Parses a given (possible) matching section.
		 * 
		 * @param <D>
		 *            The type of object to be (later) returned denoting (a part
		 *            of) the file information.
		 * @param line
		 *            The {@link CharSequence} to be parsed.
		 * @param reader
		 *            The {@link TextGridFileReader} object used to generate the
		 *            {@link TextGridFile} object representation of the file.
		 * @throws ParseException
		 *             If there is a parsing error.
		 * 
		 */
		protected final <D> void parse(final CharSequence line,
				final TextGridFileReader<D> reader) throws ParseException {
			final Matcher matcher = match(line);
			if (matcher.matches()) {
				handleMatch(matcher, reader);
			} else {
				handleMismatch(line, reader);
			}

		}

	}

	private static final Pattern END_TIME_PATTERN = Pattern
			.compile("^\\s*xmax = (\\d*\\.?\\d+)\\s*$");

	private static final Pattern START_TIME_PATTERN = Pattern
			.compile("^\\s*xmin = (\\d*\\.?\\d+)\\s*$");

	private Section currentSection;

	/**
	 * The {@link TierClass} instance which was last passed to
	 * {@link #notifyTierClass(TierClass)}.
	 */
	private TierClass currentTierClass = null;

	/**
	 * The {@link TextGridListener} instance registered to respond to callback
	 * notifications from this reader.
	 */
	private final TextGridListener<D> listener;

	/**
	 * The {@link Parser} object using for parsing file sections.
	 */
	private final Parser<? super String, ? extends D> parser;

	/**
	 * 
	 * @param parser
	 *            The {@link Parser} object using for parsing file sections.
	 * @param listener
	 *            The {@link TextGridListener} instance registered to respond to
	 *            callback notifications from this reader.
	 */
	public TextGridFileReader(final Parser<? super String, ? extends D> parser,
			final TextGridListener<D> listener) {
		this.parser = parser;
		this.listener = listener;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		final boolean result;

		if (super.equals(obj)) {
			result = true;
		} else if (obj == null) {
			result = false;
		} else if (obj instanceof TextGridFileReader<?>) {
			final TextGridFileReader<?> other = (TextGridFileReader<?>) obj;
			result = isEquivalentTo(other);
		} else {
			result = false;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((currentSection == null) ? 0 : currentSection.hashCode());
		result = (prime * result)
				+ ((currentTierClass == null) ? 0 : currentTierClass.hashCode());
		result = (prime * result)
				+ ((listener == null) ? 0 : listener.hashCode());
		result = (prime * result) + ((parser == null) ? 0 : parser.hashCode());
		return result;
	}

	@Override
	public void notifyFileEndTime(final double time) {
		listener.notifyFileEndTime(time);

	}

	@Override
	public void notifyFileSize(final int count) {
		listener.notifyFileSize(count);
	}

	@Override
	public void notifyFileStartTime(final double time) {
		listener.notifyFileStartTime(time);
	}

	@Override
	public void notifyIntervalData(final D data) {
		listener.notifyIntervalData(data);
	}

	public void notifyIntervalData(final String data) throws ParseException {
		final D parsedData = parseEntryData(data);
		notifyIntervalData(parsedData);
	}

	@Override
	public void notifyIntervalEndTime(final double time) {
		listener.notifyIntervalEndTime(time);
	}

	@Override
	public void notifyIntervalIndex(final int index) {
		listener.notifyIntervalIndex(index);
	}

	@Override
	public void notifyIntervalStartTime(final double time) {
		listener.notifyIntervalStartTime(time);
	}

	@Override
	public void notifyPointData(final D data) {
		listener.notifyPointData(data);
	}

	public void notifyPointData(final String data) throws ParseException {
		final D parsedData = parseEntryData(data);
		notifyPointData(parsedData);
	}

	@Override
	public void notifyPointIndex(final int index) {
		listener.notifyPointIndex(index);
	}

	@Override
	public void notifyPointTime(final double time) {
		listener.notifyPointTime(time);
	}

	@Override
	public void notifyTierClass(final TierClass tierClass) {
		listener.notifyTierClass(tierClass);
		currentTierClass = tierClass;
	}

	@Override
	public void notifyTierEndTime(final double time) {
		listener.notifyTierEndTime(time);
	}

	@Override
	public void notifyTierIndex(final int index) {
		listener.notifyTierIndex(index);
	}

	@Override
	public void notifyTierIntervalCount(final int count) {
		listener.notifyTierIntervalCount(count);
	}

	@Override
	public void notifyTierName(final String name) {
		listener.notifyTierName(name);
	}

	@Override
	public void notifyTierPointCount(final int count) {
		listener.notifyTierPointCount(count);
	}

	@Override
	public void notifyTierStartTime(final double time) {
		listener.notifyTierStartTime(time);
	}

	/**
	 * Reads a TextGrid file and returns the annotation data contained therein
	 * as a new {@link TextGridFile} object.
	 * 
	 * @param input
	 *            The TextGrid file to be read.
	 * @return <code>true</code> iff the entire reader input was successfully
	 *         parsed.
	 * @throws IOException
	 *             If the input {@link File} object does not refer to a valid
	 *             file or another I/O error occurs.
	 * @throws ParseException
	 *             If there is a parsing error.
	 */
	@Override
	public Boolean read(final BufferedReader input) throws IOException,
			ParseException {
		currentSection = Section.FILE_START_TIME;
		for (String line = input.readLine(); line != null; line = input
				.readLine()) {
			currentSection.parse(line, this);
		}
		return Boolean.TRUE;
	}

	/**
	 * @return The {@link TierClass} instance which was last passed to
	 *         {@link #notifyTierClass(TierClass)}.
	 */
	private TierClass getCurrentTierClass() {
		return currentTierClass;
	}

	/**
	 * Checks is a given object is equivalent to this object.
	 * 
	 * @param other
	 *            The object to check for equality.
	 * @return If the object is equal to this one.
	 */
	private boolean isEquivalentTo(final TextGridFileReader<?> other) {
		return ((currentSection == other.currentSection)
				&& (currentTierClass == other.currentTierClass)
				&& Objects.equals(listener, other.listener) && Objects.equals(
				parser, other.parser));
	}

	/**
	 * Parses a {@link String} of data with the set {@link TextGridFileReader}.
	 * 
	 * @param data
	 *            The <code>String</code> to be parsed.
	 * @return An object representing the parsed data.
	 * @throws ParseException
	 *             If there is a parsing error.
	 */
	private D parseEntryData(final String data) throws ParseException {
		return parser.parse(data);
	}

	/**
	 * Sets the current {@link Section} to parse the next file section.
	 * 
	 * @param nextSection
	 *            The <code>Section</code> to set as the current section.
	 */
	private void setCurrentSection(final Section nextSection) {
		currentSection = nextSection;

	}

}
