package com.gecko.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gecko.Server;
import com.gecko.task.Task;

/**
 * PluginManager.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 7 Jun 2011
 */
public class PluginManager {
	
	/**
	 * A List of registered plug-in's
	 */
	private List<Plugin> registeredPlugins = new ArrayList<Plugin>();
	
	/**
	 * A list of plugins that loop
	 */
	private List<Plugin> loopablePlugins = new ArrayList<Plugin>();
	
	/**
	 * Registers a new plug-in
	 * @param p Plug-in instance
	 */
	public void registerNewPlugin(Plugin p) {
		getRegisteredPlugins().add(p);
		if(p.needsLoop())
			getLoopablePlugins().add(p);
	}
	
	public void startLoops() {
		for(final Plugin p : getLoopablePlugins()) {
			Server.getTaskScheduler().schedule(new Task() {

				@Override
				protected void execute() {
					p.loop();
				}
				
			});
		}
	}
	
	/**
	 * Loads all internal plug-ins
	 */
	public void loadInternalPlugins() {
		String directory = "./src/com/gecko/plugin/impl/";
		String[] plugins = new File(directory).list();
		for(String f : plugins) {
			try {
				@SuppressWarnings("unchecked")
				Class <Plugin> e = (Class<Plugin>) Class.forName("com.gecko.plugin.impl."+f.replaceAll(".java", ""));
				registerNewPlugin(e.newInstance());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Server.getServerConfig().logger.info("Loaded " +this.getRegisteredPlugins().size()+ " plugins!");
	}

	/**
	 * @param registeredPlugins the registeredPlugins to set
	 */
	public void setRegisteredPlugins(List<Plugin> registeredPlugins) {
		this.registeredPlugins = registeredPlugins;
	}

	/**
	 * @return the registeredPlugins
	 */
	public List<Plugin> getRegisteredPlugins() {
		return registeredPlugins;
	}

	/**
	 * @param loopablePlugins the loopablePlugins to set
	 */
	public void setLoopablePlugins(List<Plugin> loopablePlugins) {
		this.loopablePlugins = loopablePlugins;
	}

	/**
	 * @return the loopablePlugins
	 */
	public List<Plugin> getLoopablePlugins() {
		return loopablePlugins;
	}

}