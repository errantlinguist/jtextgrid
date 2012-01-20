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
 * @param <D>
 *            The object type representing the data denoted by the tier entries.
 */
public class Tier<D> extends TimeSeriesDataCollection<Entry<D>, Tier<D>> {

	/**
	 * The TextGrid tier class, e.g.&nbsp;"IntervalTier" or "TextTier", as
	 * defined in the TextGrid file itself.
	 * 
	 * @author tshore
	 * @version 2012-01-16
	 * @since 2012-01-16
	 * 
	 */
	public static enum TierClass {
		/**
		 * A {@link Tier} with {@link Entry} instances which denote a time
		 * interval, i.e.&nbsp;for which {@link TimeSeriesData#getStartTime()} <
		 * {@link TimeSeriesData#getEndTime()}.
		 */
		INTERVAL("IntervalTier"),
		/**
		 * A {@link Tier} with {@link Entry} instances which denote points,
		 * i.e.&nbsp;for which {@link Entry#getStartTime()} =
		 * {@link Entry#getEndTime()}.
		 */
		TEXT("TextTier");

		private static final Map<String, TierClass> CLASS_NAMES = createStaticClassNameMap();

		private static final Map<String, TierClass> createStaticClassNameMap() {

			final Map<String, TierClass> classNames = new HashMap<String, TierClass>(
					TierClass.values().length);
			for (final TierClass tierClass : TierClass.values()) {
				classNames.put(tierClass.getName(), tierClass);
			}
			return Collections.unmodifiableMap(classNames);
		}

		/**
		 * Gets the {@link TierClass} represented by the given string name,
		 * corresponding to the name as defined in the TextGrid file.
		 * 
		 * @param name
		 *            The class name.
		 * @return The <code>TierClass</code> associated with the name.
		 */
		public static TierClass getTierClass(final String name) {
			return CLASS_NAMES.get(name);
		}

		/**
		 * The string name representing the <code>TierClass</code>, as defined
		 * in the TextGrid file.
		 */
		private final String name;

		/**
		 * 
		 * @param name
		 *            The string name of the tier class, as defined in the
		 *            TextGrid file.
		 */
		private TierClass(final String name) {
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

	private final TextGridFile<D> file;

	/**
	 * The tier class.
	 */
	protected final TierClass tierClass;

	/**
	 * @param tgf
	 *            The {@link TextGridFile} object the <code>Tier</code> has been
	 *            added to.
	 * @param tierClass
	 *            The tier class.
	 * @param startTime
	 *            The tier start time.
	 * @param endTime
	 *            The tier end time.
	 */
	Tier(final TextGridFile<D> tgf, final TierClass tierClass,
			final double startTime, final double endTime) {
		super(startTime, endTime);

		this.file = tgf;
		this.tierClass = tierClass;
	}

	/**
	 * @param tgf
	 *            The {@link TextGridFile} object the <code>Tier</code> has been
	 *            added to.
	 * @param tierClass
	 *            The tier class.
	 * @param startTime
	 *            The tier start time.
	 * @param endTime
	 *            The tier end time.
	 * @param size
	 *            The size of the tier measured by the number of {@link Entry}
	 *            objects it has.
	 */
	Tier(final TextGridFile<D> tgf, final TierClass tierClass,
			final double startTime, final double endTime, final int size) {

		super(startTime, endTime);

		this.file = tgf;
		this.tierClass = tierClass;

	}

	/**
	 * Constructs and adds a new {@link Entry} object.
	 * 
	 * @param startTime
	 *            The entry start time.
	 * @param endTime
	 *            The entry end time.
	 * @param data
	 *            The annotation data denoted by the entry.
	 * @return The newly-constructed and (successfully) -added
	 *         <code>Entry</code> object.
	 */
	public Entry<D> addEntry(final double startTime, final double endTime,
			final D data) {
		final Entry<D> newEntry = new Entry<D>(this, startTime, endTime, data);
		add(newEntry);
		file.addEntry(newEntry);
		return newEntry;

	}

	/**
	 * Constructs and adds a new {@link Entry} object.
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
	 *         <code>Entry</code> object.
	 */
	public Entry<D> addEntry(final int id, final double startTime,
			final double endTime, final D data) {
		final Entry<D> newEntry = new Entry<D>(this, startTime, endTime, data);
		add(id, newEntry);
		file.addEntry(newEntry);
		return newEntry;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see TimeSeriesData#deepCompareTo(TimeSeriesData)
	 */
	@Override
	protected int deepCompareTo(final Tier<D> arg0) {

		int comp = 0;

		// if (id < arg0.id) {
		// comp = -1;
		// } else if (id > arg0.id) {
		// comp = 1;
		//
		// } else {

		if (size() < arg0.size()) {
			comp = -1;
		} else if (size() > arg0.size()) {
			comp = 1;
		}

		// }
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
		if (!(obj instanceof Tier)) {
			return false;
		}
		final Tier<?> other = (Tier<?>) obj;
		// if (id != other.id) {
		// return false;
		// }

		if (tierClass != other.tierClass) {
			return false;
		}
		return true;
	}

	/**
	 * @return The ID of the tier.
	 */
	public Integer getID() {
		return file.getID(this);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return file.getName(this);
	}

	/**
	 * @return the tierClass
	 */
	public TierClass getTierClass() {
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
		// result = prime * result + id;
		result = prime * result
				+ (tierClass == null ? 0 : tierClass.hashCode());
		// If the TextGrid file is not null, add the hash of the tier ID
		if (file != null) {
			final Integer id = getID();
			result = prime * result + (id == null ? 0 : id.hashCode());
		} else {
			result = prime * result;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Tier[tierClass=");
		builder.append(tierClass);
		builder.append(", startTime=");
		builder.append(endTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", entries=");
		builder.append(getElements());
		builder.append("]");
		return builder.toString();
	}

}
