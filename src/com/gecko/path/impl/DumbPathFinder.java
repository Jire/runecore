package com.gecko.path.impl;

import com.gecko.model.Entity;
import com.gecko.model.Location;
import com.gecko.path.*;

/**
 * A {@link PathFinder} which generates paths in a dumb condition which
 * does not build "around" block points.
 * @author Thomas Nappo
 */
public class DumbPathFinder implements PathFinder {
	
	@Override
	public Path find(Location start, Location end) {
		final Path build = new Path(start, end);
		return build;
	}
	
	/**
	 * Finds a path between one entity and another.
	 * @param entity The entity to start from.
	 * @param to The entity to finish at.
	 * @return The generated {@link Path} or <tt>null</tt> if it failed.
	 */
	public Path find(Entity entity, Entity to) {
		return find(entity.getLocation(), to.getLocation());
	}

}