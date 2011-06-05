package com.gecko.model.world.def;

/**
 * WorldDef.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 4 Jun 2011
 */
public class WorldDef {
	
	public WorldDef(String name, String address, WorldLocation loc) {
		this.name = name;
		this.address = address;
		this.loc = loc;
		flag = loc.opcode;
	}
	
	public int flag, worldId;
	public String name, address, activity;
	public WorldLocation loc;

}
