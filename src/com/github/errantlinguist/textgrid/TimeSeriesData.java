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

package com.github.errantlinguist.textgrid;

/**
 * An object representing data associated with a time sequence.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-07-06
 * 
 * @param <T>
 *            The <code>TimeSeriesData</code> object subtype.
 * 
 */
public abstract class TimeSeriesData<T extends TimeSeriesData<?>> implements
		Comparable<T> {

	/**
	 * The start time of the data.
	 */
	protected final double endTime;

	/**
	 * The hash code for final members.
	 */
	private final int precachedHashCode;

	/**
	 * The end time of the data.
	 */
	protected final double startTime;

	/**
	 * @param startTime
	 *            The data start time.
	 * @param endTime
	 *            The data end time.
	 */
	public TimeSeriesData(final double startTime, final double endTime) {
		this.startTime = startTime;
		this.endTime = endTime;

		this.precachedHashCode = calculateHashCode();

	}

	/**
	 * Pre-caches the hash code for final members.
	 * 
	 * @return The hash code for final members.
	 */
	private int calculateHashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(endTime);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(startTime);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final T arg0) {
		if (this == arg0) {
			return 0;
		}
		int comp = Double.compare(startTime, arg0.startTime);

		if (comp == 0) {

			comp = Double.compare(endTime, arg0.endTime);

			if (comp == 0) {

				comp = deepCompareTo(arg0);

			}
		}

		return comp;

	}

	/**
	 * Compares this object with another of its type by the data it represents
	 * rather than simply by comparing their respective start and end times.
	 * 
	 * @param arg0
	 *            The object to compare this object to.
	 * @return <code>-1</code> if this object is less than the given object,
	 *         <code>0</code> if they are equal and <code>1</code> if this
	 *         object is greater than the given one.
	 */
	protected abstract int deepCompareTo(final T arg0);

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
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TimeSeriesData<?>)) {
			return false;
		}
		final TimeSeriesData<?> other = (TimeSeriesData<?>) obj;
		if (Double.doubleToLongBits(endTime) != Double
				.doubleToLongBits(other.endTime)) {
			return false;
		}
		if (Double.doubleToLongBits(startTime) != Double
				.doubleToLongBits(other.startTime)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the endTime
	 */
	public double getEndTime() {
		return endTime;
	}

	/**
	 * @return the startTime
	 */
	public double getStartTime() {
		return startTime;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName());
		builder.append("[startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append("]");
		return builder.toString();
	}

}