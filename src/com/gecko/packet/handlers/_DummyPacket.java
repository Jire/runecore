package com.gecko.packet.handlers;

import com.gecko.model.Player;
import com.gecko.network.io.InputStream;
import com.gecko.packet.PacketHandler;

public class _DummyPacket implements PacketHandler {

	@Override
	public void handle(InputStream in, Player player, int opcode) {
	}
}