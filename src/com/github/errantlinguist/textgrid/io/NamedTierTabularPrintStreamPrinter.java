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
import java.util.ListIterator;

import com.github.errantlinguist.StandardSystemProperty;
import com.github.errantlinguist.io.Printer;
import com.github.errantlinguist.textgrid.BasicEntry;
import com.github.errantlinguist.textgrid.Entry;
import com.github.errantlinguist.textgrid.NamedTier;
import com.github.errantlinguist.time.Duration;
import com.github.errantlinguist.time.printers.DurationTabularPrintStreamPrinter;
import com.github.errantlinguist.tree.ChildList;

/**
 * @author tsh
 * 
 * @param <D>
 *            The annotation data type.
 * 
 */
public class NamedTierTabularPrintStreamPrinter<D> implements
		Printer<NamedTier<D>> {

	private static final CharSequence DEFAULT_COLUMN_SEPARATOR = "\t";
	private static final CharSequence DEFAULT_ROW_SEPARATOR = StandardSystemProperty.LINE_SEPARATOR
			.value();

	private final CharSequence columnSeparator;

	private final Printer<Duration<?>> durationPrinter;
	private final Printer<? super Entry<D>> entryPrinter;

	private final PrintStream out;

	private final CharSequence rowSeparator;

	public NamedTierTabularPrintStreamPrinter(final PrintStream out) {
		this(out, new EntryTabularPrintStreamPrinter<D>(out));
	}

	public NamedTierTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator, final CharSequence rowSeparator) {
		this(out, columnSeparator, rowSeparator,
				new EntryTabularPrintStreamPrinter<D>(out));
	}

	/**
	 * 
	 */
	public NamedTierTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator,
			final CharSequence rowSeparator,
			final Printer<? super Entry<D>> entryPrinter) {
		this.out = out;
		this.columnSeparator = columnSeparator;
		this.rowSeparator = rowSeparator;
		this.entryPrinter = entryPrinter;
		durationPrinter = new DurationTabularPrintStreamPrinter(out,
				columnSeparator, rowSeparator);
	}

	public NamedTierTabularPrintStreamPrinter(final PrintStream out,
			final Printer<? super Entry<D>> entryPrinter) {
		this(out, DEFAULT_COLUMN_SEPARATOR, DEFAULT_ROW_SEPARATOR, entryPrinter);
	}

	@Override
	public void print(final NamedTier<D> output) {
		print(output.getDuration());
		out.print(rowSeparator);

		out.print("Entries:");
		out.print(rowSeparator);

		final ChildList<NamedTier<D>, BasicEntry<D>> children = output
				.getChildren();
		for (final ListIterator<BasicEntry<D>> iter = children.listIterator(1); iter
				.hasNext();) {
			final int index = iter.nextIndex();
			final BasicEntry<D> entry = iter.next();
			if (entry != null) {
				out.print("Entry " + index);
				out.print(rowSeparator);
				entryPrinter.print(entry);
				// Entry printer already prints a new row
			}
		}

	}

	private void print(final Duration<?> output) {
		out.print("Duration:");
		out.print(columnSeparator);
		durationPrinter.print(output);
	}

}
