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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import com.github.errantlinguist.jtextgrid.TextGridTier.TextGridTierClass;

/**
 * A representation of a <a href="http://www.fon.hum.uva.nl/praat/">Praat</a>
 * TextGrid file and the annotation data contained therein.
 * 
 * @param <T>
 *            The type of the annotation data.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-04-15
 * 
 */
public class TextGridFile<T> extends TimeSeriesDataCollection<TextGridTier<T>,TextGridFile<T>> {

//	/**
//	 * Forcibly increases the size of a given {@link ArrayList} to enable adding
//	 * a new element with a given index by adding null references for each index
//	 * between the end of the <code>ArrayList</code> and the given index.
//	 * 
//	 * @param <T>
//	 *            The type of the elements in the <code>ArrayList</code>.
//	 * @param list
//	 *            The <code>ArrayList</code> to increase the size of
//	 * @param index
//	 *            The index to expand the <code>ArrayList</code> to.
//	 */
//	private static final <T> void extendToIndex(final ArrayList<T> list,
//			final int index) {
//		list.ensureCapacity(index + 1);
//		while (list.size() <= index) {
//			list.add(null);
//		}
//	}

	public static void main(final String[] args) throws IOException, Exception {
		final TextGridFileReader<String> reader = TextGridFileReader
				.getStringParserInstance();
		final TextGridFile<String> tgf = reader
				.readFile("input/Lotse.TextGrid");
		System.out.println(tgf);
	}

	/**
	 * All {@link TextGridEntry} objects in all {@link TextGridTier} objects
	 * representing the TextGrid tiers.
	 */
	protected final NavigableSet<TextGridEntry<T>> entries;

//	private final Map<Integer, TextGridTier<T>> tierIDs;

//	private final ArrayList<TextGridTier<T>> tiers;

	private final Map<String, TextGridTier<T>> tierNames;

	/**
	 * The last entry ID automatically assigned to a newly-added
	 * {@link TextGridTier}.
	 */
//	private int lastAutomaticallyAddedTierID = 0;

	/**
	 * 
	 * @param startTime
	 *            The start time of the file.
	 * @param endTime
	 *            The end time of the file.
	 */
	public TextGridFile(final double startTime, final double endTime) {
		super(startTime, endTime);

//		this.tiers = new ArrayList<TextGridTier<T>>();
		this.entries = new TreeSet<TextGridEntry<T>>();
		this.tierNames = new HashMap<String, TextGridTier<T>>();
//		this.tierIDs = new HashMap<Integer, TextGridTier<T>>();
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

//		this.tiers = new ArrayList<TextGridTier<T>>(size);
		this.entries = new TreeSet<TextGridEntry<T>>();
		this.tierNames = new HashMap<String, TextGridTier<T>>(size);
//		this.tierIDs = new HashMap<Integer, TextGridTier<T>>(size);
	}

	/**
	 * Adds the {@link TextGridEntry} objects in a given {@link Collection}
	 * thereof to the set of all entries.
	 * 
	 * @param entries
	 *            The <code>Collection</code> of <code>TextGridEntry</code>
	 *            objects to add.
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

//	/**
//	 * Adds a {@link TextGridTier} object to a previously-unassigned ID.
//	 * 
//	 * @param tier
//	 *            The <code>TextGridTier</code> object to add.
//	 * @param id
//	 *            The ID to assign the newly-added <code>TextGridTier</code>
//	 *            object to.
//	 */
//	private void addNewTier(final TextGridTier<T> tier, final int id) {
//		tiers.set(id, tier);
//		tierIDs.put(tier, id);
//	}
//
//	/**
//	 * Adds a {@link TextGridTier} object to a previously-unassigned ID,
//	 * extending {@link #entries} if necessary.
//	 * 
//	 * @param tier
//	 *            The <code>TextGridTier</code> object to add.
//	 * @param id
//	 *            The ID to assign the newly-added <code>TextGridTier</code>
//	 *            object to.
//	 */
//	private void addNewTierExtend(final TextGridTier<T> tier, final int id) {
//		extendToIndex(tiers, id);
//		tiers.set(id, tier);
//		tierIDs.put(tier, id);
//	}

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
	public final TextGridTier<T> addTier(final int id,
			final TextGridTierClass tierClass, final String name,
			final double startTime, final double endTime) {
		final TextGridTier<T> newTier = new TextGridTier<T>(this, tierClass,
				id, name, startTime, endTime);
		add(id,newTier);
		return newTier;

	}

//	/**
//	 * Adds a {@link TextGridTier} object and the {@link TextGridEntry} objects
//	 * it contains.
//	 * 
//	 * @param tier
//	 *            The <code>TextGridTier</code> object to add.
//	 * @return <code>true</code> iff the <code>TextGridTier</code> was
//	 *         successfully added to the set of all tiers.
//	 */
//	public final boolean addTier(final TextGridTier<T> tier) {
//		final boolean wasAdded = tiers.add(tier);
//		if (wasAdded) {
//			putTier(tier.id, tier, tierIDs);
//			putTier(tier.name, tier, tierNames);
//
//			addEntries(tier.getElementIDs().keySet());
//		}
//
//		return wasAdded;
//	}

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
				id, name, startTime, endTime,size);
		add(id,newTier);
		return newTier;
	}

//	/**
//	 * Constructs and adds a new {@link TextGridTier} object.
//	 * 
//	 * @param startTime
//	 *            The tier start time.
//	 * @param endTime
//	 *            The tier end time.
//	 * @return The newly-constructed and (successfully) -added
//	 *         <code>TextGridTier</code> object.
//	 */
//	public TextGridTier<T> addTier(final TextGridTierClass tierClass,
//			final String name, final double startTime, final double endTime) {
//		final int newTierID = getNextFreeTierID();
//		final TextGridTier<T> newTier = addTier(newTierID, tierClass, name,
//				startTime, endTime);
//		lastAutomaticallyAddedTierID = newTierID;
//		return newTier;
//
//	}

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

			if (size() < arg0.size()) {
				comp = -1;
			} else if (size() > arg0.size()) {
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

	/**
	 * @return the entries
	 */
	public NavigableSet<TextGridEntry<T>> getEntries() {
		return entries;
	}

//	/**
//	 * Gets the next free ID for automatically assigning to a newly-added
//	 * {@link TextGridTier}.
//	 * 
//	 * @return The next free ID.
//	 */
//	private int getNextFreeTierID() {
//		int nextFreeEntryID = lastAutomaticallyAddedTierID + 1;
//		while (isAssigned(nextFreeEntryID)) {
//			nextFreeEntryID++;
//		}
//
//		return nextFreeEntryID;
//
//	}

//	/**
//	 * Gets a {@link TextGridTier} object by its ID.
//	 * 
//	 * @param id
//	 *            The ID of the tier.
//	 * @return The <code>TextGridTier</code> object associated with the given
//	 *         ID.
//	 */
//	public final TextGridTier<T> getTier(final int id) {
//		return tierIDs.get(id);
//	}

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
	 * @return An ordered {@link List} of {@link NavigableSet} objects of the
	 *         {@link TextGridEntry} objects associated with each
	 *         {@link TextGridTier} object associated with the
	 *         <code>TextGridFile</code>.
	 */
	public final List<NavigableSet<TextGridEntry<T>>> getTierEntryLists() {
		final List<NavigableSet<TextGridEntry<T>>> newList = new ArrayList<NavigableSet<TextGridEntry<T>>>(
				getElements().size());
		for (final TextGridTier<T> tier : getElements()) {
			newList.add(tier.getElementIDs().navigableKeySet());
		}

		return newList;
	}

	/**
//	 * @return the tiers
//	 */
//	public ArrayList<TextGridTier<T>> getTiers() {
//		return tiers;
//	}

//	/**
//	 * Checks if a given integer has already been used as an ID for a
//	 * {@link TextGridTier} object.
//	 * 
//	 * @param id
//	 *            The ID to check.
//	 * @return <code>true</code> iff there is a tier with the given ID.
//	 */
//	private boolean isAssigned(final int id) {
//		final boolean isAssigned;
//		// If the size of the tier list is equal to or less than the given
//		// entry ID
//		// (e.g. index), it cannot exist
//		if (tiers.size() <= id) {
//			isAssigned = false;
//		} else {
//			final TextGridTier<T> tier = tiers.get(id);
//			// If the tier reference is null, it has not been added yet even
//			// though tiers with greater IDs already have been
//			if (tier == null) {
//				isAssigned = false;
//			} else {
//				isAssigned = true;
//			}
//		}
//
//		return isAssigned;
//	}

//	/**
//	 * Puts a {@link TextGridTier} object into a specified {@link Map},
//	 * replacing a previous one and updating the set of all
//	 * <code>TextGridTier</code> objects to match if necessary.
//	 * 
//	 * @param <K>
//	 *            The <code>Map</code> key type.
//	 * @param key
//	 *            The key to map the given <code>TextGridTier</code> object to.
//	 * @param tier
//	 *            The <code>TextGridTier</code> object to add to the
//	 *            <code>Map</code>.
//	 * @param map
//	 *            The <code>Map</code> to add the <code>TextGridTier</code>
//	 *            object to.
//	 */
//	private final <K> void putTier(final K key, final TextGridTier<T> tier,
//			final Map<K, TextGridTier<T>> map) {
//		final TextGridTier<T> oldTier = map.put(key, tier);
//		if (oldTier != null) {
//			tiers.remove(oldTier);
//		}
//	}

	/**
	 * Removes the {@link TextGridEntry} objects in a given {@link Collection}
	 * thereof from the set of all entries.
	 * 
	 * @param entries
	 *            The <code>Collection</code> of <code>TextGridEntry</code>
	 *            objects to remove.
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

//	/**
//	 * Removes a {@link TextGridTier} object and the {@link TextGridEntry}
//	 * objects it contains.
//	 * 
//	 * @param id
//	 *            The tier ID.
//	 * @return The removed <code>TextGridTier</code> object.
//	 */
//	public final TextGridTier<T> removeTier(final int id) {
//		final TextGridTier<T> removee = tierIDs.remove(id);
//		if (removee != null) {
//			tiers.remove(removee);
//			tierNames.remove(removee.name);
//
//			removeEntries(removee.getElementIDs().keySet());
//		}
//		return removee;
//	}

//	/**
//	 * Removes a {@link TextGridTier} object and the {@link TextGridEntry}
//	 * objects it contains.
//	 * 
//	 * @param name
//	 *            The tier name.
//	 * @return The removed <code>TextGridTier</code> object.
//	 */
//	public final TextGridTier<T> removeTier(final String name) {
//		final TextGridTier<T> removee = tierNames.remove(name);
//		if (removee != null) {
//			tiers.remove(removee);
//			tierIDs.remove(removee.id);
//
//			removeEntries(removee.getElementIDs().keySet());
//		}
//		return removee;
//	}

//	/**
//	 * Removes a {@link TextGridTier} object and the {@link TextGridEntry}
//	 * objects it contains.
//	 * 
//	 * @param tier
//	 *            The <code>TextGridTier</code> object to remove.
//	 * @return <code>true</code> iff the <code>TextGridTier</code> was
//	 *         successfully removed from the set of all tiers.
//	 */
//	public final boolean removeTier(final TextGridTier<T> tier) {
//		final boolean wasRemoved = tiers.remove(tier);
//		if (wasRemoved) {
//			tierIDs.remove(tier.id);
//			tierNames.remove(tier.name);
//
//			removeEntries(tier.getElementIDs().keySet());
//		}
//
//		return wasRemoved;
//	}

//	/**
//	 * Replaces the {@link TextGridTier} object associated with a given ID with
//	 * a given replacement tier.
//	 * 
//	 * @param id
//	 *            The ID of the <code>TextGridTier</code> object to replace.
//	 * @param tier
//	 *            The <code>TextGridTier</code> object to add.
//	 */
//	private void replaceTier(final int id, final TextGridTier<T> tier) {
//		final TextGridTier<T> oldTier = tiers.get(id);
//		tierIDs.remove(oldTier);
//		entries.remove(id);
//
//		tierIDs.put(tier, id);
//		// Length-checking is not necessary, since the index already existed for
//		// certain
//		entries.set(id, tier);
//	}

//	/**
//	 * 
//	 * @return The amount of {@link TextGridTier} objects the
//	 *         <code>TextGridFile</code> contains.
//	 */
//	public final int tierCount() {
//		return tiers.size();
//	}

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
		builder.append(", elements=");
		builder.append(getElements());
		builder.append("]");
		return builder.toString();
	}
}
