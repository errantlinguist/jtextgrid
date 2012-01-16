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

package com.github.errantlinguist.jtextgrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A {@link Collection}-like object which contains multiple
 * {@link TimeSeriesData} objects.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-07-06
 * 
 * @param <T>
 *            The <code>TimeSeriesDataCollection</code> object subtype.
 * 
 */
public abstract class TimeSeriesDataCollection<E, T extends TimeSeriesDataCollection<?, ?>>
		extends TimeSeriesData<T> {

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
	private static final <T> void extendToIndex(final ArrayList<T> list,
			final int index) {
		list.ensureCapacity(index + 1);
		while (list.size() <= index) {
			list.add(null);
		}
	}

	/**
	 * A {@link NavigableMap} of all elements as keys with their respective IDs as
	 * values.
	 */
	private final NavigableMap<E, Integer> elementIDs;

	/**
	 * A {@link ArrayList} of all elements, where the index is the element ID.
	 */
	private final ArrayList<E> elements;

	/**
	 * The last entry ID automatically assigned to a newly-added element.
	 */
	private int lastAutomaticallyAddedElementID = 0;

	/**
	 * 
	 * @param startTime
	 *            The collection start time.
	 * @param endTime
	 *            The collection end time.
	 */
	public TimeSeriesDataCollection(final double startTime, final double endTime) {
		super(startTime, endTime);
		this.elements = new ArrayList<E>();
		this.elementIDs = new TreeMap<E, Integer>();
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
	public TimeSeriesDataCollection(final double startTime,
			final double endTime, final int size) {
		super(startTime, endTime);
		this.elements = new ArrayList<E>(size);
		this.elementIDs = new TreeMap<E, Integer>();
	}

	/**
	 * Adds a new element.
	 * 
	 * @param element
	 *            The element to add.
	 * @return The ID of the newly-added element.
	 */
	protected int add(final E element) {
		final int id = getNextFreeElementID();
		add(id, element);
		lastAutomaticallyAddedElementID = id;
		return id;

	}

	/**
	 * Adds an element into the element {@link List}, replacing any element
	 * assigned to the given ID.
	 * 
	 * @param id
	 *            The ID to assign the element object to.
	 * @param element
	 *            The element to add.
	 */
	protected void add(final int id, final E element) {
		// If the size of the list is equal to or less than the given
		// entry ID
		// (e.g. index), it cannot already exist
		if (elements.size() <= id) {
			addNewElementExtend(id, element);

		} else {
			final E oldElement = elements.get(id);
			// If the element reference is null, it has not been added yet even
			// though elements with greater IDs already have been
			if (oldElement == null) {
				addNewElement(id, element);
			} else {
				// An element already exists for the ID if it is not null;
				// replace
				// it
				replaceElement(id, element);
			}
		}

	}

	/**
	 * Adds an element to a previously-unassigned ID.
	 * 
	 * @param id
	 *            The ID to assign the newly-added element to.
	 * @param element
	 *            The element to add.
	 */
	private void addNewElement(final int id, final E element) {
		elements.set(id, element);
		elementIDs.put(element, id);
	}

	/**
	 * Adds an element to a previously-unassigned ID, extending
	 * {@link #elements} if necessary.
	 * 
	 * @param id
	 *            The ID to assign the newly-added element to.
	 * @param element
	 *            The element to add.
	 */
	private void addNewElementExtend(final int id, final E element) {
		extendToIndex(elements, id);
		elements.set(id, element);
		elementIDs.put(element, id);
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
		if (!(obj instanceof TimeSeriesDataCollection)) {
			return false;
		}
		final TimeSeriesDataCollection<?, ?> other = (TimeSeriesDataCollection<?, ?>) obj;
		if (elements == null) {
			if (other.elements != null) {
				return false;
			}
		} else if (!elements.equals(other.elements)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the elementIDs
	 */
	public NavigableMap<E, Integer> getElementIDs() {
		return elementIDs;
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
	 *            The element to get the ID of.
	 * @return The element ID.
	 */
	public Integer getID(final E element) {
		return elementIDs.get(element);
	}

	/**
	 * Gets the next free ID for automatically assigning to a newly-added
	 * element.
	 * 
	 * @return The next free ID.
	 */
	private int getNextFreeElementID() {
		int nextFreeEntryID = lastAutomaticallyAddedElementID + 1;
		while (isAssigned(nextFreeEntryID)) {
			nextFreeEntryID++;
		}

		return nextFreeEntryID;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
//		final int prime = 31;
		int result = super.hashCode();
//		result = prime * result + (elements == null ? 0 : elements.hashCode());
		return result;
	}

	/**
	 * Checks if a given integer has already been used as an ID for an element.
	 * 
	 * @param id
	 *            The ID to check.
	 * @return <code>true</code> iff there is an element with the given ID.
	 */
	private boolean isAssigned(final int id) {
		final boolean isAssigned;
		// If the size of the list is equal to or less than the given
		// entry ID
		// (e.g. index), it cannot exist
		if (elements.size() <= id) {
			isAssigned = false;
		} else {
			final E element = elements.get(id);
			// If the element reference is null, it has not been added yet even
			// though elements with greater IDs already have been
			if (element == null) {
				isAssigned = false;
			} else {
				isAssigned = true;
			}
		}

		return isAssigned;
	}

	/**
	 * Replaces the element associated with a given ID with a given replacement
	 * element.
	 * 
	 * @param id
	 *            The ID of the element to replace.
	 * @param element
	 *            The element to add as a replacement.
	 */
	private void replaceElement(final int id, final E element) {
		final E oldElement = elements.get(id);
		elementIDs.remove(oldElement);
		elementIDs.put(element, id);
		// Length-checking is not necessary, since the index already existed for
		// certain
		elements.set(id, element);
	}

	/**
	 * 
	 * @return The number of elements.
	 */
	public int size() {
		return elements.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TimeSeriesDataCollection[elements=" + elements + "]";
	}

}
