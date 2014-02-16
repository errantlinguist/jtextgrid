/*
 * 	Copyright 2012--2014 Todd Shore
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
package com.github.errantlinguist.io;

import java.io.PrintStream;
import java.io.Serializable;

/**
 * A {@link Printer} which simply prints the string representation of the object
 * to print.
 * 
 * @since 2012-01-16
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 * 
 */
public final class ToStringPrintStreamPrinter implements Printer<Object>,
		Serializable {

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = 142466668617417879L;

	/**
	* The {@link PrintStream} to print to.
	*/
	private final PrintStream out;

	/**
	* @param out The {@link PrintStream} to print to.
	*/
	public ToStringPrintStreamPrinter(final PrintStream out) {
		this.out = out;
	}

	@Override
	public void print(final Object output) {
		out.print(output);
	}

}
