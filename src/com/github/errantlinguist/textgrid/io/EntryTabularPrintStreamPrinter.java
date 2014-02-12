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

import java.io.PrintStream;

import com.github.errantlinguist.StandardSystemProperty;
import com.github.errantlinguist.io.Printer;
import com.github.errantlinguist.io.ToStringPrintStreamPrinter;
import com.github.errantlinguist.textgrid.Entry;
import com.github.errantlinguist.time.Duration;
import com.github.errantlinguist.time.printers.DurationTabularPrintStreamPrinter;

/**
 * @author tsh
 * 
 * @param <D>
 *            The annotation data type.
 * 
 */
public class EntryTabularPrintStreamPrinter<D> implements
		Printer<Entry<? extends D>> {

	private static final CharSequence DEFAULT_COLUMN_SEPARATOR = "\t";
	private static final CharSequence DEFAULT_ROW_SEPARATOR = StandardSystemProperty.LINE_SEPARATOR
			.value();

	private final CharSequence columnSeparator;

	private final Printer<? super D> dataPrinter;
	private final Printer<Duration<?>> durationPrinter;

	private final PrintStream out;

	private final CharSequence rowSeparator;

	public EntryTabularPrintStreamPrinter(final PrintStream out) {
		this(out, new ToStringPrintStreamPrinter(out));
	}

	public EntryTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator, final CharSequence rowSeparator) {
		this(out, columnSeparator, rowSeparator,
				new ToStringPrintStreamPrinter(out));
	}

	/**
	 * 
	 */
	public EntryTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator,
			final CharSequence rowSeparator,
			final Printer<? super D> dataPrinter) {
		this.out = out;
		this.columnSeparator = columnSeparator;
		this.rowSeparator = rowSeparator;
		this.dataPrinter = dataPrinter;
		durationPrinter = new DurationTabularPrintStreamPrinter(out,
				columnSeparator, rowSeparator);
	}

	public EntryTabularPrintStreamPrinter(final PrintStream out,
			final Printer<? super D> dataPrinter) {
		this(out, DEFAULT_COLUMN_SEPARATOR, DEFAULT_ROW_SEPARATOR, dataPrinter);
	}

	@Override
	public void print(final Entry<? extends D> output) {
		print(output.getDuration());
		out.print(rowSeparator);

		printData(output.getData());
		out.print(rowSeparator);

	}

	private void print(final Duration<?> output) {
		out.print("Duration:");
		out.print(columnSeparator);
		durationPrinter.print(output);
	}

	private void printData(final D output) {
		out.print("Data:");
		out.print(columnSeparator);
		dataPrinter.print(output);
	}

}
