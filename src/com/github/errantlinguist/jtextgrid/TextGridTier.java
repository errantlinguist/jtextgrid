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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A tier in a TextGrid file.
 * 
 * @author Todd Shore
 * @version 2012-01-16
 * @since 2011-07-06
 * 
 * @param <T>
 *            The object type representing the data denoted by the tier entries.
 */
public class TextGridTier<T> extends
		TimeSeriesDataCollection<TextGridEntry<T>, TextGridTier<T>> {

	/**
	 * The TextGrid tier class, e.g.&nbsp;"IntervalTier" or "TextTier", as
	 * defined in the TextGrid file itself.
	 * 
	 * @author tshore
	 * @version 2012-01-16
	 * @since 2012-01-16
	 * 
	 */
	public static enum TextGridTierClass {
		/**
		 * A {@link TextGridTier} with {@link TextGridEntry} instances which
		 * denote a time interval, i.e.&nbsp;for which
		 * {@link TimeSeriesData#getStartTime()} <
		 * {@link TimeSeriesData#getEndTime()}.
		 */
		INTERVAL("IntervalTier"),
		/**
		 * A {@link TextGridTier} with {@link TextGridEntry} instances which
		 * denote points, i.e.&nbsp;for which
		 * {@link TextGridEntry#getStartTime()} =
		 * {@link TextGridEntry#getEndTime()}.
		 */
		TEXT("TextTier");

		private static final Map<String, TextGridTierClass> CLASS_NAMES = createStaticClassNameMap();

		private static final Map<String, TextGridTierClass> createStaticClassNameMap() {

			final Map<String, TextGridTierClass> classNames = new HashMap<String, TextGridTierClass>(
					TextGridTierClass.values().length);
			for (final TextGridTierClass textGridTierClass : TextGridTierClass
					.values()) {
				classNames.put(textGridTierClass.getName(), textGridTierClass);
			}
			return Collections.unmodifiableMap(classNames);
		}

		/**
		 * Gets the {@link TextGridTierClass} represented by the given string
		 * name, corresponding to the name as defined in the TextGrid file.
		 * 
		 * @param name
		 *            The class name.
		 * @return The <code>TextGridTierClass</code> associated with the name.
		 */
		public static TextGridTierClass getTierClass(final String name) {
			return CLASS_NAMES.get(name);
		}

		/**
		 * The string name representing the <code>TextGridTierClass</code>, as
		 * defined in the TextGrid file.
		 */
		private final String name;

		/**
		 * 
		 * @param name
		 *            The string name of the tier class, as defined in the
		 *            TextGrid file.
		 */
		private TextGridTierClass(final String name) {
			this.name = name;
		}

		/**
		 * 
		 * @return The string name of the tier class, as defined in the TextGrid
		 *         file.
		 */
		public String getName() {
			return name;
		}

	}

	// /**
	// * Forcibly increases the size of a given {@link ArrayList} to enable
	// adding
	// * a new element with a given index by adding null references for each
	// index
	// * between the end of the <code>ArrayList</code> and the given index.
	// *
	// * @param <T>
	// * The type of the elements in the <code>ArrayList</code>.
	// * @param list
	// * The <code>ArrayList</code> to increase the size of
	// * @param index
	// * The index to expand the <code>ArrayList</code> to.
	// */
	// private static final <T> void extendToIndex(final ArrayList<T> list,
	// final int index) {
	// list.ensureCapacity(index + 1);
	// while (list.size() <= index) {
	// list.add(null);
	// }
	// }

	// /**
	// * A {@link ArrayList} of all {@link TextGridEntry} objects, where the
	// index
	// * is the entry ID.
	// */
	// private final ArrayList<TextGridEntry<T>> entries;

	// /**
	// * All {@link TextGridEntry} objects with their ID as values.
	// */
	// protected final NavigableMap<TextGridEntry<T>, Integer> entryIDs;

	/**
	 * The tier ID.
	 */
	protected final int id;

	// /**
	// * The last entry ID automatically assigned to a newly-added
	// * {@link TextGridEntry}.
	// */
	// private int lastAutomaticallyAddedEntryID = 0;

	/**
	 * The tier name.
	 */
	protected final String name;

	/**
	 * The tier class.
	 */
	protected final TextGridTierClass tierClass;

	/**
	 * @param tgf
	 *            The {@link TextGridFile} object the <code>TextGridTier</code>
	 *            has been added to.
	 * @param tierClass
	 *            The tier class.
	 * @param id
	 *            The tier ID.
	 * @param name
	 *            The tier name.
	 * @param startTime
	 *            The tier start time.
	 * @param endTime
	 *            The tier end time.
	 */
	TextGridTier(final TextGridFile<T> tgf, final TextGridTierClass tierClass,
			final int id, final String name, final double startTime,
			final double endTime) {
		super(startTime, endTime);

		this.id = id;
		this.name = name;
		this.tierClass = tierClass;

		// this.entryIDs = new TreeMap<TextGridEntry<T>, Integer>();
		// this.entries = new ArrayList<TextGridEntry<T>>();
	}

	/**
	 * @param tgf
	 *            The {@link TextGridFile} object the <code>TextGridTier</code>
	 *            has been added to.
	 * @param tierClass
	 *            The tier class.
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
	 */
	TextGridTier(final TextGridFile<T> tgf, final TextGridTierClass tierClass,
			final int id, final String name, final double startTime,
			final double endTime, final int size) {

		super(startTime, endTime);

		this.id = id;
		this.name = name;
		this.tierClass = tierClass;

		// this.entryIDs = new TreeMap<TextGridEntry<T>, Integer>();
		// this.entries = new ArrayList<TextGridEntry<T>>(size);
	}

	// /**
	// * Constructs and adds a new {@link TextGridEntry} object.
	// *
	// * @param startTime
	// * The entry start time.
	// * @param endTime
	// * The entry end time.
	// * @param data
	// * The annotation data denoted by the entry.
	// * @return The newly-constructed and (successfully) -added
	// * <code>TextGridEntry</code> object.
	// */
	// public TextGridEntry<T> addEntry(final double startTime,
	// final double endTime, final T data) {
	// final int newEntryID = getNextFreeEntryID();
	// final TextGridEntry<T> newEntry = addEntry(newEntryID, startTime,
	// endTime, data);
	// lastAutomaticallyAddedEntryID = newEntryID;
	// return newEntry;
	//
	// }

	// /**
	// * Adds a {@link TextGridEntry} object.
	// *
	// * @param entry
	// * The <code>TextGridEntry</code> object to add.
	// * @return The ID of the newly-added <code>TextGridEntry</code> object.
	// */
	// public int addEntry(final TextGridEntry<T> entry) {
	// int nextFreeEntryID = getNextFreeEntryID();
	// addToEntryList(nextFreeEntryID, entry);
	// entryIDs.put(entry, nextFreeEntryID);
	// tgf.addEntry(entry);
	//
	// return nextFreeEntryID;
	//
	// }

	/**
	 * Constructs and adds a new {@link TextGridEntry} object.
	 * 
	 * @param startTime
	 *            The entry start time.
	 * @param endTime
	 *            The entry end time.
	 * @param data
	 *            The annotation data denoted by the entry.
	 * @return The newly-constructed and (successfully) -added
	 *         <code>TextGridEntry</code> object.
	 */
	public TextGridEntry<T> addEntry(final double startTime,
			final double endTime, final T data) {
		final TextGridEntry<T> newEntry = new TextGridEntry<T>(this, startTime,
				endTime, data);
		add(newEntry);
		return newEntry;

	}

	/**
	 * Constructs and adds a new {@link TextGridEntry} object.
	 * 
	 * @param id
	 *            The entry ID.
	 * @param startTime
	 *            The entry start time.
	 * @param endTime
	 *            The entry end time.
	 * @param data
	 *            The annotation data denoted by the entry.
	 * @return The newly-constructed and (successfully) -added
	 *         <code>TextGridEntry</code> object.
	 */
	public TextGridEntry<T> addEntry(final int id, final double startTime,
			final double endTime, final T data) {
		final TextGridEntry<T> newEntry = new TextGridEntry<T>(this, startTime,
				endTime, data);
		add(id, newEntry);
		return newEntry;

	}

	// /**
	// * Adds an {@link TextGridEntry} object into the entry {@link List},
	// * replacing any entry assigned to the given ID.
	// *
	// * @param id
	// * The ID to assign the <code>TextGridEntry</code> object to.
	// * @param entry
	// * The <code>TextGridEntry</code> object to add.
	// */
	// private void addEntry(final int id, final TextGridEntry<T> entry) {
	// // If the size of the entry list is equal to or less than the given
	// // entry ID
	// // (e.g. index), it cannot already exist
	// if (entries.size() <= id) {
	// addNewEntryExtend(entry, id);
	//
	// } else {
	// final TextGridEntry<T> oldEntry = entries.get(id);
	// // If the entry reference is null, it has not been added yet even
	// // though entries with greater IDs already have been
	// if (oldEntry == null) {
	// addNewEntry(entry, id);
	// } else {
	// // An entry already exists for the ID if it is not null; replace
	// // it
	// replaceEntry(id, entry);
	// }
	// }
	//
	// }

	// /**
	// * Adds a {@link TextGridEntry} object to a previously-unassigned ID.
	// *
	// * @param entry
	// * The <code>TextGridEntry</code> object to add.
	// * @param id
	// * The ID to assign the newly-added <code>TextGridEntry</code>
	// * object to.
	// */
	// private void addNewEntry(final TextGridEntry<T> entry, final int id) {
	// entries.set(id, entry);
	// entryIDs.put(entry, id);
	// tgf.addEntry(entry);
	// }
	//
	// /**
	// * Adds a {@link TextGridEntry} object to a previously-unassigned ID,
	// * extending {@link #entries} if necessary.
	// *
	// * @param entry
	// * The <code>TextGridEntry</code> object to add.
	// * @param id
	// * The ID to assign the newly-added <code>TextGridEntry</code>
	// * object to.
	// */
	// private void addNewEntryExtend(final TextGridEntry<T> entry, final int
	// id) {
	// extendToIndex(entries, id);
	// entries.set(id, entry);
	// entryIDs.put(entry, id);
	// tgf.addEntry(entry);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see TimeSeriesData#deepCompareTo(TimeSeriesData)
	 */
	@Override
	protected int deepCompareTo(final TextGridTier<T> arg0) {

		int comp = 0;

		if (id < arg0.id) {
			comp = -1;
		} else if (id > arg0.id) {
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
		if (!(obj instanceof TextGridTier)) {
			return false;
		}
		final TextGridTier<?> other = (TextGridTier<?>) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		// if (tgf == null) {
		// if (other.tgf != null)
		// return false;
		// } else if (!tgf.equals(other.tgf))
		// return false;
		if (tierClass != other.tierClass) {
			return false;
		}
		return true;
	}

	// /**
	// * @return the entries
	// */
	// public NavigableMap<TextGridEntry<T>, Integer> getEntries() {
	// return entryIDs;
	// }

	// /**
	// * Gets an {@link TextGridEntry} object by its ID.
	// *
	// * @param id
	// * The ID of the entry to get.
	// * @return The {@link TextGridEntry} object associated with the given ID.
	// */
	// public TextGridEntry<T> getEntry(final int id) {
	// return entries.get(id);
	// }

	/**
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	// /**
	// *
	// * @param entry
	// * The {@link TextGridEntry} to get the ID of.
	// * @return The <code>TextGridEntry</code> ID.
	// */
	// public Integer getID(final TextGridEntry<T> entry) {
	// return entryIDs.get(entry);
	//
	// }

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	// /**
	// * Gets the next free ID for automatically assigning to a newly-added
	// * {@link TextGridEntry}.
	// *
	// * @return The next free ID.
	// */
	// private int getNextFreeEntryID() {
	// int nextFreeEntryID = lastAutomaticallyAddedEntryID + 1;
	// while (isAssigned(nextFreeEntryID)) {
	// nextFreeEntryID++;
	// }
	//
	// return nextFreeEntryID;
	//
	// }

	/**
	 * @return the tierClass
	 */
	public TextGridTierClass getTierClass() {
		return tierClass;
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
		result = prime * result + id;
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result
				+ (tierClass == null ? 0 : tierClass.hashCode());
		return result;
	}

	// /**
	// * Checks if a given integer has already been used as an ID for a
	// * {@link TextGridEntry} object.
	// *
	// * @param id
	// * The ID to check.
	// * @return <code>true</code> iff there is an entry with the given ID.
	// */
	// private boolean isAssigned(final int id) {
	// final boolean isAssigned;
	// // If the size of the entry list is equal to or less than the given
	// // entry ID
	// // (e.g. index), it cannot exist
	// if (entries.size() <= id) {
	// isAssigned = false;
	// } else {
	// final TextGridEntry<T> entry = entries.get(id);
	// // If the entry reference is null, it has not been added yet even
	// // though entries with greater IDs already have been
	// if (entry == null) {
	// isAssigned = false;
	// } else {
	// isAssigned = true;
	// }
	// }
	//
	// return isAssigned;
	// }

	// /**
	// * Removes an {@link TextGridEntry} object.
	// *
	// * @param entry
	// * The <code>TextGridEntry</code> object to remove.
	// * @return The ID previously associated with the removed
	// * <code>TextGridEntry</code> object or <code>null</code> if no
	// * object was removed.
	// *
	// **/
	// public Integer remove(final TextGridEntry<T> entry) {
	// final Integer removedEntryID = entryIDs.remove(entry);
	// if (removedEntryID != null) {
	// entries.set(removedEntryID, null);
	// tgf.removeEntry(entry);
	// }
	//
	// return removedEntryID;
	// }

	// /**
	// * Puts an {@link TextGridEntry} object into the entry {@link List},
	// * replacing a previous one and updating the set of all
	// * <code>TextGridEntry</code> objects to match if necessary.
	// *
	// * @param id
	// * The entry ID.
	// * @param entry
	// * The <code>TextGridEntry</code> object to add to the
	// * <code>Map</code>.
	// */
	// private final void putEntry(final int id, final TextGridEntry<T> entry) {
	// final TextGridEntry<T> oldEntry = entries.get(id, entry);
	// if (oldEntry != null) {
	// entryIDs.remove(entry);
	// }
	// }

	// /**
	// * Removes an {@link TextGridEntry} object.
	// *
	// * @param id
	// * The entry ID.
	// * @return The removed <code>TextGridEntry</code> object if the tier
	// * contained it and it was successfully removed, or
	// * <code>null</code> otherwise.
	// */
	// public TextGridEntry<T> removeEntry(final int id) {
	// final TextGridEntry<T> removee = entries.set(id, null);
	// if (removee != null) {
	// entryIDs.remove(removee);
	// tgf.removeEntry(removee);
	// }
	// return removee;
	//
	// }

	// /**
	// * Replaces the {@link TextGridEntry} object associated with a given ID
	// with
	// * a given replacement entry.
	// *
	// * @param id
	// * The ID of the <code>TextGridEntry</code> object to replace.
	// * @param entry
	// * The <code>TextGridEntry</code> object to add.
	// */
	// private void replaceEntry(final int id, final TextGridEntry<T> entry) {
	// final TextGridEntry<T> oldEntry = entries.get(id);
	// entryIDs.remove(oldEntry);
	// entries.remove(id);
	//
	// entryIDs.put(entry, id);
	// // Length-checking is not necessary, since the index already existed for
	// // certain
	// entries.set(id, entry);
	// }

	// /**
	// *
	// * @return The amount of {@link TextGridEntry} objects the
	// * <code>TextGridTier</code> contains.
	// */
	// public final int size() {
	// return entryIDs.size();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TextGridTier[id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", tierClass=");
		builder.append(tierClass);
		builder.append(", startTime=");
		builder.append(endTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", elements=");
		builder.append(getElements());
		builder.append("]");
		return builder.toString();
	}

}
