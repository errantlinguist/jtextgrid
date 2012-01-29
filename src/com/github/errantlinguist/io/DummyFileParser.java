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

/**
 * A {@link FileParser} which returns the same string it parses.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2012-01-16
 * 
 */
public final class DummyFileParser implements FileParser<String> {

	/**
	 * A holder class for the static singleton {@link DummyFileParser} instance.
	 * 
	 * @author Todd Shore
	 * @version 2012-01-16
	 * @since 2012-01-16
	 * 
	 */
	private static final class SingletonHolder {

		/**
		 * The static singleton {@link DummyFileParser} instance.
		 */
		private static final DummyFileParser INSTANCE = new DummyFileParser();
	}

	/**
	 * 
	 * @return The static singleton {@link DummyFileParser} instance.
	 */
	public static DummyFileParser getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private DummyFileParser() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	public final String parse(final String line) throws Exception {
		return line;
	}

}
