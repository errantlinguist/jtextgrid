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
package com.github.errantlinguist;

/**
 * An interface denoting a factory object which returns an object of a given
 * type (it need not necessarily be a new instance, depending on the
 * implementation of the interface).
 * 
  * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-06
 * @since 2014-02-06
 *
 * @param <T>
 *            The type to be created.
 * 
 */
public interface Factory<T> {

	/**
	 * Returns an instance of the type denoted by this {@link Factory} object's
	 * generic parameters (it need not necessarily be a new instance, depending
	 * on the implementation of the method).
	 * 
	 * @return An instance of the given type.
	 */
	T create();

}
