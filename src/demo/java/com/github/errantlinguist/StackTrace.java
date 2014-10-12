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

import java.util.Map;
import java.util.Map.Entry;

/**
 * A utility class for {@link Thread} objects.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2013-10-03
 * @since 2013-10-03
 * 
 */
public final class StackTrace {

	/**
	 * The name of the main program thread.
	 */
	static final String MAIN_THREAD_NAME = "main";

	/**
	 * Gets the stack trace for a {@link Thread} with a given name.
	 * 
	 * @param threadName
	 *            The name (i.e.&nbsp; the return value of
	 *            {@link Thread#getName()}) of the {@code Thread} to get.
	 *            <strong>NOTE:</strong> This reference cannot be {@code null}!
	 * @return The stack trace for the {@code Thread} with the given name or
	 *         {@code null} if none was found for that name.
	 */
	public static final StackTraceElement[] getStackTrace(
			final String threadName) {
		StackTraceElement[] result = null;

		final Map<Thread, StackTraceElement[]> allStackTraces = Thread
				.getAllStackTraces();
		for (final Entry<Thread, StackTraceElement[]> stackTrace : allStackTraces
				.entrySet()) {
			final Thread thread = stackTrace.getKey();
			if (threadName.equals(thread.getName())) {
				result = stackTrace.getValue();
			}
		}

		return result;
	}

	private StackTrace() {
		// Avoid instantiation
	}

}
