package com.gecko.network.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.gecko.Constants;
import com.gecko.common.Packet;
import com.gecko.model.Player;
import com.gecko.packet.PacketManager;

/**
 * Decodes incoming frames.
 * @author Thomas Nappo
 */
public class Decoder extends FrameDecoder {
	
	private Player player;
	
	public Decoder(Player player) {
		this.player = player;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
		int id = buffer.readUnsignedByte(); // operation code
		int length = Constants.PACKET_LENGTHS[id];
		if (length < 0) length = buffer.readUnsignedByte();
		if (length > buffer.readableBytes() || length <= 0) return null;
		
		byte[] data = new byte[length];
		buffer.readBytes(data);
		
		Packet packet = new Packet(id, data);
		PacketManager.handle(id, packet.toStream(), player); // handle packet
		
		return packet;
	}

}