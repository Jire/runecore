package com.gecko.path;

/**
 * Represents a game tile.
 * @author Thomas Nappo
 */
public class Tile {
	
	protected final int x, y;
	
	public final int getX() {
		return x;
	}
	
	public final int getY() {
		return y;
	}
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static Tile create(int x, int y) {
		return new Tile(x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Tile)) return false;
		Tile ot = (Tile) o;
		return ot.x == x && ot.y == y;
	}

}