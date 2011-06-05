package com.gecko.util;

import org.jboss.netty.buffer.ChannelBuffer;

import com.gecko.network.io.OutputStream;

public final class BufferUtils {
	
	public static String readRS2String(ChannelBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while ((b = buffer.readByte()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}
	
	/**
	 * Writes a string
	 * 
	 * @param buffer
	 *            The ChannelBuffer
	 * @param string
	 *            The string being wrote.
	 */
	public static void putJagString(OutputStream buffer, String string) {
		buffer.write((byte) 0);
		buffer.writeByte(string.getBytes());
		buffer.write((byte) 0);
	}
	
	/**
	 * Writes a smart
	 * 
	 * @param buffer
	 *            The ChannelBuffer
	 * @param value
	 *            The value being wrote
	 */
	public static void putSmart(OutputStream buffer, int value) {
		if ((value ^ 0xffffffff) > -129) {
			buffer.write((byte) value);
		} else {
			buffer.writeShort((short) value);
		}
	}

}