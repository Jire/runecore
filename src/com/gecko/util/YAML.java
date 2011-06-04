package com.gecko.util;

import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * Easier management utilitie for dealing with YAML.
 * @author Thomas Nappo
 */
public class YAML {
	
	/**
	 * Insider YAML implementation instance.
	 */
	private Yaml yaml = new Yaml(new SafeConstructor());
	
	/**
	 * Converts the YAML to it's original implementation instance.
	 * @return The utilitie's {@link #yaml}
	 */
	public Yaml toYAML() {
		return yaml;
	}
	
	/**
	 * Insider map holding instance.
	 */
	private Map<String, Object> map = new HashMap<String, Object>();
	
	/**
	 * Converts the YAML to a map.
	 * @return The YAML as a <b>String, Object</b> map.
	 */
	public Map<String, Object> toMap() {
		return map;
	}
	
	/**
	 * Appends a YAML load from a file.
	 * @param file The YAML <i>.yml</i> file location to load from.
	 */
	@SuppressWarnings("unchecked")
	public void load(String file) {
		map = (Map<String, Object>) yaml.load(file);
	}
	
	public String get(Object key) {
		return map.get(key).toString();
	}
	
	public YAML() {
		// just do nothing
	}
	
	/**
	 * Constructs a new YAML.
	 * @param file The file to load.
	 */
	public YAML(String file) {
		YAML instance = new YAML();
		instance.load(file);
	}
	
	/**
	 * Builds a new YAML.
	 * @param file The file location to load.
	 * @return The builded YAML.
	 */
	public static YAML build(String file) {
		return new YAML(file);
	}

}