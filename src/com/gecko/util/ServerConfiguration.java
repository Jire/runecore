package com.gecko.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gecko.model.world.def.WorldDef;

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
	 * An ArrayList of world definitions
	 */
	public List<WorldDef> world = new ArrayList<WorldDef>();
		
	/**
	 * Logger instance
	 */
	public Logger logger = Logger.getLogger(ServerConfiguration.class.getName());
	
	/**
	 * Sets up the server
	 */
	public void setupServer() {
		call("start.configXStream", this);
		//call("start.readWorldList", this);
	}
	
	public void setWorldList() throws FileNotFoundException{
		xStream.getxStreamInstance().fromXML(new FileInputStream("./cfg/worlds.XML"));
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
}