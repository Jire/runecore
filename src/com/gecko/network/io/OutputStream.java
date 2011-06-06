package com.gecko.network.io;

import com.gecko.common.Packet;

public class OutputStream extends Packet {
	
	public OutputStream() {
		this(new byte[DEFAULT_SIZE]);
	}
	
	public OutputStream(int id) {
		this(id, new byte[DEFAULT_SIZE]);
	}
	
	public OutputStream(int id, Type type) {
		this(id, DEFAULT_SIZE, new byte[DEFAULT_SIZE], id == -1, type);
	}
	
	public OutputStream(byte[] data) {
		this(-1, data.length, data, true, Type.FIXED);
	}

	public OutputStream(int id, byte[] data) {
		this(id, data.length, data);
	}

	public OutputStream(int id, int length, byte[] data) {
		this(id, length, data, false, Type.FIXED);
	}
	
	public OutputStream(int id, int length, byte[] data, Type type) {
		this(id, length, data, false, type);
	}

	private OutputStream(int id, int length, byte[] data, boolean headless, Type type) {
		super(id, length, data, headless, type);
	}

	private static final int DEFAULT_SIZE = 64;

	private static final int BITMASKS[] = {
		0, 0x1, 0x3, 0x7,
		0xf, 0x1f, 0x3f, 0x7f,
		0xff, 0x1ff, 0x3ff, 0x7ff,
		0xfff, 0x1fff, 0x3fff, 0x7fff,
		0xffff, 0x1ffff, 0x3ffff, 0x7ffff,
		0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
		0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff,
		0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
		-1
	};

	private int position = 0;
	private int bitPosition = 0;

	private OutputStream unsafeWrite(int b) {
		data[position++] = (byte) b;
		return this;
	}
	
	public OutputStream write(int... b) {
		for (int v : b) write(v);
		return this;
	}
	
	public OutputStream writeByte(byte... b) {
		for (int v : b) write(v);
		return this;
	}

	public OutputStream write(int b) {
		expand(1);
		return unsafeWrite(b);
	}

	public OutputStream writeByteA(int b) {
		expand(1);
		return unsafeWrite(b + 128);
	}

	public OutputStream writeByteC(int b) {
		expand(1);
		return unsafeWrite(-b);
	}

	public OutputStream writeShort(int s) {
		expand(2);
		unsafeWrite(s >> 8);
		return unsafeWrite(s);
	}

	public OutputStream writeShortA(int s) {
		expand(2);
		unsafeWrite(s >> 8);
		return unsafeWrite(s + 128);
	}

	public OutputStream writeLEShort(int s) {
		expand(2);
		unsafeWrite(s);
		return unsafeWrite(s >> 8);
	}

	public OutputStream writeLEShortA(int s) {
		expand(2);
		unsafeWrite(s + 128);
		return unsafeWrite(s >> 8);
	}


	public OutputStream writeInteger(int i) {
		expand(4);
		unsafeWrite(i >> 24);
		unsafeWrite(i >> 16);
		unsafeWrite(i >> 8);
		return unsafeWrite(i);
	}

	public OutputStream writeInteger1(int i) {
		expand(4);
		unsafeWrite(i >> 8);
		unsafeWrite(i);
		unsafeWrite(i >> 24);
		return unsafeWrite(i >> 16);
	}
	
	public OutputStream writeInteger2(int i) {
		expand(4);
		unsafeWrite(i >> 16);
		unsafeWrite(i >> 24);
		unsafeWrite(i);
		return unsafeWrite(i >> 8);
	}

	public OutputStream writeLong(long l) {
		expand(8);
		unsafeWrite((int) (l >> 56));
		unsafeWrite((int) (l >> 48));
		unsafeWrite((int) (l >> 40));
		unsafeWrite((int) (l >> 32));
		unsafeWrite((int) (l >> 24));
		unsafeWrite((int) (l >> 16));
		unsafeWrite((int) (l >> 8));
		return unsafeWrite((int) l);
	}

	public OutputStream writeString(String s) {
		expand(s.length() + 1);
		writeBytes(s.getBytes());
		return unsafeWrite(10);
	}
	
	public OutputStream writeRS2String(String string) {
		expand(string.length() + 1);
		writeBytes(string.getBytes());
		writeByte((byte)0);
		return this;
	}

	public OutputStream writeBytes(byte[] bs) {
		expand(bs.length);
		System.arraycopy(bs, 0, data, position, bs.length);
		position += bs.length;
		return this;
	}

	public OutputStream writeBits(int numBits, int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		position = (bitPosition + 7) / 8;
		for (; numBits > bitOffset; bitOffset = 8) {
			expand(1);
			data[bytePos] &= ~ BITMASKS[bitOffset]; // mask out the desired area
			data[bytePos++] |= value >> numBits - bitOffset & BITMASKS[bitOffset];

			numBits -= bitOffset;
		}
		if (numBits == bitOffset) {
			data[bytePos] &= ~ BITMASKS[bitOffset];
			data[bytePos] |= value & BITMASKS[bitOffset];
		} else {
			data[bytePos] &= ~ (BITMASKS[numBits] << bitOffset - numBits);
			data[bytePos] |= (value & BITMASKS[numBits]) << bitOffset - numBits;
		}
		return this;
	}

	public int getLength() {
		return position;
	}

	public byte[] getData() {
		byte[] actualData = new byte[position];
		System.arraycopy(data, 0, actualData, 0, position);
		return actualData;
	}

	private void expand(int i) {
		if (position + i < data.length) return;
		byte[] newbuf = new byte[(position + i) * 2];
		System.arraycopy(data, 0, newbuf, 0, position);
		data = newbuf;
	}
	
}