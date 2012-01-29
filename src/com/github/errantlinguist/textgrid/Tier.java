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

import java.util.Collection;
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
public class Tier<D> extends TimeSeriesDataList<Entry<D>, Tier<D>> {

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

	private TextGridFile<D> file;

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
	public Entry<D> add(final double startTime, final double endTime,
			final D data) {
		final Entry<D> newEntry = new Entry<D>(this, startTime, endTime, data);
		addNew(newEntry);
		file.addEntry(newEntry);
		return newEntry;

	}

	/**
	 * Adds an {@link Entry}.
	 * 
	 * @param entry
	 *            The <code>Entry</code> to add.
	 * @return The index of the newly-added <code>Entry</code>.
	 */
	@Override
	public boolean add(final Entry<D> entry) {
		final int index = getNextFreeIndex();
		add(index, entry);
		setLastAutomaticallyAddedIndex(index);
		return true;

	}

	/**
	 * Constructs and adds a new {@link Entry} object.
	 * 
	 * @param index
	 *            The entry index.
	 * @param startTime
	 *            The entry start time.
	 * @param endTime
	 *            The entry end time.
	 * @param data
	 *            The annotation data denoted by the entry.
	 * @return The newly-constructed and (successfully) -added
	 *         <code>Entry</code> object.
	 */
	public Entry<D> add(final int index, final double startTime,
			final double endTime, final D data) {
		final Entry<D> newEntry = new Entry<D>(this, startTime, endTime, data);
		addNew(index, newEntry);
		file.addEntry(newEntry);
		return newEntry;

	}

	/**
	 * Adds a new {@link Entry} at a given index.
	 * 
	 * @param entry
	 *            The <code>Entry</code> to add.
	 */
	@Override
	public void add(final int index, final Entry<D> entry) {
		entry.setTier(this);
		super.add(index, entry);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.errantlinguist.textgrid.TimeSeriesDataList#addAll(java.util
	 * .Collection)
	 */
	@Override
	public boolean addAll(final Collection<? extends Entry<D>> arg0) {
		boolean wasChanged = false;
		for (final Entry<D> entry : arg0) {
			if (add(entry)) {
				entry.setTier(this);
				wasChanged = true;
			}

		}
		return wasChanged;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.errantlinguist.textgrid.TimeSeriesDataList#addAll(int,
	 * java.util.Collection)
	 */
	@Override
	public boolean addAll(int arg0, final Collection<? extends Entry<D>> arg1) {
		if (getElements().size() == arg0) {
			return addAll(arg1);
		} else {
			shiftRight(getElements(), arg0, getElements().size(), arg1.size());

			for (final Entry<D> entry : arg1) {
				add(arg0, entry);
				entry.setTier(this);
				// Increment the index for the next element to be added
				arg0++;
			}
			return false;
		}
	}

	/**
	 * Adds a new {@link Entry}.
	 * 
	 * @param entry
	 *            The <code>Entry</code> to add.
	 * @return The index of the newly-added <code>Entry</code>.
	 */
	private boolean addNew(final Entry<D> entry) {
		final int index = getNextFreeIndex();
		addNew(index, entry);
		setLastAutomaticallyAddedIndex(index);
		return true;

	}

	/**
	 * Adds an {@link Entry} at a given index.
	 * 
	 * @param index
	 *            The index to add at.
	 * @param entry
	 *            The <code>Entry</code> to add.
	 */
	private void addNew(final int index, final Entry<D> entry) {
		super.add(index, entry);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see TimeSeriesData#deepCompareTo(TimeSeriesData)
	 */
	@Override
	protected int deepCompareTo(final Tier<D> arg0) {

		int comp = 0;

		// if (index < arg0.index) {
		// comp = -1;
		// } else if (index > arg0.index) {
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
		// if (index != other.index) {
		// return false;
		// }

		if (tierClass != other.tierClass) {
			return false;
		}
		return true;
	}

	/**
	 * @return the file
	 */
	protected TextGridFile<D> getFile() {
		return file;
	}

	/**
	 * @return The index of the tier.
	 */
	public Integer getIndex() {
		return file.getIndex(this);
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
		// result = prime * result + index;
		result = prime * result
				+ (tierClass == null ? 0 : tierClass.hashCode());
		// If the TextGrid file is not null, add the hash of the tier index
		if (file != null) {
			final Integer index = getIndex();
			result = prime * result + (index == null ? 0 : index.hashCode());
		} else {
			result = prime * result;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.errantlinguist.textgrid.TimeSeriesDataList#set(int,
	 * java.lang.Object)
	 */
	@Override
	public Entry<D> set(final int index, final Entry<D> entry) {
		entry.setTier(this);
		return super.set(index, entry);
	}

	/**
	 * @param file
	 *            the file to set
	 */
	protected void setFile(final TextGridFile<D> file) {
		this.file = file;
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
		builder.append("[tierClass=");
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
