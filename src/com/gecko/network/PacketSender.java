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
		sendSidebar();
		sendMessage("Welcome to RuneCore.");
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

	/**
	 * Sends all the sidebars.
	 * 
	 * @return The action sender instance, for chaining.
	 */
	public PacketSender sendSidebar() {
		sendTab(14, 751); // Chat options
		sendTab(75, 752); // Chatbox
		sendTab(70, 748); // HP bar
		sendTab(71, 749); // Prayer bar
		sendTab(72, 750); // Energy bar
		sendTab(67, 747); // Summoning bar
		sendGameInterface(1, 752, 8, 137); // Username on chat
		sendTab(83, 92); // Attack tab
		sendTab(84, 320); // Skill tab
		sendTab(85, 274); // Quest tab
		sendTab(86, 149); // Inventory tab
		sendTab(87, 387); // Equipment tab
		sendTab(88, 271); // Prayer tab
		sendTab(89, 192); // Magic tab
		sendTab(91, 550); // Friend tab
		sendTab(92, 551); // Ignore tab
		sendTab(93, 589); // Clan tab
		sendTab(94, 261); // Setting tab
		sendTab(95, 464); // Emote tab
		sendTab(96, 187); // Music tab
		sendTab(97, 182); // Logout tab
		return this;
	}
	
	/**
	 * Sends a tab.
	 * 
	 * @param tabId
	 *            The tab id.
	 * @param childId
	 *            The child id.
	 * @return The action sender instance, for chaining.
	 */
	public PacketSender sendTab(int tabId, int childId) {
		sendGameInterface(1, 548, tabId, childId);
		return this;
	}
	
	/**
	 * Sends a game interface.
	 * 
	 * @param showId
	 *            The show id.
	 * @param windowId
	 *            The window id.
	 * @param interfaceId
	 *            The interface id.
	 * @param childId
	 *            The child id.
	 * @return The action sender instance, for chaining.
	 */
	public PacketSender sendGameInterface(int showId, int windowId,
			int interfaceId, int childId) {
		final int id = windowId * 65536 + interfaceId;
		
		player.write(new OutputStream(141).writeLEShortA(1).writeByteC(showId)
				.writeShort(childId).writeInteger2(id));
		return this;
	}
	
	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            The message to send.
	 * @return The action sender instance, for chaining.
	 */
	public PacketSender sendMessage(String string) {
		player.write(new OutputStream(35, Type.BYTE).writeRS2String(string));
		return this;
	}
	
}