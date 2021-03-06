/**
 * (C) Copyright IBM Corp. 2006, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, ebak@de.ibm.com  
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sblim.cimclient.internal.cimxml.sax.node;

import javax.cim.CIMInstance;

import org.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT RESPONSEDESTINATION (INSTANCE)
 */

public class ResponseDestinationNode extends Node {

	// INSTANCE
	private CIMInstance iCIMInst;

	/**
	 * Ctor.
	 */
	public ResponseDestinationNode() {
		super(RESPONSEDESTINATION);
	}

	@Override
	public void childParsed(Node pChild) {
		this.iCIMInst = ((InstanceNode) pChild).getCIMInstance();
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iCIMInst = null;
		// no attribs
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (this.iCIMInst != null) throw new SAXException(getNodeName()
				+ " node can have only one child node!");
		if (pNodeNameEnum != INSTANCE) throw new SAXException(getNodeName()
				+ " node's child is not INSTANCE. " + pNodeNameEnum + " child node is invalid!");
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iCIMInst == null) throw new SAXException(getNodeName()
				+ " node must have an INSTANCE child node!");
	}

}
