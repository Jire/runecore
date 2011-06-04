package com.gecko.network;

import com.gecko.common.Packet.Type;
import com.gecko.model.Player;
import com.gecko.network.io.OutputStream;

/**
 * Sends packets which are preconfigured towards a
 * player's connection session.
 * 
 * <p>Packets are builded and shipped using data provided 
 * in the function arguments.</p>
 * 
 * @author Thomas Nappo
 */
public class PacketSender {
	
	/**
	 * The player who we send packets for.
	 */
	private final Player player;
	
	/**
	 * Constructs a new packet sender.
	 * @param player The player who we send packets for.
	 */
	public PacketSender(Player player) {
		this.player = player;
	}
	
	/**
	 * Brings the client from the login screen
	 * to the game screen.
	 */
	public PacketSender login() {
		sendMapRegion();
		sendWelcomeScreen(378, 20);
		sendMessage("Welcome to RuneScape.");
		return this;
	}
	
	/**
	 * Sends the welcome screen.
	 * @param topInterface The top interface
	 * @param buttonInterface The button interface.
	 */
	public PacketSender sendWelcomeScreen(int topInterface, int buttonInterface) {
		player.write(new OutputStream(129).writeShort(buttonInterface).writeShortA(topInterface));
		return this;
	}
	
	/**
	 * Sends a server message.
	 * @param msg The message context to send.
	 */
	public PacketSender sendMessage(String msg) {
		player.write(new OutputStream(158).writeString(msg));
		return this;
	}
	
	/**
	 * Sends server messages.
	 * @param msgs The messages to sends.
	 */
	public PacketSender sendMessage(String... msgs) {
		for (String msg : msgs) sendMessage(msg);
		return this;
	}
	
	/**
	 * Sends the map region.
	 */
	public PacketSender sendMapRegion() {
		OutputStream out = new OutputStream(49, Type.SHORT)
				.writeShortA(player.getLocation().getLocalY())
				.writeLEShortA(player.getLocation().getLocalX());
		for (int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= ((player.getLocation().getRegionX() + 6) / 8); xCalc++) {
			for (int yCalc = (player.getLocation().getRegionY() - 6) / 8; yCalc <= ((player.getLocation().getRegionY() + 6) / 8); yCalc++) {
				@SuppressWarnings("unused")
				int region = yCalc + (xCalc << 8); // 1786653352
				if (yCalc != 49 && yCalc != 149 && yCalc != 147 && xCalc != 50 && (xCalc != 49 || yCalc != 47)) {
					out.writeInteger2(0)
							.writeInteger2(0)
							.writeInteger2(0)
							.writeInteger2(0);
				}
			}
		}
		out.writeShortA(player.getLocation().getRegionX())
				.writeByteA(player.getLocation().getZ())
				.writeLEShortA((player.getLocation().getRegionY()));
		
		player.write(out);
		return this;
	}
	
	/**
	 * Sends a player interaction option.
	 * @param option The option text.
	 * @param slot The slot of the option.
	 * @param position The position of the option.
	 */
	public PacketSender sendInteractionOption(String option, int slot, int position) {
		player.write(new OutputStream(151, Type.BYTE)
				.writeString(option)
				.writeByteA(position)
				.write(slot));
		return this;
	}

}