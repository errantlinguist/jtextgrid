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
package com.github.errantlinguist.tree;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.github.errantlinguist.collections.DelegateListHolder;
import com.github.errantlinguist.collections.DelegateListIteratorHolder;
import com.github.errantlinguist.collections.SetElements;

/**
 * @param <C>
 *            The type of the child elements.
 * 
 * @since 2014-02-07
 * @version 2014-02-07
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class ChildList<P, C extends MutableChild<? super P>> extends
		DelegateListHolder<C> implements MutableChild<P>, Serializable {

	/**
	 * 
	 * @since 2014-02-07
	 * @version 2014-02-07
	 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
	 * 
	 */
	private class ListIterator extends DelegateListIteratorHolder<C> {

		/**
		 * The element last returned by {@link #next()}.
		 */
		private transient C lastElement = null;

		/**
		 * 
		 * @param decorated
		 *            The {@link java.util.ListIterator} to decorate.
		 */
		public ListIterator(final java.util.ListIterator<C> decorated) {
			super(decorated);
		}

		@Override
		public void add(final C e) {
			super.add(e);
			// Calling the decorated ListIterator's method should throw an
			// exception if it fails; it is assumed here then that the operation
			// succeeds
			e.setParent(parent);
		}

		@Override
		public C next() {
			lastElement = super.next();
			return lastElement;
		}

		@Override
		public int nextIndex() {
			return super.nextIndex();
		}

		@Override
		public C previous() {
			lastElement = super.previous();
			return lastElement;
		}

		@Override
		public void remove() {
			super.remove();
			// Calling the decorated ListIterator's method should throw an
			// exception if it fails; it is assumed here then that the operation
			// succeeds
			lastElement.setParent(null);
		}

		@Override
		public void set(final C e) {
			super.set(e);
			// Calling the decorated ListIterator's method should throw an
			// exception if it fails; it is assumed here then that the operation
			// succeeds
			lastElement.setParent(null);
			e.setParent(parent);
		}

	}

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -4657111513438335958L;

	private static final <P, C extends MutableChild<? super P>> void setChildrenParent(
			final Iterable<C> children, final P parent) {
		for (final C child : children) {
			if (child != null) {
				child.setParent(parent);
			}
		}
	}

	/**
	 * The parent of the children in the list.
	 */
	private P parent;

	/**
	 * 
	 * @param decorated
	 *            The {@link List} to decorate.
	 */
	public ChildList(final List<C> decorated) {
		this(decorated, null);
	}

	/**
	 * 
	 * @param decorated
	 *            The {@link List} to decorate.
	 * @param parent
	 *            The parent of the children in the list.
	 */
	public <SP extends P> ChildList(final List<C> decorated, final SP parent) {
		super(decorated);
		this.parent = parent;
	}

	@Override
	public boolean add(final C e) {
		final boolean result = super.add(e);
		if (result) {
			e.setParent(parent);
		}
		return result;
	}

	@Override
	public void add(final int index, final C element) {
		super.add(index, element);
		element.setParent(parent);
	}

	@Override
	public boolean addAll(final Collection<? extends C> c) {
		final Set<? extends C> uniqueElementsToAdd = SetElements.difference(c,
				this);
		final boolean result = super.addAll(c);
		if (result) {
			final Set<C> uniqueAddedElements = SetElements.intersection(this,
					uniqueElementsToAdd);
			updateChildrenParent(uniqueAddedElements);
		}

		return result;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends C> c) {
		final Set<? extends C> uniqueElementsToAdd = SetElements.difference(c,
				this);
		final boolean result = super.addAll(index, c);
		if (result) {
			final Set<C> uniqueAddedElements = SetElements.intersection(this,
					uniqueElementsToAdd);
			updateChildrenParent(uniqueAddedElements);
		}

		return result;
	}

	@Override
	public void clear() {
		setChildrenParent(this, null);
		super.clear();
	}

	@Override
	public P getParent() {
		return parent;
	}

	@Override
	public java.util.ListIterator<C> iterator() {
		return listIterator();
	}

	@Override
	public java.util.ListIterator<C> listIterator() {
		return new ListIterator(super.listIterator());
	}

	@Override
	public java.util.ListIterator<C> listIterator(final int index) {
		return new ListIterator(super.listIterator(index));
	}

	@Override
	public C remove(final int index) {
		final C result = super.remove(index);
		if (result != null) {
			result.setParent(null);
		}
		return result;
	}

	@Override
	public boolean remove(final Object o) {
		final boolean result = super.remove(o);
		if (result) {
			@SuppressWarnings("unchecked")
			final C element = (C) o;
			element.setParent(null);
		}
		return result;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		final Set<C> uniqueElementsToRemove = SetElements.intersection(this, c);
		final boolean result = super.removeAll(c);
		if (result) {
			final Set<C> uniqueRemovedElements = SetElements.intersection(this,
					uniqueElementsToRemove);
			setChildrenParent(uniqueRemovedElements, null);
		}
		return result;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		final Set<C> uniqueElementsToRemove = SetElements.difference(this, c);
		final boolean result = super.retainAll(uniqueElementsToRemove);
		if (result) {
			final Set<C> uniqueRemovedElements = SetElements.intersection(this,
					uniqueElementsToRemove);
			setChildrenParent(uniqueRemovedElements, null);
		}
		return result;
	}

	@Override
	public C set(final int index, final C element) {
		final C result = set(index, element);
		if (result != null) {
			result.setParent(null);
		}
		return result;
	}

	@Override
	public void setParent(final P parent) {
		this.parent = parent;
		updateChildrenParent();
	}

	@Override
	public List<C> subList(final int fromIndex, final int toIndex) {
		return new ChildList<P, C>(super.subList(fromIndex, toIndex),
				getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String delegatePrefix = "ChildList [delegate()=";
		final String delegateStr = Objects.toString(delegate());
		final StringBuilder builder = new StringBuilder(delegatePrefix.length()
				+ delegateStr.length() + 1);
		builder.append(delegatePrefix);
		builder.append(delegateStr);
		builder.append(']');
		return builder.toString();
	}

	private void updateChildrenParent() {
		updateChildrenParent(this);
	}

	private void updateChildrenParent(final Iterable<C> children) {
		setChildrenParent(children, parent);
	}

}
