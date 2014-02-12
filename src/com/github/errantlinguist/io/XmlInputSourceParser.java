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
package com.github.errantlinguist.io;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.github.errantlinguist.Factory;

/**
 * <p>
 * A {@link Parser} implementation which parses an {@link InputSource}
 * representing well-formed XML and returns it as a new {@link Document} object.
 * </p>
 * <p>
 * <strong>NOTE:</strong> This class is not thread-safe!
 * </p>
 * 
 * @version 2014-02-06
 * @since 2014-02-06
 * @author <a href="mailto:errantlinguist+github@gmail.com">Todd Shore</a>
 * 
 */
public class XmlInputSourceParser implements Factory<Document>,
		Parser<InputSource, Document> {

	/**
	 * A {@link ThreadLocal} object for getting {@link DocumentBuilderFactory}
	 * instances in a thread-safe manner for creating default instances for
	 * {@link #documentBuilder}.
	 */
	private static final ThreadLocal<DocumentBuilderFactory> DEFAULT_DOCUMENT_BUILDER_FACTORY = new ThreadLocal<DocumentBuilderFactory>() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected DocumentBuilderFactory initialValue() {
			final DocumentBuilderFactory result = DocumentBuilderFactory
					.newInstance();
			result.setNamespaceAware(true);
			return result;
		}
	};

	/**
	 * A constant value used for estimating the length of the string
	 * representation of the object returned by {@link #toString()}.
	 */
	private static final int ESTIMATED_STRING_REPR_LENGTH = 64;

	/**
	 * Creates a default {@link DocumentBuilder} object for parsing XML.
	 * 
	 * @return a new {@code DocumentBuilder} object for parsing XML.
	 * @throws ParserConfigurationException
	 *             If there is an error when creating a new
	 *             {@code DocumentBuilder} object for parsing XML.
	 */
	private static final DocumentBuilder createDocumentBuilder()
			throws ParserConfigurationException {
		final DocumentBuilderFactory dbf = DEFAULT_DOCUMENT_BUILDER_FACTORY
				.get();
		return dbf.newDocumentBuilder();
	}

	/**
	 * Creates a {@link DocumentBuilder} object for parsing XML with a given
	 * {@link Schema}.
	 * 
	 * @param schema
	 *            The {@code Schema} instance to be used while parsing with the
	 *            new {@code DocumentBuilder} instance.
	 * @return a new {@code DocumentBuilder} object for parsing XML with the
	 *         given {@code Schema}.
	 * @throws ParserConfigurationException
	 *             If there is an error when creating a new
	 *             {@code DocumentBuilder} object for parsing XML with the given
	 *             {@code Schema}.
	 */
	private static final DocumentBuilder createDocumentBuilder(
			final Schema schema) throws ParserConfigurationException {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setSchema(schema);
		return dbf.newDocumentBuilder();
	}

	/**
	 * The {@link DocumentBuilder} instance used to create new {@link Document}
	 * instances returned by {@link #parse(InputSource)}.
	 */
	private final DocumentBuilder documentBuilder;

	/**
	 * The pre-cached hash code.
	 */
	private final transient int hashCode;

	/**
	 * 
	 * @throws ParserConfigurationException
	 *             If there is an error when creating a new
	 *             {@link DocumentBuilder} object for parsing XML.
	 */
	public XmlInputSourceParser() throws ParserConfigurationException {
		this(createDocumentBuilder());
	}

	/**
	 * 
	 * @param documentBuilder
	 *            The {@link DocumentBuilder} instance used to create new
	 *            {@link Document} instances returned by
	 *            {@link #parse(InputSource)}.
	 */
	public XmlInputSourceParser(final DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;

		hashCode = calculateHashCode();
	}

	/**
	 * 
	 * @param schema
	 *            The {@link Schema} instance to be used while parsing.
	 * @throws ParserConfigurationException
	 */
	public XmlInputSourceParser(final Schema schema)
			throws ParserConfigurationException {
		this(createDocumentBuilder(schema));
	}

	/**
	 * Creates a new, empty {@link Document} object.
	 * 
	 * @return A new {@code Document} object.
	 */
	@Override
	public Document create() {
		return documentBuilder.newDocument();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public Document parse(final InputSource input) throws ParseException {
		final Document result;
		try {
			result = documentBuilder.parse(input);
		} catch (final IOException e) {
			throw new ParseException(e);
		} catch (final SAXException e) {
			throw new ParseException(e);
		}
		return result;
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
		builder.append("XmlInputSourceParser [documentBuilder=");
		builder.append(documentBuilder);
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
		result = (prime * result)
				+ (documentBuilder == null ? 0 : documentBuilder.hashCode());
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
			final XmlInputSourceParser castOther = (XmlInputSourceParser) other;
			result = isEquivalentTo(castOther);
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * Checks is a given object is equivalent to this object.
	 * 
	 * @param other
	 *            The instance of type {@code T} to check for equality.
	 * @return If the object is equal to this one.
	 */
	private boolean isEquivalentTo(final XmlInputSourceParser other) {
		final boolean result;
		if (documentBuilder == null) {
			result = (other.documentBuilder == null);
		} else {
			result = (documentBuilder.equals(other.documentBuilder));
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
	protected boolean isTypeEquivalentTo(final Object other) {
		return (other instanceof XmlInputSourceParser);
	}

}