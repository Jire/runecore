package com.gecko.model;

import com.gecko.model.mask.UpdateFlags;
import com.gecko.model.mask.walking.Sprites;
import com.gecko.model.mask.walking.WalkingQueue;

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
	 * 
	 * @param id
	 */
	public void setIndex(int id) {
		index = id;
	}	
	
	/**
	 * The entity's tile location on the map grid.
	 */
	protected Location location = Location.create(3222, 3222);
	
	/**
	 * Walking queue for entity
	 */
	private WalkingQueue walkingQueue = new WalkingQueue(this);
	
	/**
	 * The entity's teleport target
	 */
	private Location teleportTarget = null;
	
	/**
	 * The entity's update flags
	 */
	private UpdateFlags updateFlags = new UpdateFlags();
	
	/**
	 * The entity's walking directions
	 */
	private Sprites sprites = new Sprites();
	
	/**
	 * Is the entity teleporting?
	 */
	private boolean isTeleporting;
	
	/**
	 * The entity's last know region
	 */
	private Location lastKnownRegion;
	
	/**
	 * 
	 */
	private boolean mapRegionChanging;
	
	/**
	 * Gets the entity's tile location on the map grid.
	 * @return The entity's {@link #location}
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Does the entity have a teleport target?
	 * @return If it has a tele target
	 */
	public boolean hasTeleportTarget() {
		return getTeleportTarget() != null;
	}
	
	/**
	 * Resets the teleport target
	 */
	public void resetTeleportTarget() {
		setTeleportTarget(null);
	}
	
	/**
	 * Sets the entity's location
	 * @param loc The location of the entity to set!
	 */
	public void setLocation(Location loc) {
		this.location = loc;
	}
	
	/**
	 * Returns an Entity as a type in object form
	 * @param e EntityType of the Entity
	 * @return The entity as it's true form
	 */
	public Object asType(EntityType e) {
		if(e == EntityType.NPC)
			return (NPC) this;
		else if(e == EntityType.PLAYER)
			return (Player) this;
		return null;
	}

	/**
	 * @param updateFlags the updateFlags to set
	 */
	public void setUpdateFlags(UpdateFlags updateFlags) {
		this.updateFlags = updateFlags;
	}

	/**
	 * @return the updateFlags
	 */
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	/**
	 * @param sprites the sprites to set
	 */
	public void setSprites(Sprites sprites) {
		this.sprites = sprites;
	}

	/**
	 * @return the sprites
	 */
	public Sprites getSprites() {
		return sprites;
	}

	/**
	 * @param isTeleporting the isTeleporting to set
	 */
	public void setTeleporting(boolean isTeleporting) {
		this.isTeleporting = isTeleporting;
	}

	/**
	 * @return the isTeleporting
	 */
	public boolean isTeleporting() {
		return isTeleporting;
	}

	/**
	 * @param lastKnownRegion the lastKnownRegion to set
	 */
	public void setLastKnownRegion(Location lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
	}

	/**
	 * @return the lastKnownRegion
	 */
	public Location getLastKnownRegion() {
		return lastKnownRegion;
	}

	/**
	 * @param teleportTarget the teleportTarget to set
	 */
	public void setTeleportTarget(Location teleportTarget) {
		this.teleportTarget = teleportTarget;
	}

	/**
	 * @return the teleportTarget
	 */
	public Location getTeleportTarget() {
		return teleportTarget;
	}

	/**
	 * @param mapRegionChanging the mapRegionChanging to set
	 */
	public void setMapRegionChanging(boolean mapRegionChanging) {
		this.mapRegionChanging = mapRegionChanging;
	}

	/**
	 * @return the mapRegionChanging
	 */
	public boolean isMapRegionChanging() {
		return mapRegionChanging;
	}

	/**
	 * @param walkingQueue the walkingQueue to set
	 */
	public void setWalkingQueue(WalkingQueue walkingQueue) {
		this.walkingQueue = walkingQueue;
	}

	/**
	 * @return the walkingQueue
	 */
	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

}