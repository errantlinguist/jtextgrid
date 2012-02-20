/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 */

package com.github.errantlinguist.textgrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A special {@link List} implementation which contains multiple
 * {@link TimeSeriesData} objects.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-07-06
 * 
 * @param <T>
 *            The <code>TimeSeriesDataList</code> object subtype.
 * 
 */
public abstract class TimeSeriesDataList<E, T extends TimeSeriesDataList<?, ?>>
		extends TimeSeriesData<T> implements List<E> {

	/**
	 * Forcibly increases the size of a given {@link ArrayList} to enable adding
	 * a new element with a given index by adding null references for each index
	 * between the end of the <code>ArrayList</code> and the given index.
	 * 
	 * @param <T>
	 *            The type of the elements in the <code>ArrayList</code>.
	 * @param list
	 *            The <code>ArrayList</code> to increase the size of
	 * @param index
	 *            The index to expand the <code>ArrayList</code> to.
	 */
	private static final <T> void ensureValidIndex(final ArrayList<T> list,
			final int index) {
		list.ensureCapacity(index + 1);
		while (list.size() <= index) {
			list.add(null);
		}
	}

	/**
	 * Shifts all the elements of an {@link ArrayList} between a given start and
	 * end index right by a certain amount.&nbsp;NOTE: Doesn't work properly for
	 * shifting to the left; That should be done by iterating forwards over the
	 * given <code>List</code>, while this method iterates backwards.
	 * 
	 * @param list
	 *            The given <code>ArrayList</code> to shift the elements
	 *            thereof.
	 * @param startIndex
	 *            The start index.
	 * @param endIndex
	 *            The end index.
	 * @param shiftValue
	 *            The amount to shift each element by, such that (newIndex =
	 *            oldIndex + {@code shiftValue}).
	 */
	protected static <LE> void shiftRight(final ArrayList<LE> list,
			final int startIndex, final int endIndex, final int shiftValue) {
		// if(startIndex == endIndex){
		// return false;
		// }
		int currentNewIndex = endIndex + shiftValue;
		ensureValidIndex(list, currentNewIndex);

		for (int currentOldIndex = endIndex - 1; currentOldIndex >= startIndex; currentOldIndex--, currentNewIndex--) {
			final LE shiftee = list.get(currentOldIndex);
			list.set(currentNewIndex, shiftee);
		}

		// return true;

	}

	/**
	 * A {@link NavigableMap} of all elements as keys with their respective
	 * indices as values.
	 */
	private final NavigableMap<E, Integer> elementIndices;

	/**
	 * A {@link ArrayList} of all elements, where the index is the element
	 * index.
	 */
	private final ArrayList<E> elements;

	/**
	 * The last entry index automatically assigned to a newly-added element.
	 */
	private int lastAutomaticallyAddedIndex = -1;

	/**
	 * 
	 * @param startTime
	 *            The collection start time.
	 * @param endTime
	 *            The collection end time.
	 */
	public TimeSeriesDataList(final double startTime, final double endTime) {
		super(startTime, endTime);
		this.elements = new ArrayList<E>();
		this.elementIndices = new TreeMap<E, Integer>();
	}

	/**
	 * 
	 * @param startTime
	 *            The collection start time.
	 * @param endTime
	 *            The collection end time.
	 * @param size
	 *            The expected size of the collection.
	 */
	public TimeSeriesDataList(final double startTime, final double endTime,
			final int size) {
		super(startTime, endTime);
		this.elements = new ArrayList<E>(size);
		this.elementIndices = new TreeMap<E, Integer>();
	}

	/**
	 * Adds a new element.
	 * 
	 * @param element
	 *            The element to add.
	 * @return The index of the newly-added element.
	 */
	@Override
	public boolean add(final E element) {
		final int index = getNextFreeIndex();
		add(index, element);
		setLastAutomaticallyAddedIndex(index);
		return true;

	}

	/**
	 * Adds an element into the element {@link List}, replacing any element
	 * assigned to the given index.
	 * 
	 * @param index
	 *            The index to assign the element object to.
	 * @param element
	 *            The element to add.
	 */
	@Override
	public void add(final int index, final E element) {
		// If the size of the list is equal to or less than the given
		// entry index
		// (e.g. index), it cannot already exist
		if (elements.size() <= index) {
			addNewElementExtend(index, element);

		} else {
			final E oldElement = elements.get(index);
			// If the element reference is null, it has not been added yet even
			// though elements with greater indices already have been
			if (oldElement == null) {
				addNewElement(index, element);
			} else {
				// An element already exists for the index if it is not null;
				// replace
				// it
				set(index, element);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(final Collection<? extends E> arg0) {
		boolean wasChanged = false;
		for (final E element : arg0) {
			if (add(element)) {
				wasChanged = true;
			}

		}
		return wasChanged;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int arg0, final Collection<? extends E> arg1) {
		if (elements.size() == arg0) {
			return addAll(arg1);
		} else {
			shiftRight(elements, arg0, elements.size(), arg1.size());

			for (final E element : arg1) {
				add(arg0, element);
				// Increment the index for the next element to be added
				arg0++;
			}
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#clear()
	 */
	@Override
	public void clear() {
		elements.clear();
		elementIndices.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(final Object arg0) {
		return elementIndices.containsKey(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(final Collection<?> arg0) {
		for (final Object element : arg0) {
			if (!elementIndices.containsKey(element)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof TimeSeriesDataList)) {
			return false;
		}
		final TimeSeriesDataList<?, ?> other = (TimeSeriesDataList<?, ?>) obj;
		if (elements == null) {
			if (other.elements != null) {
				return false;
			}
		} else if (!elements.equals(other.elements)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#get(int)
	 */
	@Override
	public E get(final int arg0) {
		return elements.get(arg0);
	}

	/**
	 * @return the elementIndices
	 */
	public NavigableMap<E, Integer> getElementIndices() {
		return elementIndices;
	}

	/**
	 * @return the elements
	 */
	public ArrayList<E> getElements() {
		return elements;
	}

	/**
	 * 
	 * @param element
	 *            The element to get the index of.
	 * @return The element index.
	 */
	public Integer getIndex(final E element) {
		return elementIndices.get(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// final int prime = 31;
		final int result = super.hashCode();
		// result = prime * result + (elements == null ? 0 :
		// elements.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(final Object arg0) {
		return elementIndices.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(final Object arg0) {
		return elements.lastIndexOf(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<E> listIterator() {
		return elements.listIterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<E> listIterator(final int arg0) {
		return elements.listIterator(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#remove(int)
	 */
	@Override
	public E remove(final int arg0) {
		final E removee = elements.remove(arg0);
		elementIndices.remove(removee);
		return removee;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object arg0) {
		if (elementIndices.containsKey(arg0)) {
			final Integer index = elementIndices.get(arg0);
			remove(index);
			return true;
		} else {
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(final Collection<?> arg0) {
		boolean wasChanged = false;
		for (final Object element : arg0) {
			if (remove(element)) {
				wasChanged = true;
			}
		}
		return wasChanged;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(final Collection<?> arg0) {
		boolean wasChanged = false;
		for (final Object element : elements) {
			if (!arg0.contains(element)) {
				remove(element);
				wasChanged = true;
			}
		}
		return wasChanged;
	}

	/**
	 * Replaces the element associated with a given index with a given
	 * replacement element.
	 * 
	 * @param index
	 *            The index of the element to replace.
	 * @param element
	 *            The element to add as a replacement.
	 */
	@Override
	public E set(final int index, final E element) {
		final E oldElement = elements.get(index);
		elementIndices.remove(oldElement);
		elementIndices.put(element, index);
		// Length-checking is not necessary, since the index already existed for
		// certain
		final E replacee = elements.set(index, element);
		return replacee;
	}

	/**
	 * 
	 * @return The number of elements.
	 */
	@Override
	public int size() {
		return elements.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public List<E> subList(final int arg0, final int arg1) {
		return elements.subList(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#toArray(T[])
	 */
	@Override
	public <AE> AE[] toArray(final AE[] arg0) {
		return elements.toArray(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName());
		builder.append("[elements=");
		builder.append(elements);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Adds an element to a previously-unassigned index.
	 * 
	 * @param index
	 *            The index to assign the newly-added element to.
	 * @param element
	 *            The element to add.
	 */
	private void addNewElement(final int index, final E element) {
		elements.set(index, element);
		elementIndices.put(element, index);
	}

	/**
	 * Adds an element to a previously-unassigned index, extending
	 * {@link #elements} if necessary.
	 * 
	 * @param index
	 *            The index to assign the newly-added element to.
	 * @param element
	 *            The element to add.
	 */
	private void addNewElementExtend(final int index, final E element) {
		ensureValidIndex(elements, index);
		elements.set(index, element);
		elementIndices.put(element, index);
	}

	/**
	 * Checks if a given integer has already been used as an index for an
	 * element.
	 * 
	 * @param index
	 *            The index to check.
	 * @return <code>true</code> iff there is an element with the given index.
	 */
	private boolean isAssigned(final int index) {
		final boolean isAssigned;
		// If the size of the list is equal to or less than the given
		// entry index
		// (e.g. index), it cannot exist
		if (elements.size() <= index) {
			isAssigned = false;
		} else {
			final E element = elements.get(index);
			// If the element reference is null, it has not been added yet even
			// though elements with greater indices already have been
			if (element == null) {
				isAssigned = false;
			} else {
				isAssigned = true;
			}
		}

		return isAssigned;
	}

	/**
	 * @return the lastAutomaticallyAddedIndex
	 */
	protected int getLastAutomaticallyAddedIndex() {
		return lastAutomaticallyAddedIndex;
	}

	/**
	 * Gets the next free index for automatically assigning to a newly-added
	 * element.
	 * 
	 * @return The next free index.
	 */
	protected int getNextFreeIndex() {
		int nextFreeIndex = getLastAutomaticallyAddedIndex() + 1;
		while (isAssigned(nextFreeIndex)) {
			nextFreeIndex++;
		}

		return nextFreeIndex;

	}

	/**
	 * @param lastAutomaticallyAddedIndex
	 *            the lastAutomaticallyAddedIndex to set
	 */
	protected void setLastAutomaticallyAddedIndex(
			final int lastAutomaticallyAddedIndex) {
		this.lastAutomaticallyAddedIndex = lastAutomaticallyAddedIndex;
	}

}
