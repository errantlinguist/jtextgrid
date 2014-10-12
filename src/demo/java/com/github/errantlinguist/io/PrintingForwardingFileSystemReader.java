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
package com.github.errantlinguist.io;

import java.io.File;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * A {@link #ForwardingFileSystemReader} which prints a message to a given {@link #PrintStream} before and after each call.
 *
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-11
 * @since 2014-02-11
 * 
 */
public class PrintingForwardingFileSystemReader<O, E extends Throwable> extends
		ForwardingFileSystemReader<O, E> {

	/**
	* A default {@link Map} of {@link Hook hooks} to the format string used to format messages to be printed when calling the given hook.
	*/
	private static final Map<Hook, String> DEFAULT_HOOK_MESSAGE_FORMAT_STRS = createDefaultHookMessageFormatStrMap();

	/**
	* @return A new {@link Map} of {@link Hook hooks} to the format string used to format messages to be printed when calling the given hook.
	*/
	private static final Map<Hook, String> createDefaultHookMessageFormatStrMap() {
		final Map<Hook, String> result = new EnumMap<Hook, String>(Hook.class);

		final String beforeReadingMessageFormatStrFormatStr = "Reading %s \"%%s\"...%n";
		result.put(Hook.BEFORE_READING_FILE,
				String.format(beforeReadingMessageFormatStrFormatStr, "file"));
		result.put(Hook.BEFORE_READING_DIRECTORY, String.format(
				beforeReadingMessageFormatStrFormatStr, "directory"));
		result.put(Hook.BEFORE_READING_PATH,
				String.format(beforeReadingMessageFormatStrFormatStr, "path"));

		final String afterReadingMessageFormatStrFormatStr = "Finished reading %s \"%%s\".%n";
		result.put(Hook.AFTER_READING_FILE,
				String.format(afterReadingMessageFormatStrFormatStr, "file"));
		result.put(Hook.AFTER_READING_DIRECTORY, String.format(
				afterReadingMessageFormatStrFormatStr, "directory"));
		result.put(Hook.AFTER_READING_PATH,
				String.format(afterReadingMessageFormatStrFormatStr, "path"));

		return result;
	}

	/**
	* A {@link Map} of {@link Hook hooks} to the format string used to format messages to be printed when calling the given hook.
	*/
	private Map<Hook, String> hookMessageFormatStrs;

	/**
	* A reference to the last-read (i.e. opened) {@link File} object.
	*/
	private File lastReadPath = null;
	
	/**
	* The {@link PrintStream} to print to.
	*/
	private PrintStream out;

	/**
	* @param out The {@link PrintStream} to print to.
	* @param reader The {@link InputStreamReader} to delegate stream-reading requests to.
	*/
	public PrintingForwardingFileSystemReader(final PrintStream out,
			final InputStreamReader<O, E> reader) {
		this(out, reader, DEFAULT_HOOK_MESSAGE_FORMAT_STRS);
	}

	/**
	* @param out The {@link PrintStream} to print to.
	* @param reader The {@link InputStreamReader} to delegate stream-reading requests to.	
	* @param hookMessageFormatStrs A {@link Map} of {@link Hook hooks} to the format string used to format messages to be printed when calling the given hook.
	*/
	public PrintingForwardingFileSystemReader(final PrintStream out,
			final InputStreamReader<O, E> reader,
			final Map<Hook, String> hookMessageFormatStrs) {
		super(reader);
		this.out = out;
		this.hookMessageFormatStrs = hookMessageFormatStrs;
	}

	/**
	* Prints a message for a given {@link Hook} to the object's designated {@link #out output stream}.
	* @param hook The {@code Hook} to print a message for.
	* @param args The arguments passed to the hook method.
	*/
	private void print(final Hook hook, final Object... args) {
		final String hookMessageFormatStr = hookMessageFormatStrs.get(hook);
		if (hookMessageFormatStr != null) {
			final String hookMessage = String
					.format(hookMessageFormatStr, args);
			out.print(hookMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.errantlinguist.io.AbstractFileSystemReader#afterReadingDirectory
	 * (java.util.Map)
	 */
	@Override
	protected void afterReadingDirectory(final Map<File, O> result) {
		print(Hook.AFTER_READING_DIRECTORY, lastReadPath);
		super.afterReadingDirectory(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.errantlinguist.io.AbstractFileSystemReader#afterReadingFile
	 * (java.lang.Object)
	 */
	@Override
	protected void afterReadingFile(final O result) {
		super.afterReadingFile(result);
		print(Hook.AFTER_READING_FILE, lastReadPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.errantlinguist.io.AbstractFileSystemReader#afterReadingPath
	 * (java.util.Map)
	 */
	@Override
	protected void afterReadingPath(final Map<File, O> result) {
		super.afterReadingPath(result);
		print(Hook.AFTER_READING_PATH, lastReadPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.errantlinguist.io.AbstractFileSystemReader#beforeReadingDirectory
	 * (java.io.File)
	 */
	@Override
	protected void beforeReadingDirectory(final File indir) {
		super.beforeReadingDirectory(indir);
		print(Hook.BEFORE_READING_DIRECTORY, indir);
		lastReadPath = indir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.errantlinguist.io.AbstractFileSystemReader#beforeReadingFile
	 * (java.io.File)
	 */
	@Override
	protected void beforeReadingFile(final File infile) {
		super.beforeReadingFile(infile);
		print(Hook.BEFORE_READING_FILE, infile);
		lastReadPath = infile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.errantlinguist.io.AbstractFileSystemReader#beforeReadingPath
	 * (java.io.File)
	 */
	@Override
	protected void beforeReadingPath(final File inpath) {
		super.beforeReadingPath(inpath);
		print(Hook.BEFORE_READING_PATH, inpath);
		lastReadPath = inpath;
	}

}
