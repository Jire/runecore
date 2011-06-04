package com.gecko.path;

import com.gecko.model.Location;

/**
 * Generates a {@link Path}
 * @author Thomas Nappo
 */
public interface PathFinder {
	
	/**
	 * Builds a new {@link Path} from two points.
	 * @param start The start location.
	 * @param end The end location.
	 * @return The new builded path or <tt>null</tt> if path generation failed.
	 */
	public Path find(Location start, Location end);

}