/*
 * 	Copyright 2012--2014 Todd Shore
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

import java.util.HashMap;
import java.util.Map;

/**
 * The TextGrid tier class, e.g.&nbsp;"IntervalTier" or "TextTier", as defined
 * in the TextGrid file itself.
 * 
 * @since 2012-01-16
 * @version 2014-02-07
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public enum TierClass {
	/**
	 * A {@link NamedTier} with {@link Entry} instances which denote a time interval,
	 * i.e.&nbsp;for which the start time is less than the end time.
	 */
	INTERVAL("IntervalTier"),
	/**
	 * A {@link NamedTier} with {@link Entry} instances which denote points,
	 * i.e.&nbsp;for which the start time and end time are equal.
	 */
	TEXT("TextTier");

	private static final Map<String, TierClass> VALUE_INSTANCES = createValueMap();

	/**
	 * Gets the {@link TierClass} represented by the given string value
	 * representing the <code>TierClass</code> in the TextGrid file.
	 * 
	 * @param value
	 *            The string value representing the <code>TierClass</code> in
	 *            the TextGrid file.
	 * @return The <code>TierClass</code> associated with the value.
	 */
	public static TierClass getInstance(final String value) {
		return VALUE_INSTANCES.get(value);
	}

	/**
	 * 
	 * @return A new {@link Map} of {@link TierClass#getValue() tier class
	 *         string values} mapped to their corresponding {@code TierClass}
	 *         instances.
	 */
	private static final Map<String, TierClass> createValueMap() {
		final Map<String, TierClass> result = new HashMap<String, TierClass>(
				TierClass.values().length);
		for (final TierClass tierClass : TierClass.values()) {
			result.put(tierClass.getValue(), tierClass);
		}
		return result;
	}

	/**
	 * The string value representing the <code>TierClass</code> in the TextGrid
	 * file.
	 */
	private final String value;

	/**
	 * 
	 * @param value
	 *            The string value representing the <code>TierClass</code> in
	 *            the TextGrid file.
	 */
	private TierClass(final String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return The string value representing the <code>TierClass</code> in the
	 *         TextGrid file.
	 */
	public String getValue() {
		return value;
	}

}