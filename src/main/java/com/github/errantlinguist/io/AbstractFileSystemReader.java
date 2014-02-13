/*
 * 	Copyright 2011--2014 Todd Shore
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
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * An abstract file reader class which reads in a data file or a directory of
 * files, parses it/them, and returns an object representing the data therein.
 * 
 * @param <O>
 *            The object type representing the file data.
 * @param <E>
 *            A {@link Throwable} type thrown by the non-abstract derived class.
 * 
 * @since 2011-07-06
 * @version 2014-02-05
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 */
public abstract class AbstractFileSystemReader<O, E extends Throwable>
		implements InputStreamReader<O, E> {

	/**
	 * An enumeration of all hooks which can be defined, for usage in the
	 * development of subclasses.
	 * 
	 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
	 * @version 2014-02-05
	 * @since 2014-02-05
	 * 
	 */
	protected enum Hook {
		AFTER_READING_FILE, AFTER_READING_DIRECTORY, AFTER_READING_PATH, BEFORE_READING_DIRECTORY, BEFORE_READING_FILE, BEFORE_READING_PATH;
	}

	/**
	 * Makes a {@link List} of files in a directory and any of its
	 * sub-directories.
	 * 
	 * @param indir
	 *            The directory to be listed.
	 * @return A <code>List</code> of {@link File} objects representing all
	 *         matching files in the directory and its sub-directories.
	 */
	private static final List<File> walkDirectoryContents(final File indir) {
		final List<File> result = new ArrayList<File>();
		final Queue<File> dirsToExpand = new ArrayDeque<File>();
		dirsToExpand.add(indir);
		while (!dirsToExpand.isEmpty()) {
			final File dirToExpand = dirsToExpand.remove();
			final File[] dirContents = dirToExpand.listFiles();
			for (final File file : dirContents) {
				if (file.isFile()) {
					result.add(file);
				} else if (file.isDirectory()) {
					dirsToExpand.add(file);
				}
			}

		}

		return result;
	}

	/**
	 * Makes a {@link List} of files in a directory and any of its
	 * sub-directories.
	 * 
	 * @param indir
	 *            The directory to be listed.
	 * @param filenameFilter
	 *            A {@link FilenameFilter} denoting the filenames to be matched.
	 * @return A <code>List</code> of {@link File} objects representing all
	 *         matching files in the directory and its sub-directories.
	 */
	private static final List<File> walkDirectoryContents(final File indir,
			final FilenameFilter filenameFilter) {
		final List<File> result = new ArrayList<File>();
		final Queue<File> dirsToExpand = new ArrayDeque<File>();
		dirsToExpand.add(indir);
		while (!dirsToExpand.isEmpty()) {
			final File dirToExpand = dirsToExpand.remove();
			final File[] dirContents = dirToExpand.listFiles(filenameFilter);
			for (final File file : dirContents) {
				if (file.isFile()) {
					result.add(file);
				} else if (file.isDirectory()) {
					dirsToExpand.add(file);
				}
			}

		}

		return result;
	}

	/**
	 * Reads the files contained in a given directory and returns the data
	 * contained therein as a new {@link Map} of objects.
	 * 
	 * @param indir
	 *            The directory of files to be read.
	 * @return A <code>Map</code> of {@link File} objects as keys with objects
	 *         as values representing the input files.
	 * @throws IOException
	 *             If the <code>indir</code> is not a valid directory or another
	 *             I/O error occurs.
	 * @throws E
	 *             If there is an otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final Map<File, O> readDirectory(final File indir)
			throws IOException, E {
		beforeReadingDirectory(indir);
		final List<File> dirFiles = walkDirectoryContents(indir);
		final Map<File, O> result = new HashMap<File, O>(dirFiles.size());

		for (final File file : dirFiles) {
			final O fileContents = readFile(file);
			result.put(file, fileContents);
		}

		afterReadingDirectory(result);
		return result;
	}

	/**
	 * Reads the files contained in a given directory which match a given
	 * {@link FilenameFilter} and returns the data contained therein as a new
	 * {@link Map} of objects.
	 * 
	 * @param indir
	 *            The directory of files to be read.
	 * @param filenameFilter
	 *            A <code>FilenameFilter</code> denoting the filenames to be
	 *            read.
	 * @return A <code>Map</code> of {@link File} objects as keys with objects
	 *         as values representing the input files.
	 * @throws IOException
	 *             If the <code>indir</code> is not a valid directory or another
	 *             I/O error occurs.
	 * @throws E
	 *             If there is an otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final Map<File, O> readDirectory(final File indir,
			final FilenameFilter filenameFilter) throws IOException, E {
		beforeReadingDirectory(indir);
		final List<File> dirFiles = walkDirectoryContents(indir, filenameFilter);
		final Map<File, O> result = new HashMap<File, O>(dirFiles.size());

		for (final File file : dirFiles) {
			final O fileContents = readFile(file);
			result.put(file, fileContents);
		}
		afterReadingDirectory(result);
		return result;
	}

	/**
	 * Reads the files contained in a given directory and returns the data
	 * contained therein as a new {@link Map} of objects.
	 * 
	 * @param indir
	 *            The directory of files to be read.
	 * @return A <code>Map</code> of {@link File} objects as keys with objects
	 *         as values representing the input files.
	 * @throws IOException
	 *             If the <code>indir</code> is not a valid directory or another
	 *             I/O error occurs.
	 * @throws E
	 *             If there is an otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final Map<File, O> readDirectory(final String indir)
			throws IOException, E {
		return readDirectory(new File(indir));
	}

	/**
	 * Reads the files contained in a given directory which match a given
	 * {@link FilenameFilter} and returns the data contained therein as a new
	 * {@link Map} of objects.
	 * 
	 * @param indir
	 *            The directory of files to be read.
	 * @param filenameFilter
	 *            A <code>FilenameFilter</code> denoting the filenames to be
	 *            read.
	 * @return A <code>Map</code> of {@link File} objects as keys with objects
	 *         as values representing the input files.
	 * @throws IOException
	 *             If the <code>indir</code> is not a valid directory or another
	 *             I/O error occurs.
	 * @throws E
	 *             If there is an  otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final Map<File, O> readDirectory(final String indir,
			final FilenameFilter filenameFilter) throws IOException, E {
		return readDirectory(new File(indir), filenameFilter);
	}

	/**
	 * Reads a file and returns the data contained therein as a new object.
	 * 
	 * @param infile
	 *            The {@link File} to be read.
	 * @return An object representing the input file.
	 * @throws IOException
	 *             If the input {@code File} object does not refer to a valid
	 *             file or another I/O error occurs.
	 * @throws E
	 *             If there is an  otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final O readFile(final File infile) throws IOException, E {
		beforeReadingFile(infile);
		O result = null;

		final FileInputStream is = new FileInputStream(infile);

		try {
			result = read(is);
		} finally {
			is.close();
		}

		afterReadingFile(result);
		return result;

	}

	/**
	 * Reads a file and returns the data contained therein as a new object.
	 * 
	 * @param infile
	 *            The file to be read.
	 * @return An object representing the input file.
	 * @throws IOException
	 *             If the specified file path does not refer to a valid file or
	 *             another I/O error occurs.
	 * @throws E
	 *             If there is an  otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final O readFile(final String infile) throws IOException, E {
		return readFile(new File(infile));
	}

	/**
	 * Reads the file or directory denoted by a given path which match a given
	 * {@link FilenameFilter} and returns the data contained therein as a new
	 * {@link Map} of objects.
	 * 
	 * @param inpath
	 *            The path to be read.
	 * @return A <code>Map</code> of {@link File} objects as keys with objects
	 *         as values representing the input file(s).
	 * @throws IOException
	 *             If <code>inpath</code> is not a valid path or another I/O
	 *             error occurs.
	 * @throws E
	 *             If there is an otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final Map<File, O> readPath(final File inpath) throws IOException, E {
		final Map<File, O> result;
		beforeReadingPath(inpath);
		if (inpath.isDirectory()) {
			result = readDirectory(inpath);
		} else if (inpath.isFile()) {
			result = new HashMap<File, O>();
			final O fileContents = readFile(inpath);
			result.put(inpath, fileContents);

		} else {
			throw new IOException("Not a normal file: "
					+ inpath.getCanonicalPath());
		}
		afterReadingPath(result);
		return result;
	}

	/**
	 * Reads the file or directory denoted by a given path which match a given
	 * {@link FilenameFilter} and returns the data contained therein as a new
	 * {@link Map} of objects.
	 * 
	 * @param inpath
	 *            The path to be read.
	 * @param filenameFilter
	 *            A <code>FilenameFilter</code> denoting the filenames to be
	 *            read in the case that the path refers to a directory.
	 * @return A <code>Map</code> of {@link File} objects as keys with objects
	 *         as values representing the input file(s).
	 * @throws IOException
	 *             If <code>inpath</code> is not a valid path or another I/O
	 *             error occurs.
	 * @throws E
	 *             If there is an  otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final Map<File, O> readPath(final File inpath,
			final FilenameFilter filenameFilter) throws IOException, E {
		final Map<File, O> result;

		if (inpath.isDirectory()) {
			result = readDirectory(inpath, filenameFilter);
		} else if (inpath.isFile()) {
			result = new HashMap<File, O>();
			final O fileContents = readFile(inpath);
			result.put(inpath, fileContents);

		} else {
			throw new IOException("Not a normal file: "
					+ inpath.getCanonicalPath());
		}

		return result;

	}

	/**
	 * Reads the file or directory denoted by a given path which match a given
	 * {@link FilenameFilter} and returns the data contained therein as a new
	 * {@link Map} of objects.
	 * 
	 * @param inpath
	 *            The path to be read.
	 * @return A <code>Map</code> of {@link File} objects as keys with objects
	 *         as values representing the input file(s).
	 * @throws IOException
	 *             If <code>inpath</code> is not a valid path or another I/O
	 *             error occurs.
	 * @throws E
	 *             If there is an  otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final Map<File, O> readPath(final String inpath) throws IOException,
			E {
		return readPath(new File(inpath));
	}

	/**
	 * Reads the file or directory denoted by a given path which match a given
	 * {@link FilenameFilter} and returns the data contained therein as a new
	 * {@link Map} of objects.
	 * 
	 * @param inpath
	 *            The path to be read.
	 * @param filenameFilter
	 *            A <code>FilenameFilter</code> denoting the files to be read in
	 *            the case that the path refers to a directory.
	 * @return A <code>Map</code> of {@link File} objects as keys with objects
	 *         as values representing the input file(s).
	 * @throws IOException
	 *             If <code>inpath</code> is not a valid path or another I/O
	 *             error occurs.
	 * @throws E
	 *             If there is an  otherwise-unchecked instance of {@link Throwable} thrown from a
	 *             subclass.
	 */
	public final Map<File, O> readPath(final String inpath,
			final FilenameFilter filenameFilter) throws IOException, E {
		return readPath(new File(inpath), filenameFilter);
	}

	/**
	 * A hook method called after calling {@code readDirectory(...)}. Unless
	 * overridden by a subclass, this method does nothing.
	 * 
	 * @param result
	 *            The result of the method calling this hook.
	 */
	protected void afterReadingDirectory(final Map<File, O> result) {
		// Do nothing by default
	}

	/**
	 * A hook method called after calling {@code readFile(...)}. Unless
	 * overridden by a subclass, this method does nothing.
	 * 
	 * @param result
	 *            The result of the method calling this hook.
	 */
	protected void afterReadingFile(final O result) {
		// Do nothing by default
	}

	/**
	 * A hook method called after calling {@code readPath(...)}. Unless
	 * overridden by a subclass, this method does nothing.
	 * 
	 * @param result
	 *            The result of the method calling this hook.
	 */
	protected void afterReadingPath(final Map<File, O> result) {
		// Do nothing by default
	}

	/**
	 * A hook method called after calling {@code readDirectory(...)}. Unless
	 * overridden by a subclass, this method does nothing.
	 * 
	 * @param indir
	 *            The argument passed to the method calling this hook.
	 */
	protected void beforeReadingDirectory(final File indir) {
		// Do nothing by default
	}

	/**
	 * A hook method called after calling {@code readFile(...)}. Unless
	 * overridden by a subclass, this method does nothing.
	 * 
	 * @param infile
	 *            The argument passed to the method calling this hook.
	 */
	protected void beforeReadingFile(final File infile) {
		// Do nothing by default
	}

	/**
	 * A hook method called after calling {@code readPath(...)}. Unless
	 * overridden by a subclass, this method does nothing.
	 * 
	 * @param inpath
	 *            The argument passed to the method calling this hook.
	 */
	protected void beforeReadingPath(final File inpath) {
		// Do nothing by default
	}

}
