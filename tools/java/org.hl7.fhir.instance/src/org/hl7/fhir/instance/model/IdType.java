/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

*/
package org.hl7.fhir.instance.model;

import static org.apache.commons.lang3.StringUtils.*;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
import org.hl7.fhir.instance.model.api.IIdType;

/**
 * Primitive type "id" in FHIR: a string from 1 to 64 characters, only containing letters, digits, "-" and "."
 */
@DatatypeDef(name = "id")
public class IdType extends UriType implements IIdType {

	public static final int MAX_LENGTH = 64;
	private static final long serialVersionUID = 4L;
	
	private String myBaseUrl;
	private boolean myHaveComponentParts;
	private String myResourceType;
	private String myUnqualifiedId;
	private String myUnqualifiedVersionId;
	private volatile String myValue;

	/**
	 * Create a new empty ID
	 */
	public IdType() {
		super();
	}

	/**
	 * Create a new ID, using a BigDecimal input. Uses {@link BigDecimal#toPlainString()} to generate the string representation.
	 */
	public IdType(BigDecimal thePid) {
		if (thePid != null) {
			setValue(toPlainStringWithNpeThrowIfNeeded(thePid));
		} else {
			setValue(null);
		}
	}

	/**
	 * Create a new ID using a long
	 */
	public IdType(long theId) {
		setValue(Long.toString(theId));
	}

	/**
	 * Create a new ID using a string. This String may contain a simple ID (e.g. "1234") or it may contain a complete URL (http://example.com/fhir/Patient/1234).
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length
	 * limit of 36 characters.
	 * </p>
	 * <p>
	 * regex: [a-z0-9\-\.]{1,36}
	 * </p>
	 */
	public IdType(String theValue) {
		setValue(theValue);
	}

	/**
	 * Constructor
	 * 
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theIdPart
	 *            The ID (e.g. "123")
	 */
	public IdType(String theResourceType, BigDecimal theIdPart) {
		this(theResourceType, toPlainStringWithNpeThrowIfNeeded(theIdPart));
	}

	/**
	 * Constructor
	 * 
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theIdPart
	 *            The ID (e.g. "123")
	 */
	public IdType(String theResourceType, Long theIdPart) {
		this(theResourceType, toPlainStringWithNpeThrowIfNeeded(theIdPart));
	}

	/**
	 * Constructor
	 *
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theId
	 *            The ID (e.g. "123")
	 */
	public IdType(String theResourceType, String theId) {
		this(theResourceType, theId, null);
	}

	/**
	 * Constructor
	 * 
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theId
	 *            The ID (e.g. "123")
	 * @param theVersionId
	 *            The version ID ("e.g. "456")
	 */
	public IdType(String theResourceType, String theId, String theVersionId) {
		this(null,theResourceType,theId,theVersionId);
	}

	/**
	 * Constructor
	 * 
	 * @param theBaseUrl
	 * 	The server base URL (e.g. "http://example.com/fhir")
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theId
	 *            The ID (e.g. "123")
	 * @param theVersionId
	 *            The version ID ("e.g. "456")
	 */
	public IdType(String theBaseUrl, String theResourceType, String theId, String theVersionId) {
		myBaseUrl = theBaseUrl;
		myResourceType = theResourceType;
		myUnqualifiedId = theId;
		myUnqualifiedVersionId = StringUtils.defaultIfBlank(theVersionId, null);
		myHaveComponentParts = true;
	}

	/**
	 * Creates an ID based on a given URL
	 */
	public IdType(UriType theUrl) {
		setValue(theUrl.getValueAsString());
	}

	/**
	 * @deprecated Use {@link #getIdPartAsBigDecimal()} instead (this method was deprocated because its name is ambiguous)
	 */
	@Deprecated
	public BigDecimal asBigDecimal() {
		return getIdPartAsBigDecimal();
	}

	/**
	 * Returns true if this IdDt matches the given IdDt in terms of resource type and ID, but ignores the URL base
	 */
	@SuppressWarnings("deprecation")
	public boolean equalsIgnoreBase(IdType theId) {
		if (theId == null) {
			return false;
		}
		if (theId.isEmpty()) {
			return isEmpty();
		}
		return ObjectUtils.equals(getResourceType(), theId.getResourceType()) && ObjectUtils.equals(getIdPart(), theId.getIdPart()) && ObjectUtils.equals(getVersionIdPart(), theId.getVersionIdPart());
	}


	
	@Override
	public boolean equals(Object theArg0) {
		if (!(theArg0 instanceof IdType)) {
			return false;
		}
		IdType id = (IdType)theArg0;
		return StringUtils.equals(getValueAsString(), id.getValueAsString());
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getValueAsString());
		return b.toHashCode();
	}

	/**
	 * Returns the portion of this resource ID which corresponds to the server base URL. For example given the resource ID <code>http://example.com/fhir/Patient/123</code> the base URL would be
	 * <code>http://example.com/fhir</code>.
	 * <p>
	 * This method may return null if the ID contains no base (e.g. "Patient/123")
	 * </p>
	 */
	public String getBaseUrl() {
		return myBaseUrl;
	}

	public String getIdPart() {
		return myUnqualifiedId;
	}

	/**
	 * Returns the unqualified portion of this ID as a big decimal, or <code>null</code> if the value is null
	 * 
	 * @throws NumberFormatException
	 *             If the value is not a valid BigDecimal
	 */
	public BigDecimal getIdPartAsBigDecimal() {
		String val = getIdPart();
		if (isBlank(val)) {
			return null;
		}
		return new BigDecimal(val);
	}

	/**
	 * Returns the unqualified portion of this ID as a {@link Long}, or <code>null</code> if the value is null
	 * 
	 * @throws NumberFormatException
	 *             If the value is not a valid Long
	 */
	public Long getIdPartAsLong() {
		String val = getIdPart();
		if (isBlank(val)) {
			return null;
		}
		return Long.parseLong(val);
	}

	public String getResourceType() {
		return myResourceType;
	}

	/**
	 * Returns the value of this ID. Note that this value may be a fully qualified URL, a relative/partial URL, or a simple ID. Use {@link #getIdPart()} to get just the ID portion.
	 * 
	 * @see #getIdPart()
	 */
	@Override
	public String getValue() {
		if (myValue == null && myHaveComponentParts) {
			StringBuilder b = new StringBuilder();
			if (isNotBlank(myBaseUrl)) {
				b.append(myBaseUrl);
				if (myBaseUrl.charAt(myBaseUrl.length()-1)!='/') {
					b.append('/');
				}
			}
			
			if (isNotBlank(myResourceType)) {
				b.append(myResourceType);
			}
			
			if (b.length() > 0) {
				b.append('/');
			}
			
			b.append(myUnqualifiedId);
			if (isNotBlank(myUnqualifiedVersionId)) {
				b.append("/_history/");
				b.append(myUnqualifiedVersionId);
			}
			String value = b.toString();
			myValue = value;
		}
		return myValue;
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	public String getVersionIdPart() {
		return myUnqualifiedVersionId;
	}

	public Long getVersionIdPartAsLong() {
		if (!hasVersionIdPart()) {
			return null;
		} else {
			return Long.parseLong(getVersionIdPart());
		}
	}

	/**
	 * Returns true if this ID has a base url
	 * 
	 * @see #getBaseUrl()
	 */
	public boolean hasBaseUrl() {
		return isNotBlank(myBaseUrl);
	}

	public boolean hasIdPart() {
		return isNotBlank(getIdPart());
	}

	public boolean hasResourceType() {
		return isNotBlank(myResourceType);
	}

	public boolean hasVersionIdPart() {
		return isNotBlank(getVersionIdPart());
	}

	/**
	 * Returns <code>true</code> if this ID contains an absolute URL (in other words, a URL starting with "http://" or "https://"
	 */
	public boolean isAbsolute() {
		if (StringUtils.isBlank(getValue())) {
			return false;
		}
		return isAbsolute(getValue());
	}

	/**
	 * Returns <code>true</code> if the unqualified ID is a valid {@link Long} value (in other words, it consists only of digits)
	 */
	public boolean isIdPartValidLong() {
		String id = getIdPart();
		if (StringUtils.isBlank(id)) {
			return false;
		}
		for (int i = 0; i < id.length(); i++) {
			if (Character.isDigit(id.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns <code>true</code> if the ID is a local reference (in other words, it begins with the '#' character)
	 */
	public boolean isLocal() {
		return myUnqualifiedId != null && myUnqualifiedId.isEmpty() == false && myUnqualifiedId.charAt(0) == '#';
	}

	/**
	 * Copies the value from the given IdDt to <code>this</code> IdDt. It is generally not neccesary to use this method but it is provided for consistency with the rest of the API.
	 */
	public void setId(IdType theId) {
		setValue(theId.getValue());
	}

	/**
	 * Set the value
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length
	 * limit of 36 characters.
	 * </p>
	 * <p>
	 * regex: [a-z0-9\-\.]{1,36}
	 * </p>
	 */
	@Override
	public IdType setValue(String theValue) {
		// TODO: add validation
		myValue = theValue;
		myHaveComponentParts = false;
		if (StringUtils.isBlank(theValue)) {
			myBaseUrl = null;
			myValue = null;
			myUnqualifiedId = null;
			myUnqualifiedVersionId = null;
			myResourceType = null;
		} else if (theValue.charAt(0)== '#') {
			myValue = theValue;
			myUnqualifiedId = theValue;
			myUnqualifiedVersionId=null;
			myResourceType = null;
			myHaveComponentParts = true;
		} else {
			int vidIndex = theValue.indexOf("/_history/");
			int idIndex;
			if (vidIndex != -1) {
				myUnqualifiedVersionId = theValue.substring(vidIndex + "/_history/".length());
				idIndex = theValue.lastIndexOf('/', vidIndex - 1);
				myUnqualifiedId = theValue.substring(idIndex + 1, vidIndex);
			} else {
				idIndex = theValue.lastIndexOf('/');
				myUnqualifiedId = theValue.substring(idIndex + 1);
				myUnqualifiedVersionId = null;
			}

			myBaseUrl = null;
			if (idIndex <= 0) {
				myResourceType = null;
			} else {
				int typeIndex = theValue.lastIndexOf('/', idIndex - 1);
				if (typeIndex == -1) {
					myResourceType = theValue.substring(0, idIndex);
				} else {
					myResourceType = theValue.substring(typeIndex + 1, idIndex);

					if (typeIndex > 4) {
						myBaseUrl = theValue.substring(0, typeIndex);
					}

				}
			}

		}
		return this;
	}

	/**
	 * Set the value
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length
	 * limit of 36 characters.
	 * </p>
	 * <p>
	 * regex: [a-z0-9\-\.]{1,36}
	 * </p>
	 */
	@Override
	public void setValueAsString(String theValue) {
		setValue(theValue);
	}

	@Override
	public String toString() {
		return getValue();
	}

	/**
	 * Returns a new IdDt containing this IdDt's values but with no server base URL if one 
	 * is present in this IdDt. For example, if this IdDt contains the ID "http://foo/Patient/1",
	 * this method will return a new IdDt containing ID "Patient/1". 
	 */
	public IdType toUnqualified() {
		return new IdType(getResourceType(), getIdPart(), getVersionIdPart());
	}

	public IdType toUnqualifiedVersionless() {
		return new IdType(getResourceType(), getIdPart());
	}

	public IdType toVersionless() {
		return new IdType(getBaseUrl(), getResourceType(), getIdPart(), null);
	}

	public IdType withResourceType(String theResourceName) {
		return new IdType(theResourceName, getIdPart(), getVersionIdPart());
	}

	/**
	 * Returns a view of this ID as a fully qualified URL, given a server base and resource name (which will only be used if the ID does not already contain those respective parts). Essentially,
	 * because IdDt can contain either a complete URL or a partial one (or even jut a simple ID), this method may be used to translate into a complete URL.
	 * 
	 * @param theServerBase
	 *            The server base (e.g. "http://example.com/fhir")
	 * @param theResourceType
	 *            The resource name (e.g. "Patient")
	 * @return A fully qualified URL for this ID (e.g. "http://example.com/fhir/Patient/1")
	 */
	public IdType withServerBase(String theServerBase, String theResourceType) {
		return new IdType(theServerBase, theResourceType, getIdPart(), getVersionIdPart());
	}

	/**
	 * Creates a new instance of this ID which is identical, but refers to the specific version of this resource ID noted by theVersion.
	 * 
	 * @param theVersion
	 *            The actual version string, e.g. "1"
	 * @return A new instance of IdDt which is identical, but refers to the specific version of this resource ID noted by theVersion.
	 */
	public IdType withVersion(String theVersion) {
		Validate.notBlank(theVersion, "Version may not be null or empty");

		String existingValue = getValue();

		int i = existingValue.indexOf("_history");
		String value;
		if (i > 1) {
			value = existingValue.substring(0, i - 1);
		} else {
			value = existingValue;
		}

		return new IdType(value + "/_history/" + theVersion);
	}

	private static String toPlainStringWithNpeThrowIfNeeded(BigDecimal theIdPart) {
		if (theIdPart == null) {
			throw new NullPointerException("BigDecimal ID can not be null");
		}
		return theIdPart.toPlainString();
	}

	private static String toPlainStringWithNpeThrowIfNeeded(Long theIdPart) {
		if (theIdPart == null) {
			throw new NullPointerException("Long ID can not be null");
		}
		return theIdPart.toString();
	}

	@Override
	public boolean isEmpty() {
		return isBlank(getValue());
	}

	@Override
	public IdType copy() {
		return new IdType(getValue());
	}

	
	private static boolean isAbsolute(String theValue) {
		String value = theValue.toLowerCase();
		return value.startsWith("http://") || value.startsWith("https://");
	}

	
}