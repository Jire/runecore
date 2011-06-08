package com.gecko.model.mask.walking;

import java.util.Deque;
import java.util.LinkedList;

import com.gecko.Constants;
import com.gecko.model.Entity;
import com.gecko.model.Location;
import com.gecko.model.Player;

/**
 * WalkingQueue.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 7 Jun 2011
 */
public class WalkingQueue {
	/**
	 * Represents a single point in the queue.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	private static class Point {

		/**
		 * The x-coordinate.
		 */
		private final int x;

		/**
		 * The y-coordinate.
		 */
		private final int y;

		/**
		 * The direction to walk to this point.
		 */
		private final int dir;

		/**
		 * Creates a point.
		 * 
		 * @param x
		 *            X coord.
		 * @param y
		 *            Y coord.
		 * @param dir
		 *            Direction to walk to this point.
		 */
		public Point(int x, int y, int dir) {
			this.x = x;
			this.y = y;
			this.dir = dir;
		}

	}

	/**
	 * The maximum size of the queue. If there are more points than this size,
	 * they are discarded.
	 */
	public static final int MAXIMUM_SIZE = 50;

	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * The queue of waypoints.
	 */
	private final Deque<Point> waypoints = new LinkedList<Point>();

	/**
	 * Run toggle (button in client).
	 */
	private boolean runToggled = false;

	/**
	 * Run for this queue (CTRL-CLICK) toggle.
	 */
	private boolean runQueue = false;

	/**
	 * Creates the <code>WalkingQueue</code> for the specified
	 * <code>Entity</code>.
	 * 
	 * @param entity
	 *            The entity whose walking queue this is.
	 */
	public WalkingQueue(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Sets the run toggled flag.
	 * 
	 * @param runToggled
	 *            The run toggled flag.
	 */
	public void setRunningToggled(boolean runToggled) {
		this.runToggled = runToggled;
	}

	/**
	 * Sets the run queue flag.
	 * 
	 * @param runQueue
	 *            The run queue flag.
	 */
	public void setRunningQueue(boolean runQueue) {
		this.runQueue = runQueue;
	}

	/**
	 * Gets the run toggled flag.
	 * 
	 * @return The run toggled flag.
	 */
	public boolean isRunningToggled() {
		return runToggled;
	}

	/**
	 * Gets the running queue flag.
	 * 
	 * @return The running queue flag.
	 */
	public boolean isRunningQueue() {
		return runQueue;
	}

	/**
	 * Checks if any running flag is set.
	 * 
	 * @return <code>true</code. if so, <code>false</code> if not.
	 */
	public boolean isRunning() {
		return runToggled || runQueue;
	}

	/**
	 * Resets the walking queue so it contains no more steps.
	 */
	public void reset() {
		runQueue = false;
		waypoints.clear();
		waypoints.add(new Point(entity.getLocation().getX(), entity
				.getLocation().getY(), -1));
	}

	/**
	 * Checks if the queue is empty.
	 * 
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isEmpty() {
		return waypoints.isEmpty();
	}

	/**
	 * Removes the first waypoint which is only used for calculating directions.
	 * This means walking begins at the correct time.
	 */
	public void finish() {
		waypoints.removeFirst();
	}

	/**
	 * Adds a single step to the walking queue, filling in the points to the
	 * previous point in the queue if necessary.
	 * 
	 * @param x
	 *            The local x coordinate.
	 * @param y
	 *            The local y coordinate.
	 */
	public void addStep(int x, int y) {
		/*
		 * The RuneScape client will not send all the points in the queue. It
		 * just sends places where the direction changes.
		 * 
		 * For instance, walking from a route like this:
		 * 
		 * <code> ***** * * ***** </code>
		 * 
		 * Only the places marked with X will be sent:
		 * 
		 * <code> X***X * * X***X </code>
		 * 
		 * This code will 'fill in' these points and then add them to the queue.
		 */

		/*
		 * We need to know the previous point to fill in the path.
		 */
		if (waypoints.size() == 0) {
			/*
			 * There is no last point, reset the queue to add the player's
			 * current location.
			 */
			reset();
		}

		/*
		 * We retrieve the previous point here.
		 */
		final Point last = waypoints.peekLast();

		/*
		 * We now work out the difference between the points.
		 */
		int diffX = x - last.x;
		int diffY = y - last.y;

		/*
		 * And calculate the number of steps there is between the points.
		 */
		final int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
			/*
			 * Keep lowering the differences until they reach 0 - when our route
			 * will be complete.
			 */
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}

			/*
			 * Add this next step to the queue.
			 */
			addStepInternal(x - diffX, y - diffY);
		}
	}

	/**
	 * Adds a single step to the queue internally without counting gaps. This
	 * method is unsafe if used incorrectly so it is private to protect the
	 * queue.
	 * 
	 * @param x
	 *            The x coordinate of the step.
	 * @param y
	 *            The y coordinate of the step.
	 */
	private void addStepInternal(int x, int y) {
		/*
		 * Check if we are going to violate capacity restrictions.
		 */
		if (waypoints.size() >= MAXIMUM_SIZE) {
			/*
			 * If we are we'll just skip the point. The player won't get a
			 * complete route by large routes are not probable and are more
			 * likely sent by bots to crash servers.
			 */
			return;
		}

		/*
		 * We retrieve the previous point (this is to calculate the direction to
		 * move in).
		 */
		final Point last = waypoints.peekLast();

		/*
		 * Now we work out the difference between these steps.
		 */
		final int diffX = x - last.x;
		final int diffY = y - last.y;

		/*
		 * And calculate the direction between them.
		 */
		final int dir = DirectionUtils.direction(diffX, diffY);

		/*
		 * Check if we actually move anywhere.
		 */
		if (dir > -1) {
			/*
			 * We now have the information to add a point to the queue! We
			 * create the actual point object and add it.
			 */
			waypoints.add(new Point(x, y, dir));

		}
	}

	/**
	 * Processes the next player's movement.
	 */
	public void processNextMovement() {
		/*
		 * Store the teleporting flag.
		 */
		final boolean teleporting = entity.hasTeleportTarget();

		/*
		 * The points which we are walking to.
		 */
		Point walkPoint = null, runPoint = null;

		/*
		 * Checks if the player is teleporting i.e. not walking.
		 */
		if (teleporting) {
			/*
			 * Reset the walking queue as it will no longer apply after the
			 * teleport.
			 */
			reset();

			/*
			 * Set the 'teleporting' flag which indicates the player is
			 * teleporting.
			 */
			entity.setTeleporting(true);

			/*
			 * Sets the player's new location to be their target.
			 */
			entity.setLocation(entity.getTeleportTarget());

			/*
			 * Resets the teleport target.
			 */
			entity.resetTeleportTarget();
		} else {
			/*
			 * If the player isn't teleporting, they are walking (or standing
			 * still). We get the next direction of movement here.
			 */
			walkPoint = getNextPoint();

			/*
			 * Technically we should check for running here.
			 */
			/*
			 * Technically we should check for running here.
			 */
			if (runToggled || runQueue) {
				boolean canRun = false;
				if (entity instanceof Player) {
					canRun = ((Player) entity).getRunEnergy() > 0;
				}
				if (canRun) {
					runPoint = getNextPoint();
				}
			}
			if (runPoint != null) {
				if (entity instanceof Player) {
					((Player) entity).setRunEnergy((int) (((Player) entity)
							.getRunEnergy() - 0.88));
				} else {
					if (isRunningToggled()) {
						((Player) entity).getPacketSender().sendConfig(173, 0);
						runToggled = runQueue = false;
					}
				}
			}
			/*
			 * Now set the sprites.
			 */
			final int walkDir = walkPoint == null ? -1 : walkPoint.dir;
			final int runDir = runPoint == null ? -1 : runPoint.dir;
			entity.getSprites().setSprites(walkDir, runDir);
		}

		/*
		 * Gets the last known region.
		 */
		final Location lastRegion = entity.getLastKnownRegion();

		/*
		 * If the last known region isn't nulled it updates the region.
		 */
		if (lastRegion != null) {
			/*
			 * The region position.
			 */
			final int rx = lastRegion.getRegionX();
			final int ry = lastRegion.getRegionY();

			/*
			 * Set the x coordinate region is changing
			 */
			if (rx - entity.getLocation().getRegionX() >= 4) {
				entity.setMapRegionChanging(true);
			} else if (rx - entity.getLocation().getRegionX() <= -4) {
				entity.setMapRegionChanging(true);
			}

			/*
			 * Set the y coordinate region is changing
			 */
			if (ry - entity.getLocation().getRegionY() >= 4) {
				entity.setMapRegionChanging(true);
			} else if (ry - entity.getLocation().getRegionY() <= -4) {
				entity.setMapRegionChanging(true);
			}
		} else {

			/*
			 * Sets the the region is changing.
			 */
			entity.setMapRegionChanging(true);
		}
	}

	/**
	 * Gets the next point of movement.
	 * 
	 * @return The next point.
	 */
	private Point getNextPoint() {
		/*
		 * Take the next point from the queue.
		 */
		final Point p = waypoints.poll();

		/*
		 * Checks if there are no more points.
		 */
		if (p == null || p.dir == -1) {
			/*
			 * Return <code>null</code> indicating no movement happened.
			 */
			return null;
		} else {
			/*
			 * Set the player's new location.
			 */
			final int diffX = Constants.DIRECTION_DELTA_X[p.dir];
			final int diffY = Constants.DIRECTION_DELTA_Y[p.dir];
			entity.setLocation(entity.getLocation().transform(diffX, diffY, 0));
			/*
			 * And return the direction.
			 */
			return p;
		}
	}
}
