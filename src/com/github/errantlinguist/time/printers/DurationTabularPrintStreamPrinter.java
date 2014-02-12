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
package com.github.errantlinguist.time.printers;

import java.io.PrintStream;

import com.github.errantlinguist.StandardSystemProperty;
import com.github.errantlinguist.io.Printer;
import com.github.errantlinguist.time.Duration;

/**
 * A {@link Printer} which prints {@link Duration} objects using a given
 * {@link PrintStream} in a tabular format.
 * 
 * @since 2014-02-11
 * @version 2014-02-11
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class DurationTabularPrintStreamPrinter implements Printer<Duration<?>> {

	private static final CharSequence DEFAULT_COLUMN_SEPARATOR = "\t";
	private static final CharSequence DEFAULT_ROW_SEPARATOR = StandardSystemProperty.LINE_SEPARATOR
			.value();

	private final CharSequence columnSeparator;

	private final PrintStream out;

	// private final CharSequence rowSeparator;

	public DurationTabularPrintStreamPrinter(final PrintStream out) {
		this(out, DEFAULT_COLUMN_SEPARATOR, DEFAULT_ROW_SEPARATOR);
	}

	public DurationTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator) {
		this(out, columnSeparator, DEFAULT_ROW_SEPARATOR);
	}

	/**
	 * 
	 */
	public DurationTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator, final CharSequence rowSeparator) {
		this.out = out;
		this.columnSeparator = columnSeparator;
		// this.rowSeparator = rowSeparator;
	}

	@Override
	public void print(final Duration<?> duration) {
		out.print("Start time");
		out.print(columnSeparator);
		out.print(duration.getStartTime());
		out.print(columnSeparator);

		out.print("End time");
		out.print(columnSeparator);
		out.print(duration.getEndTime());
		out.print(columnSeparator);
	}

}
