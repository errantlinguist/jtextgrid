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

import java.io.IOException;
import java.io.InputStream;

/**
 * A subclass of {@link AbstractFileSystemReader} which delegates file-reading functionality to a given {@link InputStreamReader}.
 *
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-11
 * @since 2014-02-11
 * 
 * @param <O>
 *            The object type representing the file data.
 * @param <E>
 *            A {@link Throwable} type thrown by the non-abstract derived class.
 *  
 */
public class ForwardingFileSystemReader<O, E extends Throwable> extends
		AbstractFileSystemReader<O, E> {

	/**
	 * The {@link InputStreamReader} used for reading file contents.
	 */
	private final InputStreamReader<O, E> reader;

	/**
	 * @param reader
	 *            The {@link InputStreamReader} used for reading file
	 *            contents.
	 */
	public ForwardingFileSystemReader(final InputStreamReader<O, E> reader) {
		this.reader = reader;
	}

	@Override
	public O read(final InputStream input) throws IOException, E {
		return reader.read(input);
	}

}
