package com.gecko.model.world.def;

import java.util.ArrayList;
import java.util.List;

/**
 * WorldListBuilder.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 4 Jun 2011
 */
public class WorldListBuilder {
	
	/**
	 * An array list holding the world list
	 */
	public List<WorldDef> worldList = new ArrayList<WorldDef>();
	
	/**
	 * Builds the world lists
	 */
	public void buildWorlds() {
		for(WorldDef def : worldList) {
			def.flag = def.loc.opcode;
		}
	}

}