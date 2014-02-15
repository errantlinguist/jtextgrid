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
package com.github.errantlinguist.io;

import java.io.Serializable;

/**
 * An exception representing an error while parsing input.
 * 
 * @author Todd Shore <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @since 2014-02-06
 * @version 2014-02-06
 * 
 */
public class ParseException extends Exception {

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -2447519287942230690L;

	public ParseException() {
		super();
	}

	public ParseException(final String message) {
		super(message);
	}

	public ParseException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ParseException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParseException(final Throwable cause) {
		super(cause);
	}

}
