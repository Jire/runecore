package com.gecko.packet.handlers;

import com.gecko.model.Player;
import com.gecko.network.io.InputStream;
import com.gecko.packet.PacketHandler;

/**
 * PlayerWalkingPacketHandler.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 8 Jun 2011
 */
public class PlayerWalkingPacketHandler implements PacketHandler {

	/**
	 * Handle packet
	 */
	@Override
	public void handle(InputStream in, Player player, int opcode) {
		player.getWalkingQueue().reset();
		int size = in.available();
		if (opcode == 98) {
			size -= 14;
		}	
		final int steps = (size - 5) / 2;
		final int[][] path = new int[steps][2];

		final int firstX = in.readLEShortA();
		for (int i = 0; i < steps; i++) {
			path[i][0] = (byte)in.read();
			path[i][1] = (byte)in.readByteC();
		}
		final boolean runSteps = in.read() + 128 == 1;
		final int firstY = in.readShortA();
		player.getWalkingQueue().setRunningQueue(runSteps);
		player.getWalkingQueue().addStep(firstX, firstY);
		for (int i = 0; i < steps; i++) {
			path[i][0] += firstX;
			path[i][1] += firstY;
			player.getWalkingQueue().addStep(path[i][0], path[i][1]);
		}
		player.getWalkingQueue().finish();
	}
	
}