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

import com.github.errantlinguist.Clearable;
import com.github.errantlinguist.Factory;
import com.github.errantlinguist.time.DoubleDuration;
import com.github.errantlinguist.time.ImmutableDoubleDuration;
import com.github.errantlinguist.tree.BasicMutableChild;
import com.github.errantlinguist.tree.ChildList;

/**
 * A {@link Factory} which creates {@link NamedTier} objects.
 * 
 * @param <D>
 *            The type of the annotation data.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class NamedTierFactory<D> extends BasicMutableChild<TextGridFile<D>>
		implements Clearable, Factory<NamedTier<D>> {

	/**
	 * The initial value for newly-intialised primitive scalar fields.
	 */
	private static final int NULL_VALUE = -1;

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = 2168966977646964293L;

	/**
	 * The end time of the next new {@link BasicTier} object.
	 */
	private double endTime = NULL_VALUE;

	private int index = NULL_VALUE;

	/**
	 * The name of the next new {@link NamedTier} object.
	 */
	private String name = null;

	/**
	 * The size of the next new {@link BasicTier} object measured by the number
	 * of entries it has.
	 */
	private int size = NULL_VALUE;

	/**
	 * The start time of the next new {@link BasicTier} object.
	 */
	private double startTime = NULL_VALUE;

	/**
	 * The tier class of the next new {@link BasicTier} object.
	 */
	private TierClass tierClass;

	/**
	 * <strong>NOTE:</strong> Sets the instance returned by {@link #getParent()}
	 * to {@code null}.
	 */
	public NamedTierFactory() {
		this((TextGridFile<D>) null);
	}

	/**
	 * 
	 * @param parent
	 *            The {@link TextGridFile} instance to add newly-created
	 *            {@link NamedTier} objects to.
	 */
	public NamedTierFactory(final TextGridFile<D> parent) {
		super(parent);
	}

	@Override
	public void clear() {
		setParent(null);
		name = null;

		size = NULL_VALUE;
		startTime = NULL_VALUE;
		endTime = NULL_VALUE;
		tierClass = null;
	}

	@Override
	public NamedTier<D> create() {
		final TextGridFile<D> parent = getParent();
		final DoubleDuration duration = new ImmutableDoubleDuration(startTime,
				endTime);
		final NamedTier<D> result = new NamedTier<D>(parent, tierClass, name,
				duration, size);
		final ChildList<TextGridFile<D>, NamedTier<D>> children = parent
				.getChildren();
		children.ensureIndex(index);
		children.add(index, result);
		return result;
	}

	public void setEndTime(final double time) {
		endTime = time;

	}

	/**
	 * 
	 * @param index
	 *            The index of the next new {@link Tier} object.
	 */
	public void setIndex(final int index) {
		this.index = index;
	}

	/**
	 * Sets the name of the next new {@link NamedTier} object.
	 * 
	 * @param name
	 *            The name of the tier.
	 */
	public void setName(final String name) {
		this.name = name;

	}

	public void setSize(final int size) {
		this.size = size;

	}

	public void setStartTime(final double time) {
		startTime = time;

	}

	public void setTierClass(final TierClass tierClass) {
		this.tierClass = tierClass;

	}

}
