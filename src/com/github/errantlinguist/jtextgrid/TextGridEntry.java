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

/**
 * A single entry in the TextGrid file, representing either an interval or a
 * point on a tier.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-04-15
 * 
 * @param <T>
 *            The object type representing the entry data.
 */
public final class TextGridEntry<T> extends TimeSeriesData<TextGridEntry<T>> {

	/**
	 * The annotation data the entry represents.
	 */
	protected final T data;

	/**
	 * The ID of the entry.
	 */
	protected final int id;

	/**
	 * The hash code for final members.
	 */
	private final int precachedHashCode;

	/**
	 * The {@link TextGridTier} representing the tier the entry is on.
	 */
	private final TextGridTier<T> textGridTier;

	/**
	 * @param tier
	 *            The {@link TextGridTier} object representing the textGridTier
	 *            the entry is on.
	 * @param id
	 *            The ID of the entry.
	 * @param startTime
	 *            The start time of the entry.
	 * @param endTime
	 *            The end time of the entry.
	 * @param data
	 *            The annotation data the entry represents.
	 */
	TextGridEntry(final TextGridTier<T> tier, final int id,
			final double startTime, final double endTime, final T data) {
		super(startTime, endTime);
		this.textGridTier = tier;
		this.id = id;
		this.data = data;

		this.precachedHashCode = precachehashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see TimeSeriesData#deepCompareTo(TimeSeriesData)
	 */
	@Override
	protected int deepCompareTo(final TextGridEntry<T> arg0) {
		int comp = 0;

		// Compare textGridTier IDs
		final int tid1 = textGridTier.id;
		final int tid2 = arg0.textGridTier.id;

		if (tid1 < tid2) {
			comp = -1;
		} else if (tid1 < tid2) {
			comp = 1;
		} else {

			// Compare entry IDs
			if (id < arg0.id) {
				comp = -1;
			} else if (id > arg0.id) {
				comp = 1;
			}

		}

		return comp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof TextGridEntry<?>)) {
			return false;
		}
		final TextGridEntry<?> other = (TextGridEntry<?>) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (textGridTier == null) {
			if (other.textGridTier != null) {
				return false;
			}
		} else if (!textGridTier.equals(other.textGridTier)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return precachedHashCode;
	}

	/**
	 * Pre-caches the hash code for final members.
	 * 
	 * @return The hash code for final members.
	 */
	private int precachehashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (data == null ? 0 : data.hashCode());
		result = prime * result + id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TextGridEntry[id=");
		builder.append(id);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}
}
