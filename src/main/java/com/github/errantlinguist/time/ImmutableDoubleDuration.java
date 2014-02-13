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

import java.io.Serializable;
import java.util.Objects;

/**
 * An immutable implementation of {@link DoubleDuration}.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 * 
 */
public final class ImmutableDoubleDuration extends AbstractDoubleDuration
		implements Serializable {

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -308673764438034690L;

	/**
	 * The start time.
	 */
	private final double endTime;

	/**
	 * The pre-cached hash code for immutable members.
	 */
	private final transient int hashCode = super.hashCode();

	/**
	 * The end time.
	 */
	private final double startTime;

	/**
	 * @param startTime
	 *            The start time.
	 * @param endTime
	 *            The end time.
	 */
	public ImmutableDoubleDuration(final double startTime, final double endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.errantlinguist.textgrid.Duration#getEnd()
	 */
	@Override
	public double getEndTimeValue() {
		return endTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.errantlinguist.textgrid.Duration#getStart()
	 */
	@Override
	public double getStartTimeValue() {
		return startTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.errantlinguist.time.DoubleDuration#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String startTimePrefix = "ImmutableDoubleDuration [getStartTime()=";
		final String startTimeStr = Objects.toString(getStartTime());
		final String endTimePrefix = ", getEndTime()=";
		final String endTimeStr = Objects.toString(getEndTime());
		final char suffix = ']';

		final StringBuilder builder = new StringBuilder(
				+startTimePrefix.length() + startTimeStr.length()
						+ endTimePrefix.length() + endTimeStr.length() + 1);

		builder.append(startTimePrefix);
		builder.append(startTimeStr);
		builder.append(endTimePrefix);
		builder.append(endTimeStr);
		builder.append(suffix);

		return builder.toString();
	}

}