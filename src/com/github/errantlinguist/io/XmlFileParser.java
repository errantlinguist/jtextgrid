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

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A {@link FileParser} implementation which parses a single line of well-formed
 * XML and returns it as a new {@link Document} object.
 * 
 * @author Todd Shore
 * @version 2011-08-04
 * @since 2011-07-06
 * 
 */
public class XmlFileParser implements FileParser<Document> {

	/**
	 * The encoding to default to.
	 */
	private static final String DEFAULT_ENCODING = "utf-8";

	/**
	 * The XML version to default to.
	 */
	private static final double DEFAULT_XML_VERSION = 1.0;

	/**
	 * A {@link Pattern} matching either an empty {@link String} or one composed
	 * of only whitespaces.
	 */
	private static final Pattern EMPTY_PATTERN = Pattern.compile("\\s*");

	/**
	 * A constant value used for estimating the length of the string
	 * representation of the object returned by {@link #toString()}.
	 */
	protected static final int ESTIMATED_STRING_LENGTH = 32;

	/**
	 * Creates an XML declaration for appending to XML {@link String strings}
	 * which do not feature one.
	 * 
	 * @param xmlVersion
	 *            The XML version with which an input {@link String} is
	 *            compliant.
	 * @param encoding
	 *            The encoding scheme used to encode an input XML
	 *            <code>String</code>.
	 * @return A <code>String</code> denoting an XML declaration, e.g.&nbsp;
	 *         {@code <?xml version="1.0" encoding="utf-8"?>}.
	 */
	private static String makeXMLDeclaration(final double xmlVersion,
			final String encoding) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"");
		sb.append(Double.toString(xmlVersion));
		sb.append("\" encoding=\"");
		sb.append(encoding);
		sb.append("\"?>");

		return sb.toString();
	}

	private final DocumentBuilder db;

	private final DocumentBuilderFactory dbf;

	/**
	 * The pre-cached hash code.
	 */
	private final int hashCode;

	private final InputSource inSource;

	private final String xmlDeclaration;

	/**
	 * 
	 * @throws ParserConfigurationException
	 *             If there is an error when creating a new
	 *             {@link DocumentBuilder} object for parsing XML.
	 */
	public XmlFileParser() throws ParserConfigurationException {
		this(DEFAULT_XML_VERSION, DEFAULT_ENCODING);
	}

	/**
	 * 
	 * @param xmlVersion
	 *            The XML version with which an input {@link String} is
	 *            compliant.
	 * @param encoding
	 *            The encoding scheme used to encode an input XML
	 *            <code>String</code>.
	 * @throws ParserConfigurationException
	 *             If there is an error when creating a new
	 *             {@link DocumentBuilder} object for parsing XML.
	 */
	public XmlFileParser(final double xmlVersion, final String encoding)
			throws ParserConfigurationException {
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		inSource = new InputSource();

		db = dbf.newDocumentBuilder();

		xmlDeclaration = makeXMLDeclaration(xmlVersion, encoding);

		hashCode = calculateHashCode();
	}

	/**
	 * 
	 * @param xmlVersion
	 *            The XML version with which an input {@link String} is
	 *            compliant.
	 * @param encoding
	 *            The encoding scheme used to encode an input XML
	 *            <code>String</code>.
	 * @param schema
	 *            A {@link Schema} object representing the XML schema to
	 *            validate the input against.
	 * @throws ParserConfigurationException
	 *             If there is an error when creating a new
	 *             {@link DocumentBuilder} object for parsing XML.
	 */
	public XmlFileParser(final double xmlVersion, final String encoding,
			final Schema schema) throws ParserConfigurationException {
		this(xmlVersion, encoding);
		dbf.setSchema(schema);

	}

	/**
	 * 
	 * @param schema
	 *            A {@link Schema} object representing the XML schema to
	 *            validate the input against.
	 * @throws ParserConfigurationException
	 *             If there is an error when creating a new
	 *             {@link DocumentBuilder} object for parsing XML.
	 */
	public XmlFileParser(final Schema schema)
			throws ParserConfigurationException {
		this(DEFAULT_XML_VERSION, DEFAULT_ENCODING);
		dbf.setSchema(schema);
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
		if (!(obj instanceof XmlFileParser)) {
			return false;
		}
		final XmlFileParser other = (XmlFileParser) obj;
		if (xmlDeclaration == null) {
			if (other.xmlDeclaration != null) {
				return false;
			}
		} else if (!xmlDeclaration.equals(other.xmlDeclaration)) {
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
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uni_saarland.lsv.dlr.io.FileParser#parse(java.lang.String)
	 */
	@Override
	public Document parse(final String line) throws SAXException, IOException {

		final Document newDoc;

		if (EMPTY_PATTERN.matcher(line).matches()) {
			newDoc = db.newDocument();
		} else {

			final StringBuilder sb = new StringBuilder();
			sb.append(xmlDeclaration);
			sb.append(line);

			inSource.setCharacterStream(new StringReader(sb.toString()));

			newDoc = db.parse(inSource);
		}

		return newDoc;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(ESTIMATED_STRING_LENGTH);
		builder.append(this.getClass().getSimpleName());
		builder.append("[xmlDeclaration=");
		builder.append(xmlDeclaration);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * 
	 * @return The hash code.
	 */
	private int calculateHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (xmlDeclaration == null ? 0 : xmlDeclaration.hashCode());
		return result;
	}

}
