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
 * A mutable extension of the {@link DoubleDuration} interface.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 * 
 */
public interface MutableDoubleDuration extends DoubleDuration,
		MutableDuration<Double>, Comparable<DoubleDuration> {

	/**
	 * @param time
	 *            The end time.
	 */
	void setEndTimeValue(double time);

	/**
	 * @param time
	 *            The start time.
	 */
	void setStartTimeValue(double time);

}