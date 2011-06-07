package com.gecko.common;

import com.gecko.network.io.InputStream;

public class Packet {
	
	protected final int id, length;
	protected byte[] data;
	protected final boolean headless;
	protected final Type type;

	public Packet(byte[] data) {
		this(-1, data.length, data, true, Type.FIXED);
	}

	public Packet(int id, byte[] data) {
		this(id, data.length, data);
	}

	public Packet(int id, int length, byte[] data) {
		this(id, length, data, false, Type.FIXED);
	}
	
	public Packet(int id, int length, byte[] data, Type type) {
		this(id, length, data, false, type);
	}

	protected Packet(int id, int length, byte[] data, boolean headless, Type type) {
		this.id = id;
		this.length = length;
		this.data = data;
		this.headless = headless;
		this.type = type;
	}
	
	public boolean isEmpty() {
		return this.getData().length == 0;
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public byte[] getData() {
		return data;
	}

	public boolean isHeadless() {
		return headless;
	}
	
	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "[Packet id=" + id + " length=" + length + "]";
	}
	
	public InputStream toStream() {
		return new InputStream(data);
	}
	
	public static enum Type {
		FIXED,
		BYTE,
		SHORT;
	}
	
}