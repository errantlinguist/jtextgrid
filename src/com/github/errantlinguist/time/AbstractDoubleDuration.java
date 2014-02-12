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
package com.github.errantlinguist.time;

/**
 * An abstract class providing default behaviour for the {@link DoubleDuration}
 * interface.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 * 
 */
public abstract class AbstractDoubleDuration implements DoubleDuration {

	@Override
	public int compareTo(final DoubleDuration o) {
		int result;

		if (this == o) {
			result = 0;
		} else {
			result = Double.compare(getStartTimeValue(), o.getStartTimeValue());
			if (result == 0) {
				result = Double.compare(getEndTimeValue(), o.getEndTimeValue());
			}
		}

		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		final boolean result;

		if (super.equals(obj)) {
			result = true;
		} else if (obj == null) {
			result = false;
		} else if (obj instanceof AbstractDoubleDuration) {
			final AbstractDoubleDuration other = (AbstractDoubleDuration) obj;
			result = isEquivalentTo(other);
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * @return The same value as returned by {@link #getEndTimeValue()}.
	 */
	@Override
	public Double getEndTime() {
		return getEndTimeValue();
	}

	/**
	 * @return The same value as returned by {@link #getStartTimeValue()}.
	 */
	@Override
	public Double getStartTime() {
		return getStartTimeValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(getEndTimeValue());
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(getStartTimeValue());
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Checks is a given object is equivalent to this object; This is used by
	 * {@link #equals(Object)}.
	 * 
	 * @param other
	 *            The object to check for equality.
	 * @return If the object is equal to this one.
	 */
	private boolean isEquivalentTo(final AbstractDoubleDuration other) {
		return ((Double.doubleToLongBits(getStartTimeValue()) == Double
				.doubleToLongBits(other.getStartTimeValue())) && (Double
				.doubleToLongBits(getEndTimeValue()) == Double
				.doubleToLongBits(other.getEndTimeValue())));
	}

}