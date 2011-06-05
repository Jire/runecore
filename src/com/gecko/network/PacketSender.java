package com.gecko.network;

import com.gecko.Server;
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
		sendGamePane(548);
		return this;
	}
	
	/**
	 * Sends the map region.
	 */
	public PacketSender sendMapRegion() {
		final OutputStream out = new OutputStream(93, Type.SHORT);
		boolean forceSend = true;
		if ((player.getLocation().getRegionX() / 8 == 48 || player
				.getLocation().getRegionX() / 8 == 49)
				&& player.getLocation().getRegionY() / 8 == 48) {
			forceSend = false;
		}
		if (player.getLocation().getRegionX() / 8 == 48
				&& player.getLocation().getRegionY() / 8 == 148) {
			forceSend = false;
		}
		for (int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= (player
				.getLocation().getRegionX() + 6) / 8; xCalc++) {
			for (int yCalc = (player.getLocation().getRegionY() - 6) / 8; yCalc <= (player
					.getLocation().getRegionY() + 6) / 8; yCalc++) {
				final int region = yCalc + (xCalc << 8);
				if (forceSend || yCalc != 49 && yCalc != 149 && yCalc != 147
						&& xCalc != 50 && (xCalc != 49 || yCalc != 47)) {
					final int[] mapData = Server.getServerConfig().getMapData().get(region);
					out.writeInteger(mapData[0]);
					out.writeInteger(mapData[1]);
					out.writeInteger(mapData[2]);
					out.writeInteger(mapData[3]);
				}
			}
		}
		out.writeLEShort(player.getLocation().getRegionX());
		out.writeLEShortA(player.getLocation().getRegionY());
		out.writeShortA(player.getLocation().getLocalX());
		out.write(player.getLocation().getZ());
		out.writeShort(player.getLocation().getLocalY());
		player.write(out);
		return this;
	}
	
	/**
	 * Sends a game pane.
	 * 
	 * @param pane
	 *            The pane id.
	 * @return The action sender instance, for chaining.
	 */
	public PacketSender sendGamePane(int pane) {
		player.write(new OutputStream(230).writeShort(0).writeShort(pane)
				.write(0));
		return this;
	}

}