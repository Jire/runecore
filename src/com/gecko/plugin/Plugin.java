package com.gecko.plugin;

/**
 * Plugin.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 7 Jun 2011
 */
public interface Plugin {
	
	/**
	 * Invoked on activation of the plug-in
	 */
	public abstract void activation();
	
	/**
	 * Invoked on a server loop
	 */
	public abstract void loop();
	
	/**
	 * If the plug-in needs a loop
	 * @return Whether the plug-in loops or not
	 */
	public abstract boolean needsLoop();
	
	/**
	 * On removal of the plug-in
	 */
	public abstract void remove();

}