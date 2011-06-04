package com.gecko.util;

import com.gecko.model.world.def.WorldDef;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * XStreamParser.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 4 Jun 2011
 */
public class XStreamParser {
	
	/**
	 * The XStream Packet
	 */
	private XStream xStreamInstance;
	
	/**
	 * Starts up the XStream parser
	 */
	public void createParser() {
		setxStreamInstance(new XStream(new DomDriver()));
	}
	
	/**
	 * Sets up the parser alias's
	 */
	public void setupParser() {
		getxStreamInstance().alias("worldDef", WorldDef.class);
	}

	/**
	 * @param xStreamInstance the xStreamInstance to set
	 */
	public void setxStreamInstance(XStream xStreamInstance) {
		this.xStreamInstance = xStreamInstance;
	}

	/**
	 * @return the xStreamInstance
	 */
	public XStream getxStreamInstance() {
		return xStreamInstance;
	}
	
}