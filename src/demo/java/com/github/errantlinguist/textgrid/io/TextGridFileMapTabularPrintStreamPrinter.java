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
import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;

import com.github.errantlinguist.StandardSystemProperty;
import com.github.errantlinguist.io.Printer;
import com.github.errantlinguist.textgrid.TextGridFile;

/**
 * @author tsh
 * 
 */
public class TextGridFileMapTabularPrintStreamPrinter<D> implements
		Printer<Map<File, ? extends TextGridFile<D>>> {

	private static final CharSequence DEFAULT_COLUMN_SEPARATOR = "\t";
	private static final CharSequence DEFAULT_ROW_SEPARATOR = StandardSystemProperty.LINE_SEPARATOR
			.value();

	// private final CharSequence columnSeparator;

	private final PrintStream out;
	private final CharSequence rowSeparator;

	private final Printer<? super TextGridFile<D>> textGridFilePrinter;

	public TextGridFileMapTabularPrintStreamPrinter(final PrintStream out) {
		this(out, new TextGridFileTabularPrintStreamPrinter<D>(out));
	}

	public TextGridFileMapTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator, final CharSequence rowSeparator) {
		this(out, columnSeparator, rowSeparator,
				new TextGridFileTabularPrintStreamPrinter<D>(out,
						columnSeparator, rowSeparator));
	}

	/**
	 * 
	 */
	public TextGridFileMapTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator,
			final CharSequence rowSeparator,
			final Printer<? super TextGridFile<D>> textGridFilePrinter) {
		this.out = out;
		// this.columnSeparator = columnSeparator;
		this.rowSeparator = rowSeparator;

		this.textGridFilePrinter = textGridFilePrinter;
	}

	public TextGridFileMapTabularPrintStreamPrinter(final PrintStream out,
			final Printer<? super TextGridFile<D>> textGridFilePrinter) {
		this(out, DEFAULT_COLUMN_SEPARATOR, DEFAULT_ROW_SEPARATOR,
				textGridFilePrinter);
	}

	@Override
	public void print(final Map<File, ? extends TextGridFile<D>> output) {
		for (final Entry<File, ? extends TextGridFile<D>> entry : output
				.entrySet()) {
			final String path = entry.getKey().getPath();
			out.print(path);
			out.print(rowSeparator);
			final TextGridFile<D> textGridFile = entry.getValue();
			textGridFilePrinter.print(textGridFile);
			out.print(rowSeparator);

		}
	}

}
