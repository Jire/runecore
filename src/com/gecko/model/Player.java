package com.gecko.model;

import org.jboss.netty.channel.Channel;

import com.gecko.network.PacketSender;

/**
 * A {@link Combatable} entity which is connected and controlled via client.
 * @author Thomas Nappo
 */
public class Player extends Combatable {
	
	/**
	 * The player's packet sender.
	 */
	private final PacketSender packetSender = new PacketSender(this);
	
	/**
	 * Gets the player's packet sender.
	 * 
	 * <i><p>You can use the {@link PacketSender} to write
	 * preconfigured packets easily.</p></i>
	 * 
	 * @return The player's {@link #packetSender}
	 */
	public PacketSender getPacketSender() {
		return packetSender;
	}
	
	/**
	 * Writes data towards the player's channel.
	 * @param o The data to write.
	 */
	public void write(Object o) {
		getChannel().write(o);
	}
	
	/**
	 * The player's connection channel which you can
	 * use to write out information towards the client.
	 * @return The player's connection channel.
	 */
	public Channel getChannel() {
		return session.getChannel();
	}
	
	/**
	 * Encapsulates the connection session between
	 * client and server.
	 */
	private final Session session;
	
	/**
	 * Constructs a new player.
	 * @param session The player's connection session.
	 */
	public Player(Session session) {
		this.session = session;
	}

	@Override
	public int getFormattedIndex() {
		return index + 32768;
	}

}