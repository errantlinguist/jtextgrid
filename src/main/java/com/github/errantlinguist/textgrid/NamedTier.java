/*
 * 	Copyright 2012--2014 Todd Shore
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
package com.github.errantlinguist.textgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.errantlinguist.Named;
import com.github.errantlinguist.time.DoubleDuration;
import com.github.errantlinguist.time.Durative;
import com.github.errantlinguist.tree.ChildList;
import com.github.errantlinguist.tree.MutableChild;

/**
 * A tier in a TextGrid file with a given name.
 * 
 * @param <D>
 *            The object type representing the data denoted by the tier entries.
 * 
 * @since 2014-02-11
 * @version 2014-02-11
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 */
public class NamedTier<D> implements Durative<Double>,
		MutableChild<TextGridFile<D>>, Named<String>, Serializable {

	/**
	 * A prime constant used in calculating {@link #hashCode()}.
	 */
	private static final int HASH_CODE_PRIME = 31;

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -1536080667614929606L;

	/**
	 * A list of the {@link Entry entries} this TextGrid file contains.
	 */
	private final ChildList<NamedTier<D>, BasicEntry<D>> children;

	/**
	 * The tier duration.
	 */
	private final DoubleDuration duration;

	/**
	 * The pre-cached hash code for immutable members.
	 */
	private final transient int hashCode = calculatePartialHashCode();

	/**
	 * The tier name.
	 */
	private final String name;

	/**
	 * The {@link TextGridFile} object the <code>Tier</code> has been added to.
	 */
	private TextGridFile<D> parent;

	/**
	 * The tier class.
	 */
	private final TierClass tierClass;

	/**
	 * @param parent
	 *            The {@link TextGridFile} object the <code>Tier</code> has been
	 *            added to.
	 * @param tierClass
	 *            The tier class.
	 * @param name
	 *            The tier name.
	 * @param duration
	 *            The tier duration.
	 */
	public NamedTier(final TextGridFile<D> parent, final TierClass tierClass,
			final String name, final DoubleDuration duration) {
		this(parent, tierClass, name, duration, new ArrayList<BasicEntry<D>>());
	}

	/**
	 * @param parent
	 *            The {@link TextGridFile} object the <code>Tier</code> has been
	 *            added to.
	 * @param tierClass
	 *            The tier class.
	 * @param name
	 *            The tier name.
	 * @param duration
	 *            The tier duration.
	 * @param initialSize The initial size of the backing {@link List}.
	 */
	public NamedTier(final TextGridFile<D> parent, final TierClass tierClass,
			final String name, final DoubleDuration duration,
			final int initialSize) {
		this(parent, tierClass, name, duration, new ArrayList<BasicEntry<D>>(
				initialSize));
	}

	/**
	 * 
	 * @param parent  The {@link TextGridFile} object the <code>Tier</code> has been
	 *            added to.
	 * @param tierClass The tier class.
	 * @param name  The tier name.
	 * @param duration  The tier duration.
	 * @param entries The backing {@link List}.
	 */
	public NamedTier(final TextGridFile<D> parent, final TierClass tierClass,
			final String name, final DoubleDuration duration,
			final List<BasicEntry<D>> entries) {
		this.parent = parent;
		this.tierClass = tierClass;
		this.name = name;
		this.duration = duration;
		this.children = new ChildList<NamedTier<D>, BasicEntry<D>>(entries,
				this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		final boolean result;

		if (super.equals(obj)) {
			result = true;
		} else if (obj == null) {
			result = false;
		} else if (obj instanceof NamedTier) {
			final NamedTier<?> other = (NamedTier<?>) obj;
			result = isEquivalentTo(other);
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * @return the children
	 */
	public ChildList<NamedTier<D>, BasicEntry<D>> getChildren() {
		return children;
	}

	@Override
	public DoubleDuration getDuration() {
		return duration;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TextGridFile<D> getParent() {
		return parent;
	}

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
		int result = hashCode;
		result = (HASH_CODE_PRIME * result)
				+ ((duration == null) ? 0 : duration.hashCode());
		return result;
	}

	@Override
	public void setParent(final TextGridFile<D> parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int estimatedStringLength = estimateStringLength();
		final StringBuilder builder = new StringBuilder(estimatedStringLength);
		builder.append(this.getClass().getSimpleName());
		builder.append("[getTierClass()=");
		builder.append(getTierClass());
		builder.append(", getDuration()=");
		builder.append(getDuration());
		builder.append(", getChildren()=");
		builder.append(getChildren());
		builder.append(']');
		return builder.toString();
	}

	/**
	 * 
	 * @return The hashcode for immutable members.
	 */
	private int calculatePartialHashCode() {
		int result = 1;
		result = (HASH_CODE_PRIME * result)
				+ ((name == null) ? 0 : name.hashCode());
		result = (HASH_CODE_PRIME * result)
				+ ((tierClass == null) ? 0 : tierClass.hashCode());
		return result;
	}

	/**
	 * Estimates the length of a string representation of the tier
	 * {@link BasicEntry entries}.
	 * 
	 * @return The estimated length of the string representation of the child
	 *         entries.
	 */
	private int estimateEntryStringLength() {
		return getChildren().size() * BasicEntry.getEstimatedStringReprLength();
	}

	/**
	 * Estimates the length of the string representation of this object.
	 * 
	 * @return The estimated length of the string representation.
	 */
	private int estimateStringLength() {
		int estimatedStringLength = estimateEntryStringLength();
		// Add some extra length for misc. padding
		estimatedStringLength += 32;

		return estimatedStringLength;
	}

	/**
	 * Checks if this object is equivalent to another one (ignoring identity).
	 * 
	 * @param other
	 *            The other object to compare to.
	 * @return <code>true</code> iff this object is equivalent to the other one.
	 */
	private boolean isEquivalentTo(final NamedTier<?> other) {
		return ((getTierClass() == other.getTierClass())
				&& Objects.equals(getName(), other.getName())
				&& Objects.equals(getDuration(), other.getDuration()) && Objects
					.equals(getChildren(), other.getChildren()));
	}

}
