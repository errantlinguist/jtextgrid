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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.github.errantlinguist.ClassName;
import com.github.errantlinguist.io.DummyParser;
import com.github.errantlinguist.io.ParseException;
import com.github.errantlinguist.io.Parser;
import com.github.errantlinguist.io.Printer;
import com.github.errantlinguist.io.PrintingForwardingFileSystemReader;
import com.github.errantlinguist.textgrid.TextGridFile;
import com.github.errantlinguist.textgrid.TextGridFileListenerFactory;

/**
 * A file reader which creates one new {@link TextGridFile} object per file read
 * and prints it using a given {@link Printer}.
 * 
 * @param <D>
 *            The type of the annotation data.
 * 
 * @since 2014-02-11
 * @version 2014-02-11
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class PrintingTextGridFileReader<D> extends
		PrintingForwardingFileSystemReader<TextGridFile<D>, ParseException> {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(final String[] args) throws IOException,
			ParseException {
		if (args.length != 1) {
			printUsage();
			System.exit(64);
		} else {
			final String inpath = args[0];
			final PrintStream out = System.out;
			try {
			process(inpath, out, out);
			} catch (final FileNotFoundException e) {
				final String message = e.getMessage();
				System.err.println(message);
				System.exit(66);
			} catch (final IOException e) {
				final String message = e.getMessage();
				System.err.println(message);
				System.exit(74);
			}
		}

	}

	private static final void printUsage() {
		final String mainClassName = ClassName.getMainClassName();
		System.err.println(String.format("Usage: %s <infile>", mainClassName));
	}

	private static final void process(final String inpath,
			final PrintStream out, final PrintStream err) throws IOException,
			ParseException {
		final DummyParser<String> parser = new DummyParser<String>();
		final Printer<TextGridFile<String>> printer = new TextGridFileTabularPrintStreamPrinter<String>(
				out);
		final PrintingTextGridFileReader<String> reader = new PrintingTextGridFileReader<String>(
				err, parser, printer);

		// System.err.println(String.format("Reading path \"%s\"...", inpath));
		reader.readPath(inpath);
		// System.err.println("Finished reading path.");
	}

	/**
	 * The {@link Printer} to use for printing read {@link TextGridFile TextGrid
	 * files}.
	 */
	private final Printer<TextGridFile<D>> printer;

	/**
	 * 
	 * @param parser
	 *            The {@link Parser} used for parsing the TextGrid file entry
	 *            annotation data.
	 * @param printer
	 *            The {@link Printer} used for printing the TextGrid file
	 *            contents.
	 * 
	 */
	public PrintingTextGridFileReader(final PrintStream out,
			final Parser<? super String, ? extends D> parser,
			final Printer<TextGridFile<D>> printer) {
		this(out, parser, printer, new TextGridFileListenerFactory<D>());
	}

	/**
	 * 
	 * @param parser
	 *            The {@link Parser} used for parsing the TextGrid file entry
	 *            annotation data.
	 * @param printer
	 *            The {@link Printer} used for printing the TextGrid file
	 *            contents.
	 * @param factory
	 *            The
	 * 
	 */
	public PrintingTextGridFileReader(final PrintStream out,
			final Parser<? super String, ? extends D> parser,
			final Printer<TextGridFile<D>> printer,
			final TextGridFileListenerFactory<D> factory) {
		super(out, new TextGridFileFactoryReader<D>(parser, factory));
		this.printer = printer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.errantlinguist.io.ForwardingFileSystemReader#read(java.io.
	 * InputStream)
	 */
	@Override
	public TextGridFile<D> read(final InputStream input) throws IOException,
			ParseException {
		final TextGridFile<D> result = super.read(input);
		printer.print(result);
		return result;
	}

}
