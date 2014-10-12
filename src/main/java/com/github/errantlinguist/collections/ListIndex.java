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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A utility class for manipulating {@link List} indices.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * @version 2013-10-22
 * @since 2013-10-22
 * 
 */
public final class ListIndex {

	public static final <E> List<E> createListFromIndexMap(
			final Map<? extends Integer, ? extends E> elementIndices) {
		final List<E> result = new ArrayList<E>(elementIndices.size());

		setIndexedElements(result, elementIndices);

		return result;
	}

	// public static final <E> MultiValueMap<E, Integer, NavigableSet<Integer>>
	// createListIndexMap(
	// final List<E> list) {
	// final Map<E, NavigableSet<Integer>> decoratedMap = new HashMap<E,
	// NavigableSet<Integer>>(
	// list.size());
	// final TreeSetFactory<Integer> factory = TreeSetFactory.getInstance();
	// final MultiValueMap<E, Integer, NavigableSet<Integer>> result = new
	// MultiValueMap<E, Integer, NavigableSet<Integer>>(
	// decoratedMap, factory);
	//
	// for (final ListIterator<E> listIter = list.listIterator(); listIter
	// .hasNext();) {
	// final int nextIndex = listIter.nextIndex();
	// final E nextElement = listIter.next();
	// result.putValue(nextElement, nextIndex);
	// }
	//
	// return result;
	// }

	public static final <E> boolean ensureIndex(final ArrayList<E> list,
			final int index) {
		final int size = index + 1;
		list.ensureCapacity(size);
		return CollectionSize.ensureSize(list, size);
	}

	public static final <E> boolean ensureIndex(final List<E> list,
			final int index) {
		return CollectionSize.ensureSize(list, index);
	}

	public static final <E> boolean ensureIndex(final List<E> list,
			final int index, final E defaultElement) {
		return CollectionSize.ensureSize(list, index + 1, defaultElement);
	}

	public static final <E> List<E> removeAllIndices(final List<E> list,
			final Collection<Integer> indices) {
		final List<E> result = new ArrayList<E>(indices.size());

		removeAllIndices(list, indices, result);

		return result;
	}

	public static final <E> List<E> removeAllIndices(final List<E> list,
			final Iterable<Integer> indices) {
		final List<E> result = new LinkedList<E>();

		removeAllIndices(list, indices, result);

		return result;
	}

	public static final <E> void removeAllIndices(final List<E> list,
			final Iterable<Integer> indices, final Collection<E> removedElements) {
		for (final Integer index : indices) {
			removedElements.add(list.remove(index.intValue()));
		}
	}

	public static final <E> List<E> removeAllIndices(final List<E> list,
			final List<Integer> indices) {
		Collections.sort(indices, Collections.reverseOrder());
		final Collection<Integer> upcastIndices = indices;
		return removeAllIndices(list, upcastIndices);
	}

	public static final <E> List<E> removeAllIndices(final List<E> list,
			SortedSet<Integer> indices) {
		final Comparator<Integer> reverseComparator = Collections
				.reverseOrder();
		// If the set is not definitely already in reverse natural order, create
		// a new one
		// in that order to be sure
		if (!reverseComparator.equals(indices.comparator())) {
			final SortedSet<Integer> reverseSortedIndices = new TreeSet<Integer>(
					reverseComparator);
			reverseSortedIndices.addAll(indices);
			indices = reverseSortedIndices;
		}

		final Collection<Integer> upcastIndices = indices;
		return removeAllIndices(list, upcastIndices);
	}

	public static final <E> void setIndexedElements(
			final List<E> list,
			final Iterable<? extends Entry<? extends Integer, ? extends E>> elementIndices) {
		for (final Entry<? extends Integer, ? extends E> elementIndex : elementIndices) {
			final Integer index = elementIndex.getKey();
			final E element = elementIndex.getValue();
			list.set(index, element);
		}
	}

	public static final <E> void setIndexedElements(final List<E> list,
			final Map<? extends Integer, ? extends E> elementIndices) {

		// Find the maximum index in order to pre-set the list length
		final int maxIndex = Collections.max(elementIndices.keySet());
		ensureIndex(list, maxIndex);

		final Iterable<? extends Entry<? extends Integer, ? extends E>> elementIndexEntries = elementIndices
				.entrySet();
		setIndexedElements(list, elementIndexEntries);
	}

	private ListIndex() {
		// Avoid instantiation
	}

}
