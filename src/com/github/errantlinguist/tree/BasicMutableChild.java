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
import java.util.Objects;

/**
 * A mutable implementation of {@link Child}.
 * 
 * @param <P>
 *            The type of the parent object.
 * 
 * @since 2014-02-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 */
public class BasicMutableChild<P> implements MutableChild<P>, Serializable {

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -6536291117929831245L;

	/**
	 * The parent of this child object.
	 */
	private P parent;

	/**
	 * <strong>NOTE:</strong> Sets the instance returned by {@link #getParent()}
	 * to {@code null}.
	 */
	public BasicMutableChild() {
		this(null);
	}

	public BasicMutableChild(final P parent) {
		this.parent = parent;
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
		} else if (obj instanceof BasicMutableChild<?>) {
			final BasicMutableChild<?> other = (BasicMutableChild<?>) obj;
			result = isEquivalentTo(other);
		} else {
			result = false;
		}

		return result;
	}

	@Override
	public P getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            The parent of this child object.
	 */
	@Override
	public void setParent(final P parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		final String prefix = "BasicMutableChild [getParent()=";
		final String parentStr = Objects.toString(getParent());
		final StringBuilder builder = new StringBuilder(+prefix.length()
				+ parentStr.length() + 1);
		builder.append(prefix);
		builder.append(parentStr);
		builder.append(']');
		return builder.toString();
	}

	/**
	 * Checks is a given object is equivalent to this object. This is used by
	 * the method {@link #isEquivalentTo(Object)}.
	 * 
	 * @param other
	 *            The object to check for equality.
	 * @return If the object is equal to this one.
	 */
	private boolean isEquivalentTo(final BasicMutableChild<?> other) {
		return Objects.equals(getParent(), other.getParent());
	}

}
