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

import com.google.common.base.StandardSystemProperty;
import com.github.errantlinguist.io.Printer;
import com.github.errantlinguist.io.ToStringPrintStreamPrinter;
import com.github.errantlinguist.textgrid.Entry;
import com.github.errantlinguist.time.Duration;
import com.github.errantlinguist.time.io.DurationTabularPrintStreamPrinter;

/**
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-11
 * @since 2014-02-11
 * 
 * @param <D>
 *            The annotation data type.
 * 
 */
public class EntryTabularPrintStreamPrinter<D> implements
		Printer<Entry<? extends D>> {

	/**
	* The default table column separator.
	*/
	private static final CharSequence DEFAULT_COLUMN_SEPARATOR = "\t";
	
	/**
	* The default table row separator.
	*/	
	private static final CharSequence DEFAULT_ROW_SEPARATOR = StandardSystemProperty.LINE_SEPARATOR
			.value();

	/**
	* The table column separator.
	*/
	private final CharSequence columnSeparator;

	/**
	 * The {@link Printer} used for printing entry annotation data.
	 */
	private final Printer<? super D> dataPrinter;

	/**
	 * The {@link Printer} used for printing {@link Duration durations}.
	 */	
	private final Printer<Duration<?>> durationPrinter;

	/**
	* The {@link PrintStream} to print to.
	*/
	private final PrintStream out;

	/**
	* The table row separator.
	*/
	private final CharSequence rowSeparator;

	/**
	 * @param out The {@link PrintStream} to print to.
	 */
	public EntryTabularPrintStreamPrinter(final PrintStream out) {
		this(out, new ToStringPrintStreamPrinter(out));
	}

	/**
	 * @param out The {@link PrintStream} to print to.
	 * @param columnSeparator The table column separator.
	 * @param rowSeparator The table row separator.
	 */
	public EntryTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator, final CharSequence rowSeparator) {
		this(out, columnSeparator, rowSeparator,
				new ToStringPrintStreamPrinter(out));
	}

	/**
	 * @param out The {@link PrintStream} to print to.
	 * @param columnSeparator The table column separator.
	 * @param rowSeparator The table row separator.	 
	 * @param dataPrinter The {@link Printer} used for printing entry annotation data.
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

	/**
	 * @param out The {@link PrintStream} to print to.
	 * @param dataPrinter The {@link Printer} used for printing entry annotation data.
	 */
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

	/**
	* @param output The {@link Duration} to print.
	*/
	private void print(final Duration<?> output) {
		out.print("Duration:");
		out.print(columnSeparator);
		durationPrinter.print(output);
	}

	/**
	* @param output The entry annotation data to print.
	*/
	private void printData(final D output) {
		out.print("Data:");
		out.print(columnSeparator);
		dataPrinter.print(output);
	}

}
