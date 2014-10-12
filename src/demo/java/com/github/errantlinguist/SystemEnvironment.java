/*
 * 	Copyright 2013--2014 Todd Shore
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
package com.github.errantlinguist;

import java.util.Map.Entry;

/**
 * A utility class for {@link System} facilities.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2013-10-03
 * @since 2013-10-03
 * 
 */
public final class SystemEnvironment {

	/**
	 * Gets the value of a given system environment property
	 * 
	 * @param propertyNamePrefix
	 *            A string matching the beginning of the the name of the
	 *            property of which to get the value.
	 * @return The value of the given property or {@code null} if the property
	 *         was not found.
	 */
	public static final String getSystemEnvironmentProperty(
			final String propertyNamePrefix) {
		String result = null;

		for (final Entry<String, String> entry : System.getenv().entrySet()) {
			if (entry.getKey().startsWith(propertyNamePrefix)) {
				result = entry.getValue();
			}
		}

		return result;
	}

	private SystemEnvironment() {
		// Avoid instantiation
	}

}
