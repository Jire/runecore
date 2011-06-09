package com.gecko.packet;

import com.gecko.Server;
import com.gecko.model.Player;
import com.gecko.network.io.InputStream;
import com.gecko.packet.handlers.*;
import com.gecko.task.Task;

/**
 * Manages and distributes packets.
 * @author Thomas Nappo
 */
public class PacketManager {
	
	/**
	 * Mass of packet handlers which maps the {@link PacketHandler} object
	 * in the array slot of the operation code it corresponds.
	 */
	private static final PacketHandler[] mass = new PacketHandler[255];
	
	static { 
		assignPackets();
	}
	
	/**
	 * Assigns packets on startup
	 */
	public static void assignPackets() {
		assign(24, new PlayerChatInputPacketHandler());
		PacketHandler walking = new PlayerWalkingPacketHandler();
		assign(32, new LoadRegionPacketHandler());
		assign(98, walking);
		assign(218, walking);
		assign(249, walking);
	}
	
	/**
	 * Assigns a packet handler to an operation code.
	 * @param id When the server decodes a packet with this id (op code)
	 * it is sent towards the packet handler which is assigned the corresponding code.
	 * @param handler The {@link PacketHandler} which handles the operation.
	 */
	public static void assign(int id, PacketHandler handler) {
		mass[id] = handler;
	}
	
	// instances
	private static final _DummyPacket dummy = new _DummyPacket();
	
	/**
	 * Handles a packet.
	 * @param id The packet's operation code.
	 * @param in The stream object which the handler can read information from.
	 * @param player The player who sent the packet.
	 */
	public static void handle(final int id, final InputStream in, final Player player) {
		Server.getServerConfig().logger.info("OPCODE: "+id);
		Server.getTaskScheduler().schedule(new Task() {
			@Override
			public void execute() {
				this.stop();
				if (id < 0) {
					dummy.handle(in, player, id);
					return;
				}
				PacketHandler packet = mass[id];
				if(packet == null)
					return;
				packet.handle(in, player, id);
			}
		});
	}

}