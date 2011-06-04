package com.gecko.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.gecko.common.Packet.Type;
import com.gecko.network.io.OutputStream;

/**
 * Encodes frames for output.
 * @author Thomas Nappo
 */
public class Encoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if (!(msg instanceof OutputStream)) {
			throw new IOException("Can only encode OutputStream objects!");
		}
		
		OutputStream out = (OutputStream) msg;
		if (out.isHeadless()) return ChannelBuffers.wrappedBuffer(out.getData());
		
		int length = out.getLength() + 1; // id + length
		
		Type type = out.getType();
		switch (type) {
		case BYTE:
			length++;
			break;
		case SHORT:
			length += 2;
			break;
		}
		
		ChannelBuffer cBuffer = ChannelBuffers.buffer(length);
		
		cBuffer.writeByte((byte) out.getId());
		
		switch (type) {
		case BYTE:
			cBuffer.writeByte((byte) out.getLength());
			break;
		case SHORT:
			cBuffer.writeShort((short) out.getLength());
			break;
		}
		
		cBuffer.writeBytes(out.getData(), 0, out.getLength());
		
		return cBuffer;
	}

}