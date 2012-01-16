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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

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
public class TextGridTier<T> extends TimeSeriesData<TextGridTier<T>> {

	/**
	 * The TextGrid tier class, e.g.&nbsp;"IntervalTier" or "TextTier", as defined in
	 * the TextGrid file itself.
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
		 * denote points, i.e.&nbsp;for which {@link TextGridEntry#getStartTime()} =
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

	/**
	 * All {@link TextGridEntry} objects.
	 */
	protected final NavigableSet<TextGridEntry<T>> entries;

	private final Map<Integer, TextGridEntry<T>> entryIDs;

	/**
	 * The tier ID.
	 */
	protected final int id;

	/**
	 * The tier name.
	 */
	protected final String name;

	private final TextGridFile<T> tgf;

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

		this.tgf = tgf;
		this.id = id;
		this.name = name;
		this.tierClass = tierClass;

		this.entries = new TreeSet<TextGridEntry<T>>();
		this.entryIDs = new HashMap<Integer, TextGridEntry<T>>();
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

		this.tgf = tgf;
		this.id = id;
		this.name = name;
		this.tierClass = tierClass;

		this.entries = new TreeSet<TextGridEntry<T>>();
		this.entryIDs = new HashMap<Integer, TextGridEntry<T>>(size);
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
	public final TextGridEntry<T> addEntry(final int id,
			final double startTime, final double endTime, final T data) {
		final TextGridEntry<T> newEntry = new TextGridEntry<T>(this, id,
				startTime, endTime, data);

		if (addEntry(newEntry)) {
			return newEntry;
		} else {
			return null;
		}

	}

	/**
	 * Adds an {@link TextGridEntry object}.
	 * 
	 * @param entry
	 *            The <code>TextGridEntry</code> object to add.
	 * @return <code>true</code> iff the <code>TextGridEntry</code> was
	 *         successfully added to the set of all <code>TextGridEntry</code>
	 *         objects.
	 */
	public final boolean addEntry(final TextGridEntry<T> entry) {
		if (entries.add(entry)) {
			putEntry(entry.id, entry);
			tgf.addEntry(entry);
			return true;
		} else {
			return false;
		}
	}

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
		if (!(obj instanceof TextGridTier<?>)) {
			return false;
		}
		final TextGridTier<?> other = (TextGridTier<?>) obj;
		if (entries == null) {
			if (other.entries != null) {
				return false;
			}
		} else if (!entries.equals(other.entries)) {
			return false;
		}
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
		if (tgf == null) {
			if (other.tgf != null) {
				return false;
			}
		} else if (!tgf.equals(other.tgf)) {
			return false;
		}
		if (tierClass == null) {
			if (other.tierClass != null) {
				return false;
			}
		} else if (!tierClass.equals(other.tierClass)) {
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
	 * Gets an {@link TextGridEntry} object by its ID.
	 * 
	 * @param id
	 *            The ID of the entry to get.
	 * @return The {@link TextGridEntry} object associated with the given ID.
	 */
	public final TextGridEntry<T> getEntry(final int id) {
		return entryIDs.get(id);
	}

	/**
	 * 
	 * @return An ordered {@link List} of all the {@link TextGridEntry} objects
	 *         associated with the tier.
	 */
	public final List<TextGridEntry<T>> getEntryList() {
		final List<TextGridEntry<T>> newList = new ArrayList<TextGridEntry<T>>(
				entries);
		return newList;
	}

	/**
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

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
		result = prime * result + (entries == null ? 0 : entries.hashCode());
		result = prime * result + id;
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result
				+ (tierClass == null ? 0 : tierClass.hashCode());
		return result;
	}

	/**
	 * Puts an {@link TextGridEntry} object into the entry ID {@link Map},
	 * replacing a previous one and updating the set of all
	 * <code>TextGridEntry</code> objects to match if necessary.
	 * 
	 * @param id
	 *            The entry ID.
	 * @param entry
	 *            The <code>TextGridEntry</code> object to add to the
	 *            <code>Map</code>.
	 */
	private final void putEntry(final int id, final TextGridEntry<T> entry) {
		final TextGridEntry<T> oldEntry = entryIDs.put(id, entry);
		if (oldEntry != null) {
			entries.remove(entry);
		}
	}

	/**
	 * Removes an {@link TextGridEntry} object.
	 * 
	 * @param id
	 *            The entry ID.
	 * @return The removed <code>TextGridEntry</code> object if the tier
	 *         contained it and it was successfully removed, or
	 *         <code>null</code> otherwise.
	 */
	public final TextGridEntry<T> removeEntry(final int id) {
		final TextGridEntry<T> removee = entryIDs.remove(id);
		if (removee != null) {
			entries.remove(removee);
			tgf.removeEntry(removee);
		}
		return removee;

	}

	/**
	 * Removes an {@link TextGridEntry} object.
	 * 
	 * @param entry
	 *            The <code>TextGridEntry</code> object to remove.
	 * @return <code>true</code> iff the <code>TextGridEntry</code> was
	 *         successfully removed from the set of all
	 *         <code>TextGridEntry</code> objects.
	 * 
	 **/
	public final boolean removeEntry(final TextGridEntry<T> entry) {
		final boolean wasRemoved = entries.remove(entry);
		if (wasRemoved) {
			entryIDs.remove(entry.id);
			tgf.removeEntry(entry);
		}

		return wasRemoved;
	}

	/**
	 * 
	 * @return The amount of {@link TextGridEntry} objects the
	 *         <code>TextGridTier</code> contains.
	 */
	public final int size() {
		return entries.size();
	}

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
		builder.append(", entries=");
		builder.append(entries);
		builder.append("]");
		return builder.toString();
	}

}
