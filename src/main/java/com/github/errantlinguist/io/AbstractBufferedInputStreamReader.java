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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * An abstract file reader class which reads in data from an {@link InputStream}
 * and transforms the input into an object of a given generic type.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-06
 * @since 2014-02-06
 *
 * @param <O>
 *            The object type representing the input data.
 * @param <E>
 *            A {@link Throwable} type thrown by the non-abstract derived class.
 *
 */
public abstract class AbstractBufferedInputStreamReader<O, E extends Throwable>
		implements com.github.errantlinguist.io.InputStreamReader<O, E> {

	/**
	 * Reads a {@link BufferedReader} and returns an object as a result.
	 * 
	 * @param input
	 *            The {@code BufferedReader} to be read.
	 * @return An object representing the read input.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws E
	 *             This exception is thrown by the logic of the non-abstract
	 *             derived class.
	 */
	public abstract O read(final BufferedReader input) throws IOException, E;

	/**
	 * Reads an {@link InputStream} and returns an object as a result.
	 * 
	 * @param input
	 *            The {@code InputStream} to be read.
	 * @return An object representing the read input.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws E
	 *             This exception is thrown by the logic of the non-abstract
	 *             derived class.
	 */
	@Override
	public final O read(final InputStream input) throws IOException, E {
		return read(new InputStreamReader(input));
	}

	/**
	 * Reads an {@link InputStreamReader} and returns an object as a result.
	 * 
	 * @param reader
	 *            The {@code InputStreamReader} to be read.
	 * @return An object representing the read input.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws E
	 *             This exception is thrown by the logic of the non-abstract
	 *             derived class.
	 */
	public final O read(final InputStreamReader reader) throws IOException, E {
		return read(new BufferedReader(reader));
	}

}
