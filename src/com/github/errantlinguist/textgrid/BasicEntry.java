/*
 * 	Copyright 2011--2014 Todd Shore
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
import java.util.Objects;

import com.github.errantlinguist.time.DoubleDuration;
import com.github.errantlinguist.time.ImmutableDoubleDuration;
import com.github.errantlinguist.tree.BasicMutableChild;

/**
 * A single entry in the TextGrid file, representing either an interval or a
 * point on a tier.
 * 
 * @param <D>
 *            The object type representing the entry data.
 * 
 * @since 2011-04-15
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 * 
 */
public final class BasicEntry<D> extends BasicMutableChild<NamedTier<D>>
		implements Comparable<BasicEntry<D>>, Entry<D> {

	/**
	 * A constant value used for estimating the length of the string
	 * representation of the object returned by {@link #toString()}.
	 */
	private static final int ESTIMATED_STRING_REPR_LENGTH = 128;

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -6153586692612823432L;

	/**
	 * 
	 * @return A value used for estimating the length of the string
	 *         representation of the object returned by {@link #toString()}.
	 */
	protected static final int getEstimatedStringReprLength() {
		return ESTIMATED_STRING_REPR_LENGTH;
	}

	/**
	 * The annotation data the entry represents.
	 */
	private final D data;

	/**
	 * The duration of the entry.
	 */
	private final ImmutableDoubleDuration duration;

	/**
	 * @param parent
	 *            The {@link NamedTier} object representing the TextGrid tier
	 *            the entry is on.
	 * @param duration
	 *            The duration of the entry.
	 * @param data
	 *            The annotation data the entry represents.
	 */
	protected BasicEntry(final NamedTier<D> parent,
			final ImmutableDoubleDuration duration, final D data) {
		super(parent);
		this.duration = duration;
		this.data = data;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see TimeSeriesData#deepCompareTo(TimeSeriesData)
	 */
	@Override
	public int compareTo(final BasicEntry<D> o) {
		final int result = compareDurationTo(o);
		if (result == 0) {
			// result = compareParentTo(o);
		}

		return result;
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
		} else if (obj instanceof BasicEntry) {
			final Entry<?> other = (Entry<?>) obj;
			result = isEquivalentTo(other);
		} else {
			result = false;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.errantlinguist.textgrid.Entry#getData()
	 */
	@Override
	public D getData() {
		return data;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#hashCode()
	// */
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = super.hashCode();
	// result = (prime * result) + (data == null ? 0 : data.hashCode());
	// // If the tier is not null, combine the hash of the entry's index with
	// // the
	// // hash in order to differentiate entries which are equal in all ways
	// // except for the tier they are on.
	// final Integer id;
	// if (parent != null) {
	// id = getIndex();
	// result = (prime * result) + (id == null ? 0 : id.hashCode());
	// } else {
	// id = result = prime * result;
	// }
	//
	// return result;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.errantlinguist.textgrid.Entry#getDuration()
	 */
	@Override
	public DoubleDuration getDuration() {
		return duration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		final D data = getData();
		result = (prime * result) + ((data == null) ? 0 : data.hashCode());
		final DoubleDuration duration = getDuration();
		result = (prime * result)
				+ ((duration == null) ? 0 : duration.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(
				ESTIMATED_STRING_REPR_LENGTH);
		builder.append("Entry [getDuration()=");
		builder.append(getDuration());
		builder.append(", getData()=");
		builder.append(getData());
		builder.append(']');
		return builder.toString();
	}

	private final int compareDurationTo(final Entry<D> o) {
		final int result;
		final DoubleDuration duration1 = getDuration();
		final DoubleDuration duration2 = o.getDuration();
		if ((duration1 == null) && (duration2 == null)) {
			result = 0;
		} else {
			result = duration1.compareTo(duration2);
		}
		return result;
	}

	private boolean isEquivalentTo(final Entry<?> other) {
		return (Objects.equals(getDuration(), other.getDuration()) && Objects
				.equals(getData(), other.getData()));
	}

	// private final int compareParentTo(final Entry<D> o) {
	// final int result;
	// final NamedTier<D> parent1 = getParent();
	// final NamedTier<D> parent2 = o.getParent();
	// if ((parent1 == null) && (parent2 == null)) {
	// result = 0;
	// } else {
	// result = parent1.compareTo(parent2);
	// }
	//
	// return result;
	// }
}
