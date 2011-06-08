package com.gecko.model;

import com.gecko.path.Tile;

/**
 * A point on the map.
 * @author Thomas Nappo
 */
public class Location extends Tile {
	
	private final int z;
	
	public final int getZ() {
		return z;
	}
	
	public int getLocalX() {
		return getLocalX(this);
	}
	
	public int getLocalY() {
		return getLocalY(this);
	}
	
	public int getLocalX(Location l) {
		return x - 8 * (l.getRegionX() - 6);
	}
	
	public int getLocalY(Location l) {
		return y - 8 * (l.getRegionY() - 6);
	}
	
	public int getRegionX() {
		return x >> 3;
	}
	
	public int getRegionY() {
		return y >> 3;
	}
	
	public Location(int x, int y, int z) {
		super(x, y);
		this.z = z;
	}
	
	public Location(int x, int y) {
		this(x, y, 0);
	}
	
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}
	
	public static Location create(int x, int y) {
		return new Location(x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && ((Location) o).z == z;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	/**
	 * Creates a new location based on this location.
	 * 
	 * @param diffX
	 *            X difference.
	 * @param diffY
	 *            Y difference.
	 * @param diffZ
	 *            Z difference.
	 * @return The new location.
	 */
	public Location transform(int diffX, int diffY, int diffZ) {
		return Location.create(x + diffX, y + diffY, z + diffZ);
	}

}