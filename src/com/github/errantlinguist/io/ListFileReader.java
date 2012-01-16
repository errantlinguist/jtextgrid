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
	 * 
	 * @return The static singleton {{@link ListFileReader} instance with a
	 *         {@link DummyFileParser} for {@link ListFileReader#parser}.
	 */
	public static ListFileReader<String> getStringParserInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * The {@link FileParser} to be used for parsing files.
	 */
	protected final FileParser<T> parser;

	/**
	 * 
	 * @param parser
	 *            The {@link FileParser} to be used to parse the file contents.
	 */
	public ListFileReader(final FileParser<T> parser) {
		this.parser = parser;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (parser == null ? 0 : parser.hashCode());
		return result;
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
		final InputStreamReader isr = new InputStreamReader(
				new FileInputStream(infile));
		final BufferedReader br = new BufferedReader(isr);

		final List<T> contents = new ArrayList<T>();

		for (String line = br.readLine(); line != null; line = br.readLine()) {
			final T data = parser.parse(line);
			contents.add(data);
		}

		return contents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ListFileReader[parser=");
		builder.append(parser);
		builder.append("]");
		return builder.toString();
	}

}
