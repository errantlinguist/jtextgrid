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

package com.github.errantlinguist.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A filename filter matching a given filename extension.
 * 
 * @author Todd Shore
 * @version 2011-07-06
 * @since 2011-07-06
 * 
 */
public class ExtFilter implements FilenameFilter {
	private final String ext;

	/**
	 * 
	 * @param ext
	 *            The file extension to be matched.
	 */
	public ExtFilter(final String ext) {
		this.ext = ext;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(ext);
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
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ExtFilter)) {
			return false;
		}
		final ExtFilter other = (ExtFilter) obj;
		if (ext == null) {
			if (other.ext != null) {
				return false;
			}
		} else if (!ext.equals(other.ext)) {
			return false;
		}
		return true;
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
		result = prime * result + (ext == null ? 0 : ext.hashCode());
		return result;
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
		builder.append("[ext=");
		builder.append(ext);
		builder.append("]");
		return builder.toString();
	}
}
