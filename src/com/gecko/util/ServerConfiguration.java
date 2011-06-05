package com.gecko.util;

import java.util.logging.Logger;

import com.gecko.model.world.def.WorldListBuilder;

/**
 * ServerConfiguration.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 4 Jun 2011
 */
public class ServerConfiguration {
	
	/**
	 * The XStream instance
	 */
	public XStreamParser xStream = new XStreamParser();
	
	/**
	 * JavaScript manager
	 */
	public JavaScriptManager jsManager = new JavaScriptManager();
	
	/**
	 * WorldListBuilder instance
	 */
	private WorldListBuilder worldListBuilder = new WorldListBuilder();
		
	/**
	 * Logger instance
	 */
	public Logger logger = Logger.getLogger(ServerConfiguration.class.getName());
	
	/**
	 * Sets up the server
	 */
	public void setupServer() {
		call("start.configXStream", this);
		call("start.readWorldList", this);
	}	
	
	/**
	 * Calls a script
	 * @param script the script name
	 * @param args the argumenets
	 * @return
	 */
	public Object call(String script, Object... args) {
		return jsManager.call(script, args);
	}

	/**
	 * @param worldListBuilder the worldListBuilder to set
	 */
	public void setWorldListBuilder(WorldListBuilder worldListBuilder) {
		this.worldListBuilder = worldListBuilder;
	}

	/**
	 * @return the worldListBuilder
	 */
	public WorldListBuilder getWorldListBuilder() {
		return worldListBuilder;
	}
}