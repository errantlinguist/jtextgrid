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

/**
 * An interface for listening to parsing events called by {@link
 * TextGridFileReader.readFile(File)}.
 * 
 * @param <D>
 *            The type of the annotation data.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public interface TextGridListener<D> {
	/**
	 * 
	 * @param time
	 *            The TextGrid file end time.
	 */
	void notifyFileEndTime(double time);

	/**
	 * 
	 * @param size
	 *            The TextGrid file size, measured in the number of tiers it
	 *            contains.
	 */
	void notifyFileSize(int size);

	/**
	 * 
	 * @param time
	 *            The TextGrid file start time.
	 */
	void notifyFileStartTime(double time);

	/**
	 * 
	 * @param data
	 *            The next parsed interval annotation data.
	 */
	void notifyIntervalData(D data);

	/**
	 * 
	 * @param time
	 *            The next parsed interval end time.
	 */
	void notifyIntervalEndTime(double time);

	/**
	 * 
	 * @param index
	 *            The next parsed interval index in its corresponding tier.
	 */
	void notifyIntervalIndex(int index);

	/**
	 * 
	 * @param time
	 *            The next parsed interval start time.
	 */
	void notifyIntervalStartTime(double time);

	/**
	 * 
	 * @param data
	 *            The next parsed point annotation data.
	 */
	void notifyPointData(D data);

	/**
	 * 
	 * @param index
	 *            The next parsed point index in its corresponding tier.
	 */
	void notifyPointIndex(int index);

	/**
	 * 
	 * @param time
	 *            The next parsed point time.
	 */
	void notifyPointTime(double time);

	/**
	 * 
	 * @param tierClass
	 *            The next parsed {@link TierClass tier class}.
	 */
	void notifyTierClass(TierClass tierClass);

	/**
	 * 
	 * @param time
	 *            The next parsed tier end time.
	 */
	void notifyTierEndTime(double time);

	/**
	 * 
	 * @param index
	 *            The next parsed tier index in the TextGrid file.
	 */
	void notifyTierIndex(int index);

	/**
	 * 
	 * @param count
	 *            The number of intervals contained by the tier being parsed.
	 */
	void notifyTierIntervalCount(int count);

	/**
	 * 
	 * @param name
	 *            The next parsed tier name.
	 */
	void notifyTierName(String name);

	/**
	 * 
	 * @param count
	 *            The number of points contained by the tier being parsed.
	 */
	void notifyTierPointCount(int count);

	/**
	 * 
	 * @param time
	 *            The next parsed tier start time.
	 */
	void notifyTierStartTime(double time);
}