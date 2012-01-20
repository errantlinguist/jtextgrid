/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.github.errantlinguist.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * An abstract file reader class which reads in a data file or a directory of
 * files, parses it/them, and returns an object representing the data therein.
 * 
 * @author Todd Shore
 * @version 2011-07-06
 * @since 2011-07-06
 * 
 * @param <T>
 *            The object type representing the file data.
 */
public abstract class FileReader<T> {

	/**
	 * Makes a {@link List} of files in a directory and any of its
	 * sub-directories.
	 * 
	 * @param indir
	 *            The directory to be listed.
	 * @return A <code>List</code> of {@link File} objects representing all
	 *         matching files in the directory and its sub-directories.
	 */
	private static final List<File> getDirContents(final File indir) {
		final List<File> recursiveContents = new ArrayList<File>();
		final Stack<File> dirsToExpand = new Stack<File>();
		dirsToExpand.add(indir);
		while (!dirsToExpand.isEmpty()) {

			final File[] dirContents = indir.listFiles();
			for (final File file : dirContents) {
				if (file.isFile()) {
					recursiveContents.add(file);
				} else if (file.isDirectory()) {
					dirsToExpand.add(file);
				}
			}

		}

		return recursiveContents;
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
	private static final List<File> getDirContents(final File indir,
			final FilenameFilter filenameFilter) {
		final List<File> recursiveContents = new ArrayList<File>();
		final Stack<File> dirsToExpand = new Stack<File>();
		dirsToExpand.add(indir);
		while (!dirsToExpand.isEmpty()) {

			final File[] dirContents = indir.listFiles(filenameFilter);
			for (final File file : dirContents) {
				if (file.isFile()) {
					recursiveContents.add(file);
				} else if (file.isDirectory()) {
					dirsToExpand.add(file);
				}
			}

		}

		return recursiveContents;
	}

	/**
	 * Reads the files contained in a given directory and returns the data
	 * contained therein as a new {@link Map} of objects.
	 * 
	 * @param indir
	 *            The directory of files to be read.
	 * @return A <code>Map</code> of filenames as keys with objects as values
	 *         representing the input files.
	 * @throws IOException
	 *             If the <code>indir</code> is not a valid directory or another
	 *             I/O error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final Map<String, T> readDir(final File indir) throws IOException,
			Exception {
		final List<File> dirFiles = getDirContents(indir);
		final Map<String, T> dirContents = new HashMap<String, T>(
				dirFiles.size());

		for (final File file : dirFiles) {
			final T fileContents = readFile(file);
			dirContents.put(file.getCanonicalPath(), fileContents);
		}

		return dirContents;
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
	 * @return A <code>Map</code> of filenames as keys with objects as values
	 *         representing the input files.
	 * @throws IOException
	 *             If the <code>indir</code> is not a valid directory or another
	 *             I/O error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final Map<String, T> readDir(final File indir,
			final FilenameFilter filenameFilter) throws IOException, Exception {
		final List<File> dirFiles = getDirContents(indir, filenameFilter);
		final Map<String, T> dirContents = new HashMap<String, T>(
				dirFiles.size());

		for (final File file : dirFiles) {
			final T fileContents = readFile(file);
			dirContents.put(file.getCanonicalPath(), fileContents);
		}

		return dirContents;
	}

	/**
	 * Reads the files contained in a given directory and returns the data
	 * contained therein as a new {@link Map} of objects.
	 * 
	 * @param indir
	 *            The directory of files to be read.
	 * @return A <code>Map</code> of filenames as keys with objects as values
	 *         representing the input files.
	 * @throws IOException
	 *             If the <code>indir</code> is not a valid directory or another
	 *             I/O error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final Map<String, T> readDir(final String indir) throws IOException,
			Exception {
		return readDir(new File(indir));
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
	 * @return A <code>Map</code> of filenames as keys with objects as values
	 *         representing the input files.
	 * @throws IOException
	 *             If the <code>indir</code> is not a valid directory or another
	 *             I/O error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final Map<String, T> readDir(final String indir,
			final FilenameFilter filenameFilter) throws IOException, Exception {
		return readDir(new File(indir), filenameFilter);
	}

	/**
	 * Reads a file and returns the data contained therein as a new object.
	 * 
	 * @param infile
	 *            The {@link File} to be read.
	 * @return An object representing the input file.
	 * @throws IOException
	 *             If the input {@link File} object does not refer to a valid
	 *             file or another I/O error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public abstract T readFile(final File infile) throws IOException, Exception;

	/**
	 * Reads a file and returns the data contained therein as a new object.
	 * 
	 * @param infile
	 *            The file to be read.
	 * @return An object representing the input file.
	 * @throws IOException
	 *             If the specified file path does not refer to a valid file or
	 *             another I/O error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final T readFile(final String infile) throws IOException, Exception {
		return readFile(new File(infile));
	}

	/**
	 * Reads the file or directory denoted by a given path which match a given
	 * {@link FilenameFilter} and returns the data contained therein as a new
	 * {@link Map} of objects.
	 * 
	 * @param inpath
	 *            The path to be read.
	 * @return A <code>Map</code> of filenames as keys with objects as values
	 *         representing the input file(s).
	 * @throws IOException
	 *             If <code>inpath</code> is not a valid path or another I/O
	 *             error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final Map<String, T> readPath(final File inpath) throws IOException,
			Exception {
		if (inpath.isDirectory()) {
			return readDir(inpath);
		} else {

			if (inpath.isFile()) {
				final Map<String, T> dirContents = new HashMap<String, T>();
				final T fileContents = readFile(inpath);
				dirContents.put(inpath.getCanonicalPath(), fileContents);
				return dirContents;

			} else {
				throw new IOException("Not a normal file: "
						+ inpath.getCanonicalPath());
			}

		}
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
	 * @return A <code>Map</code> of filenames as keys with objects as values
	 *         representing the input file(s).
	 * @throws IOException
	 *             If <code>inpath</code> is not a valid path or another I/O
	 *             error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final Map<String, T> readPath(final File inpath,
			final FilenameFilter filenameFilter) throws IOException, Exception {
		if (inpath.isDirectory()) {
			return readDir(inpath, filenameFilter);
		} else {

			if (inpath.isFile()) {
				final Map<String, T> dirContents = new HashMap<String, T>();
				final T fileContents = readFile(inpath);
				dirContents.put(inpath.getCanonicalPath(), fileContents);
				return dirContents;

			} else {
				throw new IOException("Not a normal file: "
						+ inpath.getCanonicalPath());
			}

		}
	}

	/**
	 * Reads the file or directory denoted by a given path which match a given
	 * {@link FilenameFilter} and returns the data contained therein as a new
	 * {@link Map} of objects.
	 * 
	 * @param inpath
	 *            The path to be read.
	 * @return A <code>Map</code> of filenames as keys with objects as values
	 *         representing the input file(s).
	 * @throws IOException
	 *             If <code>inpath</code> is not a valid path or another I/O
	 *             error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final Map<String, T> readPath(final String inpath)
			throws IOException, Exception {
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
	 * @return A <code>Map</code> of filenames as keys with objects as values
	 *         representing the input file(s).
	 * @throws IOException
	 *             If <code>inpath</code> is not a valid path or another I/O
	 *             error occurs.
	 * @throws Exception
	 *             If there is an otherwise unchecked {@link Exception} thrown,
	 *             e.g.&nbsp;from a subclass.
	 */
	public final Map<String, T> readPath(final String inpath,
			final FilenameFilter filenameFilter) throws IOException, Exception {
		return readPath(new File(inpath), filenameFilter);
	}

}
