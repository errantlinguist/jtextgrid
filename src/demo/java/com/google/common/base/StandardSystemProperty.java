/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.common.base;

/**
 * <p>
 * Represents a {@linkplain System#getProperties() standard system property}
 * </p>
 * <p>
 * <strong>NOTE:</strong> Copied from Guava source code (version 15.0).
 * </p>
 * 
 * @author Kurt Alfred Kluever
 * @see <a
 *      href="https://code.google.com/p/guava-libraries/source/browse/guava/src/com/google/common/base/StandardSystemProperty.java">The
 *      original Guava source code</a>
 * @since 15.0
 */
public enum StandardSystemProperty {

	/** File separator ("/" on UNIX). */
	FILE_SEPARATOR("file.separator"),

	/** Java class path. */
	JAVA_CLASS_PATH("java.class.path"),

	/** Java class format version number. */
	JAVA_CLASS_VERSION("java.class.version"),

	/** Name of JIT compiler to use. */
	JAVA_COMPILER("java.compiler"),

	/** Path of extension directory or directories. */
	JAVA_EXT_DIRS("java.ext.dirs"),

	/** Java installation directory. */
	JAVA_HOME("java.home"),

	/** Default temp file path. */
	JAVA_IO_TMPDIR("java.io.tmpdir"),

	/** List of paths to search when loading libraries. */
	JAVA_LIBRARY_PATH("java.library.path"),

	/** Java Runtime Environment specification name. */
	JAVA_SPECIFICATION_NAME("java.specification.name"),

	/** Java Runtime Environment specification vendor. */
	JAVA_SPECIFICATION_VENDOR("java.specification.vendor"),

	/** Java Runtime Environment specification version. */
	JAVA_SPECIFICATION_VERSION("java.specification.version"),

	/** Java Runtime Environment vendor. */
	JAVA_VENDOR("java.vendor"),

	/** Java vendor URL. */
	JAVA_VENDOR_URL("java.vendor.url"),

	/** Java Runtime Environment version. */
	JAVA_VERSION("java.version"),

	/** Java Virtual Machine implementation name. */
	JAVA_VM_NAME("java.vm.name"),

	/** Java Virtual Machine specification name. */
	JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),

	/** Java Virtual Machine specification vendor. */
	JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),

	/** Java Virtual Machine specification version. */
	JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),

	/** Java Virtual Machine implementation vendor. */
	JAVA_VM_VENDOR("java.vm.vendor"),

	/** Java Virtual Machine implementation version. */
	JAVA_VM_VERSION("java.vm.version"),

	/** Line separator ("\n" on UNIX). */
	LINE_SEPARATOR("line.separator"),

	/** Operating system architecture. */
	OS_ARCH("os.arch"),

	/** Operating system name. */
	OS_NAME("os.name"),

	/** Operating system version. */
	OS_VERSION("os.version"),

	/** Path separator (":" on UNIX). */
	PATH_SEPARATOR("path.separator"),

	/** User's current working directory. */
	USER_DIR("user.dir"),

	/** User's home directory. */
	USER_HOME("user.home"),

	/** User's account name. */
	USER_NAME("user.name");

	private final String key;

	private StandardSystemProperty(final String key) {
		this.key = key;
	}

	/**
	 * Returns the key used to lookup this system property.
	 */
	public String key() {
		return key;
	}

	/**
	 * Returns a string representation of this system property.
	 */
	@Override
	public String toString() {
		return key() + "=" + value();
	}

	/**
	 * Returns the current value for this system property by delegating to
	 * {@link System#getProperty(String)}.
	 */
	public String value() {
		return System.getProperty(key);
	}
}
