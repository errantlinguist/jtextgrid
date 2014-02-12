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
package com.github.errantlinguist.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link FilenameFilter} matching a given filename extension.
 * 
 * @since 2011-07-06
 * @version 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 * 
 */
public class FilenameExtensionFilter implements FilenameFilter, Serializable {

	/**
	 * The serial version UID for use in {@link Serializable serialization}.
	 */
	private static final long serialVersionUID = -5623165538918362266L;

	private final String extension;

	/**
	 * The pre-cached hash code.
	 */
	private transient final int hashCode;

	/**
	 * 
	 * @param extension
	 *            The file extension to be matched.
	 */
	public FilenameExtensionFilter(final String extension) {
		this.extension = extension;

		hashCode = calculateHashCode();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(extension);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		final boolean result;

		if (this == obj) {
			result = true;
		} else if (obj == null) {
			result = false;
		} else {
			result = isEquivalentTo(obj);
		}

		return result;
	}

	/**
	 * @return The file extension to be matched.
	 */
	public String getExtension() {
		return extension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String prefix = "ExtFilter [getExtension()=";
		final String extensionStr = Objects.toString(getExtension());
		final StringBuilder builder = new StringBuilder(prefix.length()
				+ extensionStr.length() + 1);
		builder.append(prefix);
		builder.append(extensionStr);
		builder.append(']');
		return builder.toString();
	}

	/**
	 * 
	 * @return The hash code.
	 */
	private int calculateHashCode() {
		final int prime = 31;
		int result = 1;
		final String extension = getExtension();
		result = (prime * result)
				+ (extension == null ? 0 : extension.hashCode());
		return result;
	}

	/**
	 * Checks is a given object is equivalent to this object.
	 * 
	 * @param other
	 *            The object to check for equality.
	 * @return If the object is equal to this one.
	 */
	private boolean isEquivalentTo(final FilenameExtensionFilter other) {
		final boolean result;
		final String extension = getExtension();
		if (extension == null) {
			result = (other.getExtension() == null);
		} else {
			result = (extension.equals(other.getExtension()));
		}
		return result;
	}

	/**
	 * Checks is a given object is equivalent to this object.
	 * 
	 * @param other
	 *            The object to check for equality.
	 * @return If the object is equal to this one.
	 */
	private final boolean isEquivalentTo(final Object other) {
		final boolean result;

		if (isTypeEquivalentTo(other)) {
			final FilenameExtensionFilter castOther = (FilenameExtensionFilter) other;
			result = isEquivalentTo(castOther);
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * Checks if a given object is a subtype of this object.
	 * 
	 * @param other
	 *            The object to check for type equality.
	 * @return If the given object is a subtype of the type of this object.
	 */
	private boolean isTypeEquivalentTo(final Object other) {
		return (other instanceof FilenameExtensionFilter);
	}
}
