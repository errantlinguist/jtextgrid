/*
 * 	Copyright 2014 Todd Shore
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

import com.github.errantlinguist.Clearable;
import com.github.errantlinguist.Factory;
import com.github.errantlinguist.time.ImmutableDoubleDuration;
import com.github.errantlinguist.tree.BasicMutableChild;
import com.github.errantlinguist.tree.ChildList;

/**
 * A {@link Factory} which creates {@link BasicEntry} objects.
 * 
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 *  @version 2014-02-06
 * @since 2014-02-06
 *
 * @param <D>
 *            The type of the annotation data.
 * 
 */
public class BasicEntryFactory<D> extends BasicMutableChild<NamedTier<D>>
		implements Clearable, Factory<BasicEntry<D>> {

	/**
	 * A constant value used for estimating the length of the string
	 * representation of the object returned by {@link #toString()}.
	 */
	private static final int ESTIMATED_STRING_REPR_LENGTH = 168;

	/**
	 * The initial value for newly-intialised primitive scalar fields.
	 */
	private static final int NULL_VALUE = -1;

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -6063742066662401250L;

	/**
	 * The data represented by the new {@link BasicEntry} object.
	 */
	private D data;

	/**
	 * The end time of the next new {@link BasicEntry} object.
	 */
	private double endTime = NULL_VALUE;

	/**
	 * The index of the next new {@link BasicEntry} object.
	 */
	private int index = NULL_VALUE;

	/**
	 * The start time of the next new {@link BasicEntry} object.
	 */
	private double startTime = NULL_VALUE;

	/**
	 * <strong>NOTE:</strong> Sets the instance returned by {@link #getParent()}
	 * to {@code null}.
	 */
	public BasicEntryFactory() {
		super();
	}

	/**
	 * 
	 * @param parent
	 *            The {@link NamedTier} instance to add newly-created
	 *            {@link BasicEntry} objects to.
	 */
	public BasicEntryFactory(final NamedTier<D> parent) {
		super(parent);
	}

	@Override
	public void clear() {
		setParent(null);
		index = NULL_VALUE;
		startTime = NULL_VALUE;
		endTime = NULL_VALUE;
	}

	@Override
	public BasicEntry<D> create() {
		final NamedTier<D> parent = getParent();
		final ImmutableDoubleDuration duration = new ImmutableDoubleDuration(
				startTime, endTime);
		final BasicEntry<D> result = new BasicEntry<D>(parent, duration, data);
		final ChildList<NamedTier<D>, BasicEntry<D>> children = parent
				.getChildren();
		children.ensureIndex(index);
		children.add(index, result);
		return result;
	}

	/**
	 * @param data
	 *            The data represented by the new {@link BasicEntry} object.
	 */
	public void setData(final D data) {
		this.data = data;
	}

	/**
	 * Sets the end time of the next new {@link BasicEntry} object.
	 * 
	 * @param time
	 *            The end time of the entry.
	 */
	public void setEndTime(final double time) {
		endTime = time;

	}

	/**
	 * Sets the index of the next new {@link BasicEntry} object.
	 * 
	 * @param index
	 *            The index of the entry.
	 */
	public void setIndex(final int index) {
		this.index = index;

	}

	/**
	 * Sets the start time of the next new {@link BasicEntry} object.
	 * 
	 * @param time
	 *            The start time of the entry.
	 */
	public void setStartTime(final double time) {
		startTime = time;

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
		builder.append("EntryFactory [index=");
		builder.append(index);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", data=");
		builder.append(data);
		builder.append(']');
		return builder.toString();
	}

}
