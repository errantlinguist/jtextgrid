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
package com.github.errantlinguist.textgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.errantlinguist.time.DoubleDuration;
import com.github.errantlinguist.time.Durative;
import com.github.errantlinguist.tree.ChildList;

/**
 * A representation of a <a href="http://www.fon.hum.uva.nl/praat/">Praat</a>
 * TextGrid file and the annotation data contained therein.
 * 
 * @param <D>
 *            The type of the annotation data.
 * 
 * @since 2011-04-15
 * @version 2014-02-11
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class TextGridFile<D> implements Durative<Double>, Serializable {

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -7360292361743846299L;

	/**
	 * A list of the {@link NamedTier tiers} this TextGrid file contains.
	 */
	private final ChildList<TextGridFile<D>, NamedTier<D>> children;

	/**
	 * The duration of the TextGrid file.
	 */
	private final DoubleDuration duration;

	/**
	 * 
	 * @param duration
	 *            The duration of the TextGrid file.
	 */
	public TextGridFile(final DoubleDuration duration) {
		this(duration, new ArrayList<NamedTier<D>>());
	}

	/**
	 * 
	 * @param duration
	 *            The duration of the TextGrid file.
	 * @param initialSize
	 *            The size of the file measured by the number of tiers it has.
	 */
	public TextGridFile(final DoubleDuration duration, final int initialSize) {
		this(duration, new ArrayList<NamedTier<D>>(initialSize));
	}

	/**
	 * 
	 * @param duration
	 *            The duration of the TextGrid file.
	 * @param tiers
	 *            A list of the {@link NamedTier tiers} this TextGrid file
	 *            contains.
	 */
	public TextGridFile(final DoubleDuration duration,
			final List<NamedTier<D>> tiers) {
		this.children = new ChildList<TextGridFile<D>, NamedTier<D>>(tiers,
				this);
		this.duration = duration;

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
		} else if (obj instanceof TextGridFile) {
			final TextGridFile<?> other = (TextGridFile<?>) obj;
			result = isEquivalentTo(other);
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * @return A list of the {@link NamedTier tiers} this TextGrid file
	 *         contains.
	 */
	public ChildList<TextGridFile<D>, NamedTier<D>> getChildren() {
		return children;
	}

	/**
	 * @return The duration of the TextGrid file.
	 */
	@Override
	public DoubleDuration getDuration() {
		return duration;
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
				+ ((duration == null) ? 0 : duration.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String durationPrefix = "TextGridFile [getDuration()=";
		final String durationStr = Objects.toString(getDuration());
		final String childrenPrefix = ", getChildren()=";
		final String childrenStr = Objects.toString(getChildren());
		final StringBuilder builder = new StringBuilder(durationPrefix.length()
				+ durationStr.length() + childrenPrefix.length()
				+ childrenStr.length() + 1);
		builder.append(durationPrefix);
		builder.append(durationStr);
		builder.append(childrenPrefix);
		builder.append(childrenStr);
		builder.append(']');
		return builder.toString();
	}

	/**
	 * Checks if this object is equivalent to another one (ignoring identity).
	 * 
	 * @param other
	 *            The other object to compare to.
	 * @return <code>true</code> iff this object is equivalent to the other one.
	 */
	private boolean isEquivalentTo(final TextGridFile<?> other) {
		return (Objects.equals(getDuration(), other.getDuration()) && Objects
				.equals(getChildren(), other.getChildren()));
	}

}
