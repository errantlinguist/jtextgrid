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
package com.github.errantlinguist.collections;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.ForwardingList;

/**
 * A subclass of {@link ForwardingList} which holds a reference to
 * {@link #delegate() the list it delegates to}.
 * 
 * @param <E>
 *            The type of the elements in the list.
 * 
 * @since 2014-02-10
 * @version 2014-02-10
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class DelegateListHolder<E> extends ForwardingList<E> {

	/**
	 * The {@link List} to delegate to.
	 */
	private final List<E> delegate;

	/**
	 * 
	 * @param delegate
	 *            The {@link List} to delegate to.
	 */
	public DelegateListHolder(final List<E> delegate) {
		this.delegate = delegate;
	}

	public boolean ensureIndex(final int index) {
		return ListIndex.ensureIndex(this, index);
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
		} else if (obj instanceof DelegateListHolder<?>) {
			final DelegateListHolder<?> other = (DelegateListHolder<?>) obj;
			result = Objects.equals(delegate(), other.delegate());
		} else {
			result = false;
		}

		return result;
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
		final Object decorated = delegate();
		result = (prime * result)
				+ ((decorated == null) ? 0 : decorated.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String delegatePrefix = "DelegateListHolder [delegate()=";
		final String delegateStr = Objects.toString(delegate());
		final StringBuilder builder = new StringBuilder(delegatePrefix.length()
				+ delegateStr.length() + 1);
		builder.append(delegatePrefix);
		builder.append(delegateStr);
		builder.append(']');
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.collect.ForwardingList#delegate()
	 */
	@Override
	protected final List<E> delegate() {
		return delegate;
	}

}