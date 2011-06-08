package com.gecko.packet.handlers;

import com.gecko.Server;
import com.gecko.model.Player;
import com.gecko.network.io.InputStream;
import com.gecko.packet.PacketHandler;
import com.gecko.util.entity.ChatUtils;

/**
 * PlayerChatInputPacketHandler.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 7 Jun 2011
 */
public class PlayerChatInputPacketHandler implements PacketHandler {

	/**
	 * handle
	 */
	@Override
	public void handle(InputStream in, Player player, int opcode) {
		final int COLOUR = in.read();
		final int SFX = in.read();
		final int LENGTH = in.read();
		String msg = ChatUtils.decryptPlayerChat(in, LENGTH);
		Server.getServerConfig().logger.info("CHAT MSG: "+msg);
	}

}
