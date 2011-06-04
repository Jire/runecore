package com.gecko.model;

/**
 * A which can be interacted with and holds a grid point.
 * @author Thomas Nappo
 */
public abstract class Entity {
	
	/**
	 * The index interaction id.
	 */
	protected int index;
	
	/**
	 * Gets the formatted client index.
	 * @return {@link #index} formatted for client interaction id use.
	 */
	public abstract int getFormattedIndex();
	
	/**
	 * Gets the entity's index.
	 * @return The enitty's {@link #index}
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * The entity's tile location on the map grid.
	 */
	protected Location location = Location.create(3222, 3222);
	
	/**
	 * Gets the entity's tile location on the map grid.
	 * @return The entity's {@link #location}
	 */
	public Location getLocation() {
		return location;
	}

}