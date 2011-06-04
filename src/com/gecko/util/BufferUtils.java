package com.gecko.util;

import org.jboss.netty.buffer.ChannelBuffer;

public final class BufferUtils {
	
	public static String readRS2String(ChannelBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while ((b = buffer.readByte()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}

}