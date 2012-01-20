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

import com.github.errantlinguist.jtextgrid.Tier.TierClass;

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
public class TextGridFile<T> extends
		TimeSeriesDataCollection<Tier<T>, TextGridFile<T>> {

	/**
	 * All {@link Entry} objects in all {@link Tier} objects representing the
	 * TextGrid tiers.
	 */
	protected final NavigableSet<Entry<T>> entries;

	// public static void main(final String[] args) throws IOException,
	// Exception {
	// final TextGridFileReader<String> reader = TextGridFileReader
	// .getStringParserInstance();
	// final TextGridFile<String> tgf = reader
	// .readFile("input/Lotse.TextGrid");
	// System.out.println(tgf);
	// System.out.println(tgf.entryCount());
	// System.out.println(tgf.tiersByName.keySet());
	// System.out.println(tgf.tierNames.values());
	// }

	private final Map<Tier<T>, String> tierNames;

	private final Map<String, Tier<T>> tiersByName;

	/**
	 * 
	 * @param startTime
	 *            The start time of the file.
	 * @param endTime
	 *            The end time of the file.
	 */
	public TextGridFile(final double startTime, final double endTime) {
		super(startTime, endTime);

		this.entries = new TreeSet<Entry<T>>();
		this.tiersByName = new HashMap<String, Tier<T>>();
		this.tierNames = new HashMap<Tier<T>, String>();
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

		this.entries = new TreeSet<Entry<T>>();
		this.tiersByName = new HashMap<String, Tier<T>>(size);
		this.tierNames = new HashMap<Tier<T>, String>(size);
	}

	/**
	 * Adds the {@link Entry} objects in a given {@link Collection} thereof to
	 * the set of all entries.
	 * 
	 * @param entries
	 *            The <code>Collection</code> of <code>Entry</code> objects to
	 *            add.
	 * @return <code>true</code> iff the set of <code>Entry</code> objects was
	 *         changed, i.e.&nbsp;if at least one <code>Entry</code> was added.
	 */
	protected final boolean addEntries(final Collection<Entry<T>> entries) {

		boolean wasChanged = false;

		for (final Entry<T> entry : entries) {
			final boolean wasAdded = this.entries.add(entry);
			if (wasAdded) {
				wasChanged = true;
			}

		}

		return wasChanged;
	}

	/**
	 * Adds an {@link Entry} object to the set of all entries.
	 * 
	 * @param entry
	 *            The <code>Entry</code> object to add.
	 * @return <code>true</code> iff the <code>Entry</code> was successfully
	 *         added.
	 */
	protected final boolean addEntry(final Entry<T> entry) {
		return entries.add(entry);

	}

	/**
	 * Constructs and adds a new {@link Tier} object.
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
	 * @return The newly-constructed and (successfully) -added <code>Tier</code>
	 *         object.
	 */
	public final Tier<T> addTier(final int id, final TierClass tierClass,
			final String name, final double startTime, final double endTime) {
		final Tier<T> newTier = new Tier<T>(this, tierClass, startTime, endTime);
		add(id, newTier);
		putTierByName(name, newTier);
		return newTier;

	}

	/**
	 * Constructs and adds a new {@link Tier} object.
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
	 *            The size of the tier measured by the number of {@link Entry}
	 *            objects it has.
	 * @return The newly-constructed and (successfully) -added <code>Tier</code>
	 *         object.
	 */
	public final Tier<T> addTier(final TierClass tierClass, final int id,
			final String name, final double startTime, final double endTime,
			final int size) {
		final Tier<T> newTier = new Tier<T>(this, tierClass, startTime,
				endTime, size);
		add(id, newTier);
		putTierByName(name, newTier);
		return newTier;
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
	 * @return The amount of {@link Entry} objects associated with the
	 *         <code>TextGridFile</code>.
	 */
	public final int entryCount() {
		return entries.size();
	}

	/**
	 * @return the entries
	 */
	public NavigableSet<Entry<T>> getEntries() {
		return entries;
	}

	/**
	 * Gets the string name of a given {@link Tier}.
	 * 
	 * @param tier
	 *            The <code>Tier</code> to get the name of.
	 * @return The name of the <code>Tier</code>.
	 */
	public String getName(final Tier<T> tier) {
		final String name = tierNames.get(tier);
		return name;
	}

	/**
	 * Gets a {@link Tier} object by its name.
	 * 
	 * @param name
	 *            name of the tier.
	 * @return The <code>Tier</code> object associated with the given name.
	 */
	public final Tier<T> getTier(final String name) {
		return tiersByName.get(name);
	}

	/**
	 * 
	 * @return An ordered {@link List} of {@link NavigableSet} objects of the
	 *         {@link Entry} objects associated with each {@link Tier} object
	 *         associated with the <code>TextGridFile</code>.
	 */
	public final List<NavigableSet<Entry<T>>> getTierEntryLists() {
		final List<NavigableSet<Entry<T>>> newList = new ArrayList<NavigableSet<Entry<T>>>(
				getElements().size());
		for (final Tier<T> tier : getElements()) {
			newList.add(tier.getElementIDs().navigableKeySet());
		}

		return newList;
	}

	/**
	 * Puts a {@link Tier} into the string name mapping.
	 * 
	 * @param name
	 *            The name of the <code>Tier</code> to put.
	 * @return The <code>Tier</code> previously mapped to by the name.
	 */
	private Tier<T> putTierByName(final String name, final Tier<T> tier) {

		tierNames.put(tier, name);
		final Tier<T> oldTier = tiersByName.put(name, tier);
		return oldTier;
	}

	/**
	 * Removes the {@link Entry} objects in a given {@link Collection} thereof
	 * from the set of all entries.
	 * 
	 * @param entries
	 *            The <code>Collection</code> of <code>Entry</code> objects to
	 *            remove.
	 * @return <code>true</code> iff the set of <code>Entry</code> objects was
	 *         changed, i.e.&nbsp;if at least one <code>Entry</code> was
	 *         removed.
	 */
	protected final boolean removeEntries(final Collection<Entry<T>> entries) {

		boolean wasChanged = false;

		for (final Entry<T> entry : entries) {
			final boolean wasRemoved = this.entries.remove(entry);
			if (wasRemoved) {
				wasChanged = true;
			}

		}

		return wasChanged;
	}

	/**
	 * Removes an {@link Entry} object from the set of all entries.
	 * 
	 * @param entry
	 *            The <code>Entry</code> object to remove.
	 * @return <code>true</code> iff the <code>Entry</code> was in the set of
	 *         <code>Entry</code> objects and was successfully removed.
	 */
	protected final boolean removeEntry(final Entry<T> entry) {
		return entries.remove(entry);
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
		builder.append(getElements());
		builder.append("]");
		return builder.toString();
	}
}
