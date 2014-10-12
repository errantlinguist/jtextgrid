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

import com.google.common.base.StandardSystemProperty;
import com.github.errantlinguist.io.Printer;
import com.github.errantlinguist.textgrid.NamedTier;
import com.github.errantlinguist.textgrid.TextGridFile;
import com.github.errantlinguist.time.Duration;
import com.github.errantlinguist.time.io.DurationTabularPrintStreamPrinter;
import com.github.errantlinguist.tree.ChildList;

/**
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-11
 * @since 2014-02-11
 * 
 * @param <D>
 *            The annotation data type.
 * 
 */
public class TextGridFileTabularPrintStreamPrinter<D> implements
		Printer<TextGridFile<D>> {

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
	 * The {@link Printer} used for printing {@link NamedTier TextGrid file tiers}.
	 */			
	private final Printer<? super NamedTier<D>> tierPrinter;

	/**
	 * @param out The {@link PrintStream} to print to.
	 */
	public TextGridFileTabularPrintStreamPrinter(final PrintStream out) {
		this(out, new NamedTierTabularPrintStreamPrinter<D>(out));
	}

	/**
	 * @param out The {@link PrintStream} to print to.
	 * @param columnSeparator The table column separator.
	 * @param rowSeparator The table row separator.
	 */
	public TextGridFileTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator, final CharSequence rowSeparator) {
		this(out, columnSeparator, rowSeparator,
				new NamedTierTabularPrintStreamPrinter<D>(out, columnSeparator,
						rowSeparator));
	}

	/**
	 * @param out The {@link PrintStream} to print to.
	 * @param columnSeparator The table column separator.
	 * @param rowSeparator The table row separator.
	 * @param tierPrinter The {@link Printer} used for printing {@link NamedTier TextGrid file tiers}.
	 */
	public TextGridFileTabularPrintStreamPrinter(final PrintStream out,
			final CharSequence columnSeparator,
			final CharSequence rowSeparator,
			final Printer<? super NamedTier<D>> tierPrinter) {
		this.out = out;
		this.columnSeparator = columnSeparator;
		this.rowSeparator = rowSeparator;
		this.tierPrinter = tierPrinter;
		durationPrinter = new DurationTabularPrintStreamPrinter(out,
				columnSeparator, rowSeparator);
	}

	/**
	 * @param out The {@link PrintStream} to print to.
	 * @param tierPrinter The {@link Printer} used for printing {@link NamedTier TextGrid file tiers}.
	 */
	public TextGridFileTabularPrintStreamPrinter(final PrintStream out,
			final Printer<? super NamedTier<D>> tierPrinter) {
		this(out, DEFAULT_COLUMN_SEPARATOR, DEFAULT_ROW_SEPARATOR, tierPrinter);
	}

	@Override
	public void print(final TextGridFile<D> output) {
		print(output.getDuration());
		out.print(rowSeparator);

		out.print("Tiers:");
		out.print(rowSeparator);

		final ChildList<TextGridFile<D>, NamedTier<D>> children = output
				.getChildren();
		for (final ListIterator<NamedTier<D>> iter = children.listIterator(1); iter
				.hasNext();) {
			final int index = iter.nextIndex();
			final NamedTier<D> tier = iter.next();
			if (tier != null) {
				out.print("Tier " + index);
				out.print(rowSeparator);
				tierPrinter.print(tier);
				// The Tier printer already prints a new row, so no need to print one here
			}
		}

	}
	
	/**
	* @param output The {@link Duration} to print.
	*/
	private void print(final Duration<?> output) {
		out.print("Duration:");
		out.print(columnSeparator);
		durationPrinter.print(output);
	}

}
