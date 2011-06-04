package com.gecko.path.impl;

import com.gecko.model.Location;
import com.gecko.path.*;

/**
 * A {@link PathFinder} which generates paths in a smart condition which
 * derives from start to end locations (if possible).
 * @author Thomas Nappo
 */
public class SmartPathFinder implements PathFinder {

	@Override
	public Path find(Location start, Location end) {
		final Path build = new Path(start, end);
		return build;
	}

}