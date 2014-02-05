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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A file reader which reads in and parses each line with a given
 * {@link FileParser}, returning a {@link List} of the parser output.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-07-06
 * 
 */
public class ListFileReader<T> extends FileReader<List<T>> {

	/**
	 * A holder class for the static singleton {@link ListFileReader} instance
	 * with a {@link DummyFileParser} for {@link ListFileReader#parser}.
	 * 
	 * @author Todd Shore
	 * @version 2012-01-16
	 * @since 2012-01-16
	 * 
	 */
	private static final class SingletonHolder {

		/**
		 * The static singleton {@link ListFileReader} instance with a
		 * {@link DummyFileParser} for {@link ListFileReader#parser}.
		 */
		private static final ListFileReader<String> INSTANCE = new ListFileReader<String>(
				DummyFileParser.getInstance());
	}

	/**
	 * A constant value used for estimating the length of the string
	 * representation of the object returned by {@link #toString()}.
	 */
	protected static final int ESTIMATED_STRING_LENGTH = 64;

	/**
	 * 
	 * @return The static singleton {{@link ListFileReader} instance with a
	 *         {@link DummyFileParser} for {@link ListFileReader#parser}.
	 */
	public static ListFileReader<String> getStringParserInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * The pre-cached hash code.
	 */
	private final int hashCode;

	/**
	 * The {@link FileParser} to be used for parsing files.
	 */
	private final FileParser<T> parser;

	/**
	 * 
	 * @param parser
	 *            The {@link FileParser} to be used to parse the file contents.
	 */
	public ListFileReader(final FileParser<T> parser) {
		this.parser = parser;

		this.hashCode = calculateHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ListFileReader<?>)) {
			return false;
		}
		final ListFileReader<?> other = (ListFileReader<?>) obj;
		if (parser == null) {
			if (other.parser != null) {
				return false;
			}
		} else if (!parser.equals(other.parser)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the parser
	 */
	public FileParser<T> getParser() {
		return parser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashCode;
	}

	/**
	 * Reads a {@link BufferedReader} and returns the data contained therein as a
	 * {@link List} of new objects.
	 * 
	 * @param reader The {@code BufferedReader} to be read.
	 * @return A <code>List</code> of objects representing the parsed file
	 *         lines.
	 * @throws Exception If there is a parsing error.
	 */
	public List<T> read(final BufferedReader reader) throws Exception {
		final List<T> result = new ArrayList<T>();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			final T data = parser.parse(line);
			result.add(data);
		}		
		return result;
	}
	
	/**
	 * Reads an {@link InputStream} and returns the data contained therein as a
	 * {@link List} of new objects.
	 * 
	 * @param input The {@code InputStream} to be read.
	 * @return A <code>List</code> of objects representing the parsed file
	 *         lines.
	 * @throws Exception If there is a parsing error.
	 */
	public List<T> read(final InputStream input) throws Exception {
		return read(new InputStreamReader(input));
	}
	
	/**
	 * Reads an {@link InputStreamReader} and returns the data contained therein as a
	 * {@link List} of new objects.
	 * 
	 * @param reader The {@code InputStreamReader} to be read.
	 * @return A <code>List</code> of objects representing the parsed file
	 *         lines.
	 * @throws Exception If there is a parsing error.
	 */
	public List<T> read(final InputStreamReader reader) throws Exception {
		return read(new BufferedReader(reader));
	}
	
	/**
	 * Reads a {@link File} and returns the data contained therein as a
	 * {@link List} of new objects.
	 * 
	 * @param infile
	 *            The <code>File</code> to be read.
	 * @return A <code>List</code> of objects representing the parsed file
	 *         lines.
	 * @throws IOException
	 *             If the input {@link File} object does not refer to a valid
	 *             file or another I/O error occurs.
	 * @throws Exception
	 *             If there is a parsing error.
	 */
	@Override
	public List<T> readFile(final File infile) throws Exception {
		List<T> result = null;

		try (final FileInputStream is = new FileInputStream(infile)){
			result = read(is);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(ESTIMATED_STRING_LENGTH);
		final String className = this.getClass().getSimpleName();
		builder.append(className);
		builder.append("[parser=");
		builder.append(parser);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * 
	 * @return The hash code.
	 */
	private int calculateHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (parser == null ? 0 : parser.hashCode());
		return result;
	}

}
