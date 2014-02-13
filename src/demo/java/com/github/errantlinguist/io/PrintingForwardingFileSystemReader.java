/**
 * 
 */
package com.github.errantlinguist.io;

import java.io.File;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author tsh
 * 
 */
public class PrintingForwardingFileSystemReader<O, E extends Throwable> extends
		ForwardingFileSystemReader<O, E> {

	private static final Map<Hook, String> DEFAULT_HOOK_MESSAGE_FORMAT_STRS = createDefaultHookMessageFormatStrMap();

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

	private Map<com.github.errantlinguist.io.AbstractFileSystemReader.Hook, String> hookMessageFormatStrs;

	private File lastReadPath = null;

	private PrintStream out;

	public PrintingForwardingFileSystemReader(final PrintStream out,
			final InputStreamReader<O, E> reader) {
		this(out, reader, DEFAULT_HOOK_MESSAGE_FORMAT_STRS);
	}

	public PrintingForwardingFileSystemReader(final PrintStream out,
			final InputStreamReader<O, E> reader,
			final Map<Hook, String> hookMessageFormatStrs) {
		super(reader);
		this.out = out;
		this.hookMessageFormatStrs = hookMessageFormatStrs;
	}

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
