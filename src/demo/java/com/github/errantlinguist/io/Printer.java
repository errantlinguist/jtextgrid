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

/**
 * An interface for classes which print information from a given object.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-11
 * @since 2014-02-11 
 * 
 * @param <T>
 *            The type of object to print.
 * 
 */
public interface Printer<T> {

	/**
	 * @param output
	 *            The object to print.
	 */
	void print(T output);

}
