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
package com.github.errantlinguist.textgrid.io;

import java.io.IOException;
import java.io.InputStream;

import com.github.errantlinguist.io.InputStreamReader;
import com.github.errantlinguist.io.ParseException;
import com.github.errantlinguist.io.Parser;
import com.github.errantlinguist.textgrid.TextGridFile;
import com.github.errantlinguist.textgrid.TextGridFileListenerFactory;

/**
 * A file reader which creates one new {@link TextGridFile} object per file
 * read.
 * 
 * @param <D>
 *            The type of the annotation data.
 * 
 * @since 2014-02-11
 * @version 2014-02-11
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class TextGridFileFactoryReader<D> implements
		InputStreamReader<TextGridFile<D>, ParseException> {

	/**
	 * The factory used for creating {@link TextGridFile} objects.
	 */
	private final TextGridFileListenerFactory<D> factory;

	/**
	 * The reader used for reading TextGrid file contents.
	 */
	private final TextGridFileReader<D> reader;

	/**
	 * 
	 * @param parser
	 *            The {@link Parser} used for reading TextGrid file entry data.
	 */
	public TextGridFileFactoryReader(
			final Parser<? super String, ? extends D> parser) {
		this(parser, new TextGridFileListenerFactory<D>());
	}

	/**
	 * 
	 * @param parser
	 *            The {@link Parser} used for reading TextGrid file entry data.
	 * @param factory
	 *            The factory used for creating {@link TextGridFile} objects.
	 */
	public TextGridFileFactoryReader(
			final Parser<? super String, ? extends D> parser,
			final TextGridFileListenerFactory<D> factory) {
		this.factory = factory;
		this.reader = new TextGridFileReader<D>(parser, factory);
	}

	@Override
	public TextGridFile<D> read(final InputStream input)
			throws IOException, ParseException {
		final TextGridFile<D> result;
		final Boolean finishedReading = reader.read(input);
		if (finishedReading) {
			result = factory.create();
		} else {
			throw new ParseException(
					"Reading of the input TextGrid file halted before finishing.");
		}
		return result;
	}

}
