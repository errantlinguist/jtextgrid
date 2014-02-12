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

import java.io.Serializable;

/**
 * A {@link Parser} which returns the same input it parses.
 * 
 * @param <T>
 *            The data type to be parsed and returned.
 * 
 * @since 2012-01-16
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 * 
 * 
 * 
 */
public final class DummyParser<T> implements Parser<T, T>, Serializable {

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = 142466668617417879L;

	@Override
	public final T parse(final T input) throws ParseException {
		return input;
	}

}
