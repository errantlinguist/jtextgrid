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
 * A parser which parses a single line in a file of a given type.
 * 
 * @author Todd Shore
 * @version 2011-07-06
 * @since 2011-07-06
 * 
 * @param <T>
 *            The object type representing the line data.
 */
public interface FileParser<T> {

	/**
	 * Parses file data and returns a data structure representing its
	 * denoted information.
	 * 
	 * @param line
	 *            A line of data representing the interval data to be parsed.
	 * @return A new object of the parameterised type representing the interval.
	 * @throws Exception
	 *             If a parse error occurs.
	 */
	public T parse(final String line) throws Exception;

}
