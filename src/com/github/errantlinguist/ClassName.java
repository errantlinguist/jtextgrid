/*
 * 	Copyright 2013 Todd Shore
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

/**
 * A utility class for miscellaneous methods for {@link Class class}-related
 * meta-information.
 * 
 * @author <a href="mailto:errantlinguist@gmail.com">Todd Shore</a>
 */
public final class ClassName {

	/**
	 * The name of the Java system property which denotes the Java main class.
	 */
	public static final String JAVA_MAIN_CLASS_PROPERTY_NAME = "JAVA_MAIN_CLASS";

	/**
	 * Attempts to get the name of the class which contains the main method
	 * executed in the given runtime instance, first checking for the existence
	 * of a property which starts with {@link #JAVA_MAIN_CLASS_PROPERTY_NAME},
	 * and, if such a property does not exist, all current threads are searched
	 * for one named {@link #MAIN_THREAD_NAME}.
	 * 
	 * @return The name of the class which contains the main method executed in
	 *         the given runtime instance, or {@code null} if the main method
	 *         was not found.
	 */
	public static final String getMainClassName() {

		String result = null;
		// <http://stackoverflow.com/a/940035>
		result = getMainClassNameSystemEnvironmentProperty();

		if (result == null) {
			result = getThreadClassName(StackTrace.MAIN_THREAD_NAME);
		}

		return result;

	}

	/**
	 * Gets the name of the class containing the execution point at the top of
	 * the execution stack for a {@link Thread} with a given name.
	 * 
	 * @param threadName
	 *            The name of the {@code Thread} for which to get the class
	 *            name.
	 * @return The name of the class containing the last execution point for the
	 *         {@code Thread} with the given name or {@code null} if none was
	 *         found for that name.
	 */
	public static final String getThreadClassName(final String threadName) {
		String result = null;

		final StackTraceElement[] stackTrace = StackTrace
				.getStackTrace(threadName);
		if (stackTrace != null) {
			final StackTraceElement first = stackTrace[stackTrace.length];
			result = first.getClassName();
		}

		return result;
	}

	/**
	 * 
	 * @return The name of the class which contains the main method executed in
	 *         the given runtime instance or {@code null} if the main method was
	 *         not found in the system environment variables.
	 * @see <a
	 *      href="http://stackoverflow.com/a/940035">http://stackoverflow.com/a/940035</a>
	 */
	private static final String getMainClassNameSystemEnvironmentProperty() {
		return SystemEnvironment
				.getSystemEnvironmentProperty(JAVA_MAIN_CLASS_PROPERTY_NAME);
	}

	private ClassName() {
		// Avoid instantiation
	}

}
