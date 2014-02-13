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

import com.github.errantlinguist.Clearable;
import com.github.errantlinguist.Factory;

/**
 * 
 * @param <D>
 *            The type representing each datum stored in the result
 *            <code>TextGridFile</code> object.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 */
public class TextGridFileListenerFactory<D> implements Clearable,
		Factory<TextGridFile<D>>, TextGridListener<D> {

	private TextGridFile<D> currentTextGridFile;

	private final BasicEntryFactory<D> entryFactory;

	private final NamedTierFactory<D> namedTierFactory;

	private final TextGridFileFactory<D> textGridFileFactory;

	public TextGridFileListenerFactory() {
		textGridFileFactory = new TextGridFileFactory<D>();
		namedTierFactory = new NamedTierFactory<D>();
		entryFactory = new BasicEntryFactory<D>();

		currentTextGridFile = null;
	}

	@Override
	public void clear() {
		currentTextGridFile = null;

		textGridFileFactory.clear();
		namedTierFactory.clear();
		entryFactory.clear();
	}

	@Override
	public TextGridFile<D> create() {
		final TextGridFile<D> result = this.currentTextGridFile;
		clear();
		return result;
	}

	@Override
	public void notifyFileEndTime(final double time) {
		textGridFileFactory.setEndTime(time);
	}

	@Override
	public void notifyFileSize(final int size) {
		textGridFileFactory.setSize(size);
		final TextGridFile<D> newTextGridFile = textGridFileFactory.create();
		namedTierFactory.setParent(newTextGridFile);
		currentTextGridFile = newTextGridFile;
	}

	@Override
	public void notifyFileStartTime(final double time) {
		textGridFileFactory.setStartTime(time);
	}

	@Override
	public void notifyIntervalData(final D data) {
		notifyEntryData(data);
	}

	@Override
	public void notifyIntervalEndTime(final double time) {
		notifyEntryEndTime(time);
	}

	@Override
	public void notifyIntervalIndex(final int index) {
		notifyEntryIndex(index);

	}

	@Override
	public void notifyIntervalStartTime(final double time) {
		notifyEntryStartTime(time);
	}

	@Override
	public void notifyPointData(final D data) {
		notifyEntryData(data);
	}

	@Override
	public void notifyPointIndex(final int index) {
		notifyEntryIndex(index);
	}

	@Override
	public void notifyPointTime(final double time) {
		notifyEntryStartTime(time);
		notifyEntryEndTime(time);
	}

	@Override
	public void notifyTierClass(final TierClass tierClass) {
		namedTierFactory.setTierClass(tierClass);
	}

	@Override
	public void notifyTierEndTime(final double time) {
		namedTierFactory.setEndTime(time);
	}

	@Override
	public void notifyTierIndex(final int index) {
		namedTierFactory.setIndex(index);
	}

	@Override
	public void notifyTierIntervalCount(final int count) {
		notifyTierEntryCount(count);
	}

	@Override
	public void notifyTierName(final String name) {
		namedTierFactory.setName(name);
	}

	@Override
	public void notifyTierPointCount(final int count) {
		notifyTierEntryCount(count);
	}

	@Override
	public void notifyTierStartTime(final double time) {
		namedTierFactory.setStartTime(time);
	}

	protected void notifyEntryData(final D data) {
		entryFactory.setData(data);
		entryFactory.create();
	}

	protected void notifyEntryEndTime(final double time) {
		entryFactory.setEndTime(time);
	}

	protected void notifyEntryIndex(final int index) {
		entryFactory.setIndex(index);
	}

	protected void notifyEntryStartTime(final double time) {
		entryFactory.setStartTime(time);
	}

	protected void notifyTierEntryCount(final int count) {
		namedTierFactory.setSize(count);
		final NamedTier<D> newTier = namedTierFactory.create();
		entryFactory.setParent(newTier);
	}

}
