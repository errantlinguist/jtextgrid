/*
 * 	Copyright 2014 Todd Shore
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

import com.github.errantlinguist.Clearable;
import com.github.errantlinguist.Factory;
import com.github.errantlinguist.time.DoubleDuration;
import com.github.errantlinguist.time.ImmutableDoubleDuration;

/**
 * A {@link Factory} which creates {@link TextGridFile} objects.
 * 
 * @param <D>
 *            The type of the annotation data.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class TextGridFileFactory<D> implements Clearable,
		Factory<TextGridFile<D>> {

	/**
	 * A constant value used for estimating the length of the string
	 * representation of the object returned by {@link #toString()}.
	 */
	private static final int ESTIMATED_STRING_REPR_LENGTH = 96;

	/**
	 * The initial value for newly-intialised primitive fields.
	 */
	private static final int NULL_VALUE = -1;

	/**
	 * The end time of the next new {@link TextGridFile} object.
	 */
	private double endTime = NULL_VALUE;

	/**
	 * The size of the next new {@link TextGridFile} object measured by the
	 * number of tiers it has.
	 */
	private int size = NULL_VALUE;

	/**
	 * The start time of the next new {@link TextGridFile} object.
	 */
	private double startTime = NULL_VALUE;

	@Override
	public void clear() {
		size = NULL_VALUE;
		startTime = NULL_VALUE;
		endTime = NULL_VALUE;
	}

	@Override
	public TextGridFile<D> create() {
		final DoubleDuration duration = new ImmutableDoubleDuration(startTime,
				endTime);
		return new TextGridFile<D>(duration, size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		final boolean result;

		if (this == obj) {
			result = true;
		} else if (obj == null) {
			result = false;
		} else if (obj instanceof TextGridFileFactory<?>) {
			final TextGridFileFactory<?> other = (TextGridFileFactory<?>) obj;
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
		result = (prime * result) + size;
		long temp;
		temp = Double.doubleToLongBits(startTime);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(endTime);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Sets the end time of the next new {@link TextGridFile} object.
	 * 
	 * @param time
	 *            The end time of the file.
	 */
	public void setEndTime(final double time) {
		endTime = time;
	}

	/**
	 * Sets the size of the next new {@link TextGridFile} object measured by the
	 * number of tiers it has.
	 * 
	 * @param size
	 *            The size of the file.
	 */
	public void setSize(final int size) {
		this.size = size;
	}

	/**
	 * Sets the start time of the next new {@link TextGridFile} object.
	 * 
	 * @param time
	 *            The start time of the file.
	 */
	public void setStartTime(final double time) {
		startTime = time;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(
				ESTIMATED_STRING_REPR_LENGTH);
		builder.append("TextGridFileFactory [fileSize=");
		builder.append(size);
		builder.append(", fileStartTime=");
		builder.append(startTime);
		builder.append(", fileEndTime=");
		builder.append(endTime);
		builder.append(']');
		return builder.toString();
	}

	/**
	 * Checks is a given object is equivalent to this object.
	 * 
	 * @param other
	 *            The object to check for equality.
	 * @return If the object is equal to this one.
	 */
	private boolean isEquivalentTo(final TextGridFileFactory<?> other) {
		return ((size == other.size)
				&& (Double.doubleToLongBits(startTime) != Double
						.doubleToLongBits(other.startTime)) && (Double
					.doubleToLongBits(endTime) != Double
				.doubleToLongBits(other.endTime)));
	}

}
