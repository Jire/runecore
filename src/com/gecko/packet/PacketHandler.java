package com.gecko.packet;

import com.gecko.model.Player;
import com.gecko.network.io.InputStream;

/**
 * Handles a packet.
 * @author Thomas Nappo
 */
public interface PacketHandler {
	
	/**
	 * Handles the packet.
	 * @param in The stream of input to handle the packet with.
	 * @param player The player who called the packet.
	 */
	public void handle(InputStream in, Player player);

}