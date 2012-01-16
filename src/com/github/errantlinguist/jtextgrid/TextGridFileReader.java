/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 */

package com.github.errantlinguist.jtextgrid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.errantlinguist.io.DummyFileParser;
import com.github.errantlinguist.io.FileParser;
import com.github.errantlinguist.io.FileReader;
import com.github.errantlinguist.jtextgrid.TextGridTier.TextGridTierClass;

/**
 * A file reader which reads in <a href="http://www.fon.hum.uva.nl/praat/">Praat</a> TextGrid files, parses the data contained
 * therein, and returns it as a {@link TextGridFile} object representing it.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-04-15
 * 
 * @param <T>
 *            The type of data stored in the <code>TextGridFile</code> object.
 */
public class TextGridFileReader<T> extends FileReader<TextGridFile<T>> {
	
	/**
	 * An enumeration of section types in a <a href="http://www.fon.hum.uva.nl/praat/">Praat</a> TextGrid file.
	 * 
	 * @author Todd Shore
	 * @version 2011-07-28
	 * 
	 */
	private static enum Section {
		FILE_END_TIME(END_TIME_PATTERN) {

			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setFileEndTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(FILE_TIER_COUNT);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}

		},
		FILE_START_TIME(START_TIME_PATTERN) {

			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setFileStartTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(FILE_END_TIME);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		FILE_TIER_COUNT(Pattern.compile("size = (\\d+)\\s*")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setFileSize(Integer.parseInt(matcher.group(1)));
				reader.makeNewTextGridFile();
				reader.setCurrentSection(TIER_START);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		INTERVAL_DATA(Pattern.compile("^\\s*text = \"(.*)\"\\s*$")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) throws Exception {

				reader.parseEntryData(matcher.group(1));
				reader.addNewEntry();

				reader.setCurrentSection(INTERVAL_START);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		INTERVAL_END_TIME(END_TIME_PATTERN) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setEntryEndTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(INTERVAL_DATA);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		INTERVAL_START(Pattern.compile("^\\s*intervals \\[(\\d+)\\]:\\s*")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setEntryID(Integer.parseInt(matcher.group(1)));
				reader.setCurrentSection(INTERVAL_START_TIME);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {
				final Matcher pointMatcher = Section.TIER_START.match(line);
				if (pointMatcher.matches()) {
					TIER_START.parse(line, reader);
				}

			}
		},
		INTERVAL_START_TIME(START_TIME_PATTERN) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setEntryStartTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(INTERVAL_END_TIME);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		POINT_DATA(Pattern.compile("^\\s*mark = \"(.*)\"\\s*$")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) throws Exception {

				reader.parseEntryData(matcher.group(1));
				reader.addNewEntry();

				reader.setCurrentSection(POINT_START);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		POINT_START(Pattern.compile("^\\s*points \\[(\\d+)\\]:\\s*")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setEntryID(Integer.parseInt(matcher.group(1)));
				reader.setCurrentSection(POINT_TIME);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {
				final Matcher intervalMatcher = Section.TIER_START.match(line);
				if (intervalMatcher.matches()) {
					TIER_START.parse(line, reader);
				}

			}
		},
		POINT_TIME(Pattern.compile("^\\s*time = (\\d*\\.?\\d+)\\s*")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				final double time = Double.parseDouble(matcher.group(1));
				reader.setEntryStartTime(time);
				reader.setEntryEndTime(time);
				reader.setCurrentSection(POINT_DATA);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		TIER_CLASS(Pattern.compile("^\\s*class = \"(.*)\"\\s*$")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				final TextGridTierClass tierClass = TextGridTierClass.getTierClass(matcher.group(1));
				reader.setTierClass(tierClass);
				reader.setCurrentSection(TIER_NAME);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		TIER_END_TIME(END_TIME_PATTERN) {
			private Section getNextSection(final String tierClass) {
				final Section newSection;

				if (tierClass.equals(INTERVAL_TIER_NAME)) {
					newSection = TIER_INTERVAL_COUNT;
				} else if (tierClass.equals(POINT_TIER_NAME)) {
					newSection = TIER_POINT_COUNT;
				} else {
					newSection = null;
				}
				return newSection;
			}

			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setTierEndTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(getNextSection(reader.getTierClass().getName()));

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		TIER_INTERVAL_COUNT(Pattern.compile("\\s*intervals: size = (\\d+)\\s*")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setTierSize(Integer.parseInt(matcher.group(1)));
				reader.addNewTier();
				reader.setCurrentSection(INTERVAL_START);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		TIER_NAME(Pattern.compile("^\\s*name = \"(.*)\"\\s*$")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setTierName(matcher.group(1));
				reader.setCurrentSection(TIER_START_TIME);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		TIER_POINT_COUNT(Pattern.compile("\\s*points: size = (\\d+)\\s*")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setTierSize(Integer.parseInt(matcher.group(1)));
				reader.addNewTier();
				reader.setCurrentSection(POINT_START);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		},
		TIER_START(Pattern.compile("^\\s*item \\[(\\d+)\\]:\\s*$")) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setTierID(Integer.parseInt(matcher.group(1)));
				reader.setCurrentSection(TIER_CLASS);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}

		},
		TIER_START_TIME(START_TIME_PATTERN) {
			@Override
			protected <T> void handleMatch(final Matcher matcher,
					final TextGridFileReader<T> reader) {

				reader.setTierStartTime(Double.parseDouble(matcher.group(1)));
				reader.setCurrentSection(TIER_END_TIME);

			}

			@Override
			protected <T> void handleMismatch(final String line,
					final TextGridFileReader<T> reader) throws Exception {

			}
		};

		private static String INTERVAL_TIER_NAME = "IntervalTier";
		private static String POINT_TIER_NAME = "TextTier";

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
		 * @param <T>
		 *            The type of object to be (later) returned denoting (a part
		 *            of) the file information.
		 * @param matcher
		 *            The {@link Matcher} object representing a section match.
		 * @param reader
		 *            The {@link TextGridFileReader} used to parse the file.
		 * @throws Exception
		 *             If there is a parsing error.
		 */
		protected abstract <T> void handleMatch(final Matcher matcher,
				final TextGridFileReader<T> reader) throws Exception;

		/**
		 * Handles a section mismatch.
		 * 
		 * @param <T>
		 *            The type of object to be (later) returned denoting (a part
		 *            of) the file information.
		 * @param line
		 *            The {@link String} to be parsed.
		 * @param reader
		 *            The {@link TextGridFileReader} object used to parse the
		 *            file.
		 * @throws Exception
		 *             If there is an otherwise unhandled error.
		 */
		protected abstract <T> void handleMismatch(final String line,
				final TextGridFileReader<T> reader) throws Exception;

		/**
		 * Matches a {@link String} input to the section {@link Pattern}.
		 * 
		 * @param line
		 *            The <code>String</code> to match.
		 * @return A {@link Matcher} object matching the <code>String</code> to the section
		 *         <code>Pattern</code>.
		 */
		protected final Matcher match(final String line) {
			final Matcher matcher = pattern.matcher(line);
			return matcher;

		}

		/**
		 * Parses a given (possible) matching section.
		 * 
		 * @param <T>
		 *            The type of object to be (later) returned denoting (a part
		 *            of) the file information.
		 * @param line
		 *            The {@link String} to be parsed.
		 * @param reader
		 *            The {@link TextGridFileReader} object used to generate the
		 *            {@link TextGridFile} object representation of the file.
		 * @throws Exception
		 *             If there is a parsing error.
		 */
		protected final <T> void parse(final String line,
				final TextGridFileReader<T> reader) throws Exception {

			final Matcher matcher = match(line);
			if (matcher.matches()) {
				handleMatch(matcher, reader);
			} else {
				handleMismatch(line, reader);
			}

		}

	}

	/**
	 * A holder class for the static singleton {@link TextGridFileReader} instance
	 * with a {@link DummyFileParser} for {@link TextGridFileReader#parser}.
	 * 
	 * @author Todd Shore
	 * @version 2012-01-16
	 * @since 2012-01-16
	 * 
	 */
	private static final class SingletonHolder {

		/**
		 * The static singleton {@link TextGridFileReader} instance with a
		 * {@link DummyFileParser} for {@link TextGridFileReader#parser}.
		 */
		private static final TextGridFileReader<String> INSTANCE = new TextGridFileReader<String>(
				DummyFileParser.getInstance());
	}

	private static final Pattern END_TIME_PATTERN = Pattern
			.compile("^\\s*xmax = (\\d*\\.?\\d+)\\s*$");

	private static final Pattern START_TIME_PATTERN = Pattern
			.compile("^\\s*xmin = (\\d*\\.?\\d+)\\s*$");

	/**
	 * 
	 * @return The static singleton {{@link TextGridFileReader} instance with a
	 *         {@link DummyFileParser} for {@link TextGridFileReader#parser}.
	 */
	public static TextGridFileReader<String> getStringParserInstance() {
		return SingletonHolder.INSTANCE;
	}

	private Section currentSection;

	private T entryData;

	private double entryEndTime;

	private int entryID;

	private double entryStartTime;

	private double fileEndTime;

	private int fileSize;

	private double fileStartTime;

	private final FileParser<T> parser;

	private TextGridFile<T> tgf;

	private TextGridTier<T> tier;

	private TextGridTierClass tierClass;

	private double tierEndTime;

	private int tierID;

	private String tierName;

	private int tierSize;

	private double tierStartTime;

	/**
	 * 
	 * @param parser
	 *            The {@link FileParser} object using for parsing file sections.
	 */
	public TextGridFileReader(final FileParser<T> parser) {
		this.parser = parser;
	}

	/**
	 * Constructs and adds a new {@link TextGridEntry} object with the current
	 * entry details to the currently {@link TextGridTier} object.
	 */
	private void addNewEntry() {
		tier.addEntry(entryID, entryStartTime, entryEndTime, entryData);
	}

	/**
	 * Constructs a new {@link TextGridTier} object with the current tier
	 * details to the current {@link TextGridFile} object and sets it as the
	 * current <code>TextGridTier</code> being added to.
	 */
	private void addNewTier() {
		tier = tgf.addTier(tierClass, tierID, tierName, tierStartTime,
				tierEndTime, tierSize);

	}

	/**
	 * 
	 * @return the tierClass
	 */
	private TextGridTierClass getTierClass() {
		return tierClass;
	}

	/**
	 * Constructs a new {@link TextGridFile} object using the current file
	 * details and sets it to the current <code>TextGridFile</code> object being
	 * added to.
	 */
	private void makeNewTextGridFile() {
		tgf = new TextGridFile<T>(fileStartTime, fileEndTime, fileSize);

	}

	/**
	 * Parses a {@link String} of data with the set {@link TextGridFileReader}.
	 * 
	 * @param data
	 *            The <code>String</code> to be parsed.
	 * @throws Exception
	 *             If there is a parsing error.
	 */
	private void parseEntryData(final String data) throws Exception {
		entryData = parser.parse(data);
	}

	/**
	 * Reads a TextGrid file and returns the annotation data contained therein
	 * as a new {@link TextGridFile} object.
	 * 
	 * @param infile
	 *            The TextGrid file to be read.
	 * @return A <code>TextGridFile</code> object representing the input
	 *         TextGrid file.
	 * @throws IOException
	 *             If the input {@link File} object does not refer to a valid
	 *             file or another I/O error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subtype of this class.
	 */
	@Override
	public final TextGridFile<T> readFile(final File infile)
			throws IOException, Exception {
		final InputStreamReader isr = new InputStreamReader(
				new FileInputStream(infile));
		final BufferedReader br = new BufferedReader(isr);

		tgf = null;
		fileSize = 0;
		tier = null;
		tierClass = null;
		tierID = 0;
		tierName = null;
		tierSize = 0;
		fileStartTime = 0.0;
		fileEndTime = 0.0;
		tierStartTime = 0.0;
		tierEndTime = 0.0;
		entryID = 0;
		entryStartTime = 0.0;
		entryEndTime = 0.0;
		entryData = null;
		currentSection = Section.FILE_START_TIME;

		for (String line = br.readLine(); line != null; line = br.readLine()) {

			currentSection.parse(line, this);

		}
		br.close();
		isr.close();

		return tgf;
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

	/**
	 * Sets the end time of the next new {@link TextGridEntry} object.
	 * 
	 * @param time
	 *            The end time of the entry.
	 */
	private void setEntryEndTime(final double time) {
		entryEndTime = time;

	}

	/**
	 * Sets the ID of the next new {@link TextGridEntry} object.
	 * 
	 * @param id
	 *            The ID of the entry.
	 */
	private void setEntryID(final int id) {
		entryID = id;

	}

	/**
	 * Sets the start time of the next new {@link TextGridEntry} object.
	 * 
	 * @param time
	 *            The start time of the entry.
	 */
	private void setEntryStartTime(final double time) {
		entryStartTime = time;

	}

	/**
	 * Sets the end time of the next new {@link TextGridFile} object.
	 * 
	 * @param time
	 *            The end time of the file.
	 */
	private void setFileEndTime(final double time) {
		fileEndTime = time;

	}

	/**
	 * Sets the size of the next new {@link TextGridFile} object measured by the
	 * number of tiers it has.
	 * 
	 * @param size
	 *            The size of the file.
	 */
	private void setFileSize(final int size) {
		fileSize = size;

	}

	/**
	 * Sets the start time of the next new {@link TextGridFile} object.
	 * 
	 * @param time
	 *            The start time of the file.
	 */
	private void setFileStartTime(final double time) {
		fileStartTime = time;

	}

	/**
	 * Sets the tier class of the next new {@link TextGridTier} object.
	 * 
	 * @param tierClass
	 *            The tier class.
	 */
	private void setTierClass(final TextGridTierClass tierClass) {
		this.tierClass = tierClass;

	}

	/**
	 * Sets the end time of the next new {@link TextGridTier} object.
	 * 
	 * @param time
	 *            The end time of the file.
	 */
	private void setTierEndTime(final double time) {
		tierEndTime = time;

	}

	/**
	 * Sets the ID of the next new {@link TextGridTier} object.
	 * 
	 * @param id
	 *            The ID of the tier.
	 */
	private void setTierID(final int id) {
		tierID = id;

	}

	/**
	 * Sets the name of the next new {@link TextGridTier} object.
	 * 
	 * @param name
	 *            The name of the tier.
	 */
	private void setTierName(final String name) {
		tierName = name;

	}

	/**
	 * Sets the size of the next new {@link TextGridTier} object measured by the
	 * number of entries it has.
	 * 
	 * @param size
	 *            The size of the tier.
	 */
	private void setTierSize(final int size) {
		tierSize = size;

	}

	/**
	 * Sets the start time of the next new {@link TextGridTier} object.
	 * 
	 * @param time
	 *            The start time of the tier.
	 */
	private void setTierStartTime(final double time) {
		tierStartTime = time;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TextGridFileReader[currentSection=");
		builder.append(currentSection);
		builder.append(", entryData=");
		builder.append(entryData);
		builder.append(", entryEndTime=");
		builder.append(entryEndTime);
		builder.append(", entryID=");
		builder.append(entryID);
		builder.append(", entryStartTime=");
		builder.append(entryStartTime);
		builder.append(", fileEndTime=");
		builder.append(fileEndTime);
		builder.append(", fileSize=");
		builder.append(fileSize);
		builder.append(", fileStartTime=");
		builder.append(fileStartTime);
		builder.append(", parser=");
		builder.append(parser);
		builder.append(", tgf=");
		builder.append(tgf);
		builder.append(", tier=");
		builder.append(tier);
		builder.append(", tierClass=");
		builder.append(tierClass);
		builder.append(", tierEndTime=");
		builder.append(tierEndTime);
		builder.append(", tierID=");
		builder.append(tierID);
		builder.append(", tierName=");
		builder.append(tierName);
		builder.append(", tierSize=");
		builder.append(tierSize);
		builder.append(", tierStartTime=");
		builder.append(tierStartTime);
		builder.append("]");
		return builder.toString();
	}

}
