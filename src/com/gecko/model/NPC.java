package com.gecko.model;

/**
 * A {@link Combatable} entity which is a non-player.
 * @author Thomas Nappo
 */
public class NPC extends Combatable {

	@Override
	public int getFormattedIndex() {
		return index;
	}

}