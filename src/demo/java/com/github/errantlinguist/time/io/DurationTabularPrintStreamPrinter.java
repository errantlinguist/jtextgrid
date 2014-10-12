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
package com.github.errantlinguist.time.io;

import java.io.PrintStream;

import com.google.common.base.StandardSystemProperty;
import com.github.errantlinguist.io.Printer;
import com.github.errantlinguist.time.Duration;

/**
 * A {@link Printer} which prints {@link Duration} objects using a given
 * {@link PrintStream} in a tabular format.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-11
 * @since 2014-02-11
 * 
 */
public class DurationTabularPrintStreamPrinter implements Printer<Duration<?>> {

	/**
	* The default table column separator.
	*/
	private static final CharSequence DEFAULT_COLUMN_SEPARATOR = "\t";

	/**
	* The table column separator.
	*/
	private final CharSequence columnSeparator;

	/**
	* The {@link PrintStream} to print to.
	*/
	private final PrintStream out;

	/**
	* @param out The {@link PrintStream} to print to.
	*/
	public DurationTabularPrintStreamPrinter(final PrintStream out) {
		this(out, DEFAULT_COLUMN_SEPARATOR);
	}

	/**
	* @param out The {@link PrintStream} to print to.
	* @param columnSeparator The table column separator.
	*/
	public DurationTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator) {
		this.out = out;
		this.columnSeparator = columnSeparator;
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
