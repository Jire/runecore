package com.gecko.util;

import com.gecko.model.Location;

/**
 * Misc.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 7 Jun 2011
 */
public class Misc {
	
	/**
	 * Gets the distance between 2 locations
	 * @param loc The first location
	 * @param loc2 The second location
	 * @return The distance between the 2 locations
	 */
	public static int distance(Location loc, Location loc2) {
		int deltaX = loc2.getX() - loc.getX();
		int deltaY = loc2.getY() - loc.getY();
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}
	
	/**
	 * Gets a random int
	 * @param rand
	 * @return
	 */
	public static int random(int rand) {
		return (int) (Math.random() * (rand + 1));
	}

}
