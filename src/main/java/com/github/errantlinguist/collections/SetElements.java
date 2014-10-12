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
package com.github.errantlinguist.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A utility class for processing {@link Set} elements.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2014-02-07
 * @since 2014-02-07
 * 
 */
public final class SetElements {

	/**
	 * Returns a new {@link Set} representing the set-theoretic difference
	 * (a.k.a.&nbsp;relative complement) of a given {@link Collection} in
	 * another given {@code Collection}.
	 * 
	 * @param minuend
	 *            The {@code Collection} to get the relative complement from.
	 * @param subtrahend
	 *            The {@code Collection} to get the relative complement of.
	 * @return A new {@code Set} of the relative complement.
	 */
	public static final <E> Set<E> difference(final Collection<E> minuend,
			final Collection<?> subtrahend) {
		final Set<E> result = new HashSet<E>(minuend);
		result.removeAll(subtrahend);
		return result;
	}

	/**
	 * Returns a new {@link Set} representing the intersection of two given
	 * {@link Collection collections}.
	 * 
	 * @param set1
	 *            The first {@code Collection}.
	 * @param set2
	 *            The second {@code Collection}.
	 * @return A new {@code Set} of the relative complement.
	 */
	public static final <E> Set<E> intersection(final Collection<E> set1,
			final Collection<?> set2) {
		final Set<E> result = new HashSet<E>(set1);
		result.retainAll(set2);
		return result;
	}

	private SetElements() {
	}

}
