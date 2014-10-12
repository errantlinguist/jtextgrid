/*
 * 	Copyright 2013--2014 Todd Shore
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
package com.github.errantlinguist.collections;

import java.util.Collection;
import java.util.Collections;

/**
 * A utility class for manipulating the size of {@link Collection} objects.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2013-10-15
 * @since 2013-10-15
 * 
 */
public final class CollectionSize {

	/**
	 * Ensures that a given {@link Collection} has a given size, adding
	 * {@code null} references to it until it has the given size.
	 * 
	 * @param collection
	 *            The {@code Collection} to increase the size of.
	 * @param size
	 *            The target size of the {@code Collection}.
	 * @return {@code true} iff elements were added to the {@code Collection}.
	 */
	public static final <E> boolean ensureSize(final Collection<E> collection,
			final int size) {
		return ensureSize(collection, size, null);
	}

	/**
	 * Ensures that a given {@link Collection} has a given size, adding
	 * references to it until it has the given size.
	 * 
	 * @param collection
	 *            The {@code Collection} to increase the size of.
	 * @param size
	 *            The target size of the {@code Collection}.
	 * @param defaultElement
	 *            The element reference to add until the {@code Collection} is
	 *            of the target size.
	 * @return {@code true} iff elements were added to the {@code Collection}.
	 */
	public static final <E> boolean ensureSize(final Collection<E> collection,
			final int size, final E defaultElement) {
		final boolean result;

		final int sizeDifference = size - collection.size();
		if (sizeDifference > 0) {
			final Collection<E> elementsToAdd = Collections.nCopies(
					sizeDifference, defaultElement);
			result = collection.addAll(elementsToAdd);
		} else {
			result = false;
		}

		return result;
	}

	private CollectionSize() {
		// Avoid instantiation
	}

}
