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
 * A mutable extension of the {@link Duration} interface.
 * 
 * @param <T>
 *            The type of the object representing an instant in time.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 * 
 */
public interface MutableDuration<T> extends Duration<T> {

	/**
	 * @param time
	 *            The end time.
	 */
	void setEndTime(T time);

	/**
	 * @param time
	 *            The start time.
	 */
	void setStartTime(T time);

}