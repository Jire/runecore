package com.gecko.packet.handlers;

import com.gecko.Server;
import com.gecko.model.Player;
import com.gecko.network.io.InputStream;
import com.gecko.packet.PacketHandler;

/**
 * LoadRegionPacketHandler.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 8 Jun 2011
 */
public class LoadRegionPacketHandler implements PacketHandler {

	/**
	 * Handles packet
	 */
	@Override
	public void handle(InputStream in, Player player, int opcode) {
		player.getPacketSender().sendMapRegion();
		Server.getServerConfig().logger.info("Loading finished.");
	}

}
