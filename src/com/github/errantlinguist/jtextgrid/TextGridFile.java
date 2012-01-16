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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import com.github.errantlinguist.jtextgrid.TextGridTier.TextGridTierClass;

/**
 * A representation of a <a href="http://www.fon.hum.uva.nl/praat/">Praat</a> TextGrid
 * file and the annotation data contained therein.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-04-15
 * 
 */
public class TextGridFile<T> extends TimeSeriesData<TextGridFile<T>> {

	/**
	 * All {@link TextGridEntry} objects in all {@link TextGridTier} objects
	 * representing the TextGrid tiers.
	 */
	protected final NavigableSet<TextGridEntry<T>> entries;

	private final Map<Integer, TextGridTier<T>> tierIDs;

	private final Map<String, TextGridTier<T>> tierNames;

	/**
	 * All {@link TextGridTier} objects.
	 */
	protected final NavigableSet<TextGridTier<T>> tiers;

	/**
	 * 
	 * @param startTime
	 *            The start time of the file.
	 * @param endTime
	 *            The end time of the file.
	 */
	public TextGridFile(final double startTime, final double endTime) {
		super(startTime, endTime);

		this.tiers = new TreeSet<TextGridTier<T>>();
		this.entries = new TreeSet<TextGridEntry<T>>();
		this.tierNames = new HashMap<String, TextGridTier<T>>();
		this.tierIDs = new HashMap<Integer, TextGridTier<T>>();
	}

	/**
	 * 
	 * @param startTime
	 *            The start time of the file.
	 * @param endTime
	 *            The end time of the file.
	 * @param size
	 *            The size of the file measured by the number of tiers it has.
	 */
	public TextGridFile(final double startTime, final double endTime,
			final int size) {
		super(startTime, endTime);

		this.tiers = new TreeSet<TextGridTier<T>>();
		this.entries = new TreeSet<TextGridEntry<T>>();
		this.tierNames = new HashMap<String, TextGridTier<T>>(size);
		this.tierIDs = new HashMap<Integer, TextGridTier<T>>(size);
	}

	/**
	 * Adds a collection of {@link TextGridEntry} objects to the set of all
	 * entries.
	 * 
	 * @param entries
	 *            The collection of <code>TextGridEntry</code> objects to add.
	 * @return <code>true</code> iff the set of <code>TextGridEntry</code>
	 *         objects was changed, i.e.&nbsp;if at least one
	 *         <code>TextGridEntry</code> was added.
	 */
	protected final boolean addEntries(
			final Collection<TextGridEntry<T>> entries) {

		boolean wasChanged = false;

		for (final TextGridEntry<T> entry : entries) {
			final boolean wasAdded = this.entries.add(entry);
			if (wasAdded) {
				wasChanged = true;
			}

		}

		return wasChanged;
	}

	/**
	 * Adds an {@link TextGridEntry} object to the set of all entries.
	 * 
	 * @param entry
	 *            The <code>TextGridEntry</code> object to add.
	 * @return <code>true</code> iff the <code>TextGridEntry</code> was
	 *         successfully added.
	 */
	protected final boolean addEntry(final TextGridEntry<T> entry) {
		return entries.add(entry);

	}

	/**
	 * Adds a {@link TextGridTier} object and the {@link TextGridEntry} objects
	 * it contains.
	 * 
	 * @param tier
	 *            The <code>TextGridTier</code> object to add.
	 * @return <code>true</code> iff the <code>TextGridTier</code> was
	 *         successfully added to the set of all tiers.
	 */
	public final boolean addTier(final TextGridTier<T> tier) {
		final boolean wasAdded = tiers.add(tier);
		if (wasAdded) {
			putTier(tier.id, tier, tierIDs);
			putTier(tier.name, tier, tierNames);

			addEntries(tier.entries);
		}

		return wasAdded;
	}

	/**
	 * Constructs and adds a new {@link TextGridTier} object.
	 * 
	 * @param tierClass
	 *            The class of the tier.
	 * @param id
	 *            The tier ID.
	 * @param name
	 *            The tier name.
	 * @param startTime
	 *            The tier start time.
	 * @param endTime
	 *            The tier end time.
	 * @return The newly-constructed and (successfully) -added
	 *         <code>TextGridTier</code> object.
	 */
	public final TextGridTier<T> addTier(final TextGridTierClass tierClass,
			final int id, final String name, final double startTime,
			final double endTime) {
		final TextGridTier<T> newTier = new TextGridTier<T>(this, tierClass,
				id, name, startTime, endTime);

		if (tiers.add(newTier)) {
			putTier(id, newTier, tierIDs);
			putTier(name, newTier, tierNames);

			return newTier;

		} else {
			return null;
		}

	}

	/**
	 * Constructs and adds a new {@link TextGridTier} object.
	 * 
	 * @param tierClass
	 *            The class of the tier.
	 * @param id
	 *            The tier ID.
	 * @param name
	 *            The tier name.
	 * @param startTime
	 *            The tier start time.
	 * @param endTime
	 *            The tier end time.
	 * @param size
	 *            The size of the tier measured by the number of
	 *            {@link TextGridEntry} objects it has.
	 * @return The newly-constructed and (successfully) -added
	 *         <code>TextGridTier</code> object.
	 */
	public final TextGridTier<T> addTier(final TextGridTierClass tierClass,
			final int id, final String name, final double startTime,
			final double endTime, final int size) {
		final TextGridTier<T> newTier = new TextGridTier<T>(this, tierClass,
				id, name, startTime, endTime, size);

		if (tiers.add(newTier)) {
			putTier(id, newTier, tierIDs);
			putTier(name, newTier, tierNames);

			return newTier;

		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see TimeSeriesData#deepCompareTo(TimeSeriesData)
	 */
	@Override
	protected int deepCompareTo(final TextGridFile<T> arg0) {

		int comp = 0;

		if (entryCount() < arg0.entryCount()) {
			comp = -1;
		} else if (entryCount() > arg0.entryCount()) {
			comp = 1;
		} else {

			if (tierCount() < arg0.tierCount()) {
				comp = -1;
			} else if (tierCount() > arg0.tierCount()) {
				comp = 1;
			}

		}
		return comp;
	}

	/**
	 * 
	 * @return The amount of {@link TextGridEntry} objects associated with the
	 *         <code>TextGridFile</code>.
	 */
	public final int entryCount() {
		return entries.size();
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
		if (!(obj instanceof TextGridFile<?>)) {
			return false;
		}
		final TextGridFile<?> other = (TextGridFile<?>) obj;
		if (entries == null) {
			if (other.entries != null) {
				return false;
			}
		} else if (!entries.equals(other.entries)) {
			return false;
		}
		if (tiers == null) {
			if (other.tiers != null) {
				return false;
			}
		} else if (!tiers.equals(other.tiers)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the entries
	 */
	public NavigableSet<TextGridEntry<T>> getEntries() {
		return entries;
	}

	/**
	 * Gets a {@link TextGridTier} object by its ID.
	 * 
	 * @param id
	 *            The ID of the tier.
	 * @return The <code>TextGridTier</code> object associated with the given
	 *         ID.
	 */
	public final TextGridTier<T> getTier(final int id) {
		return tierIDs.get(id);
	}

	/**
	 * Gets a {@link TextGridTier} object by its name.
	 * 
	 * @param name
	 *            name of the tier.
	 * @return The <code>TextGridTier</code> object associated with the given
	 *         name.
	 */
	public final TextGridTier<T> getTier(final String name) {
		return tierNames.get(name);
	}

	/**
	 * 
	 * @return An ordered {@link List} of <code>List</code>s of the
	 *         {@link TextGridEntry} objects associated with each
	 *         {@link TextGridTier} object associated with the
	 *         <code>TextGridFile</code>.
	 */
	public final List<List<TextGridEntry<T>>> getTierEntryLists() {
		final List<List<TextGridEntry<T>>> newList = new ArrayList<List<TextGridEntry<T>>>(
				tiers.size());
		for (final TextGridTier<T> tier : tiers) {
			newList.add(tier.getEntryList());
		}

		return newList;
	}

	/**
	 * @return the tiers
	 */
	public NavigableSet<TextGridTier<T>> getTiers() {
		return tiers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (entries == null ? 0 : entries.hashCode());
		result = prime * result + (tiers == null ? 0 : tiers.hashCode());
		return result;
	}

	/**
	 * Puts a {@link TextGridTier} object into a specified {@link Map},
	 * replacing a previous one and updating the set of all
	 * <code>TextGridTier</code> objects to match if necessary.
	 * 
	 * @param <K>
	 *            The <code>Map</code> key type.
	 * @param key
	 *            The key to map the given <code>TextGridTier</code> object to.
	 * @param tier
	 *            The <code>TextGridTier</code> object to add to the
	 *            <code>Map</code>.
	 * @param map
	 *            The <code>Map</code> to add the <code>TextGridTier</code>
	 *            object to.
	 */
	private final <K> void putTier(final K key, final TextGridTier<T> tier,
			final Map<K, TextGridTier<T>> map) {
		final TextGridTier<T> oldTier = map.put(key, tier);
		if (oldTier != null) {
			tiers.remove(oldTier);
		}
	}

	/**
	 * Removes a collection of {@link TextGridEntry} objects from the set of all
	 * entries.
	 * 
	 * @param entries
	 *            The collection of <code>TextGridEntry</code> objects to
	 *            remove.
	 * @return <code>true</code> iff the set of <code>TextGridEntry</code>
	 *         objects was changed, i.e.&nbsp;if at least one
	 *         <code>TextGridEntry</code> was removed.
	 */
	protected final boolean removeEntries(
			final Collection<TextGridEntry<T>> entries) {

		boolean wasChanged = false;

		for (final TextGridEntry<T> entry : entries) {
			final boolean wasRemoved = this.entries.remove(entry);
			if (wasRemoved) {
				wasChanged = true;
			}

		}

		return wasChanged;
	}

	/**
	 * Removes an {@link TextGridEntry} object from the set of all entries.
	 * 
	 * @param entry
	 *            The <code>TextGridEntry</code> object to remove.
	 * @return <code>true</code> iff the <code>TextGridEntry</code> was in the
	 *         set of <code>TextGridEntry</code> objects and was successfully
	 *         removed.
	 */
	protected final boolean removeEntry(final TextGridEntry<T> entry) {
		return entries.remove(entry);
	}

	/**
	 * Removes a {@link TextGridTier} object and the {@link TextGridEntry}
	 * objects it contains.
	 * 
	 * @param id
	 *            The tier ID.
	 * @return The removed <code>TextGridTier</code> object.
	 */
	public final TextGridTier<T> removeTier(final int id) {
		final TextGridTier<T> removee = tierIDs.remove(id);
		if (removee != null) {
			tiers.remove(removee);
			tierNames.remove(removee.name);

			removeEntries(removee.entries);
		}
		return removee;
	}

	/**
	 * Removes a {@link TextGridTier} object and the {@link TextGridEntry}
	 * objects it contains.
	 * 
	 * @param name
	 *            The tier name.
	 * @return The removed <code>TextGridTier</code> object.
	 */
	public final TextGridTier<T> removeTier(final String name) {
		final TextGridTier<T> removee = tierNames.remove(name);
		if (removee != null) {
			tiers.remove(removee);
			tierIDs.remove(removee.id);

			removeEntries(removee.entries);
		}
		return removee;
	}

	/**
	 * Removes a {@link TextGridTier} object and the {@link TextGridEntry}
	 * objects it contains.
	 * 
	 * @param tier
	 *            The <code>TextGridTier</code> object to remove.
	 * @return <code>true</code> iff the <code>TextGridTier</code> was
	 *         successfully removed from the set of all tiers.
	 */
	public final boolean removeTier(final TextGridTier<T> tier) {
		final boolean wasRemoved = tiers.remove(tier);
		if (wasRemoved) {
			tierIDs.remove(tier.id);
			tierNames.remove(tier.name);

			removeEntries(tier.entries);
		}

		return wasRemoved;
	}

	/**
	 * 
	 * @return The amount of {@link TextGridTier} objects the
	 *         <code>TextGridFile</code> contains.
	 */
	public final int tierCount() {
		return tiers.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TextGridFile[startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", tiers=");
		builder.append(tiers);
		builder.append("]");
		return builder.toString();
	}
}
