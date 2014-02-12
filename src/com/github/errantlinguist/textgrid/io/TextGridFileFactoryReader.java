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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import com.github.errantlinguist.ClassName;
import com.github.errantlinguist.io.AbstractFileSystemReader;
import com.github.errantlinguist.io.DummyParser;
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
public class TextGridFileFactoryReader<D> extends
		AbstractFileSystemReader<TextGridFile<D>, ParseException> {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(final String[] args) throws IOException,
			ParseException {
		if (args.length < 1) {
			printUsage();
			System.exit(64);
		} else {

			final DummyParser<String> parser = new DummyParser<String>();
			final TextGridFileFactoryReader<String> reader = new TextGridFileFactoryReader<String>(
					parser);

			final String inpath = args[0];
			System.err.println(String.format("Reading path \"%s\"...", inpath));
			final Map<File, TextGridFile<String>> results = reader
					.readPath(inpath);
			System.err.println("Finished reading path.");
			final TextGridFileMapTabularPrintStreamPrinter<String> printer = new TextGridFileMapTabularPrintStreamPrinter<String>(
					System.out);
			printer.print(results);

		}

	}

	private static final void printUsage() {
		final String mainClassName = ClassName.getMainClassName();
		System.err.println(String.format("Usage: %s <infile>", mainClassName));
	}

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
	 * @param reader
	 *            The reader used for reading TextGrid file contents.
	 */
	public TextGridFileFactoryReader(
			final Parser<? super String, ? extends D> parser) {
		this(parser, new TextGridFileListenerFactory<D>());
	}

	/**
	 * 
	 * @param reader
	 *            The reader used for reading TextGrid file contents.
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
	public TextGridFile<D> read(final FileInputStream input)
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
