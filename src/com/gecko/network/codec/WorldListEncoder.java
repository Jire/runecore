package com.gecko.network.codec;

import org.jboss.netty.channel.Channel;

import com.gecko.Server;
import com.gecko.model.world.def.WorldDef;
import com.gecko.network.io.OutputStream;
import com.gecko.util.BufferUtils;

/**
 * WorldListEncoder.java
 * RuneCore 525 www.runecore.org
 * @author Harry Andreas
 * 5 Jun 2011
 */
public class WorldListEncoder {
	
	/**
	 * Encodes the world list data
	 * @param chan
	 * @param status
	 */
	@SuppressWarnings("unused")
	public static OutputStream encode(Channel chan, boolean... status) {
		
		int worldListSize = Server.getServerConfig().getWorldListBuilder().worldList.size();
		
		OutputStream packet = new OutputStream();
		
		packet.write(status[0] ? 1 : 0).write(status[1] ? 1 : 0);
		
		if (status[1]) {
			BufferUtils.putSmart(packet, worldListSize);
			setWorldLocations(packet);
			BufferUtils.putSmart(packet, 0);
			BufferUtils.putSmart(packet, (worldListSize + 1));
			BufferUtils.putSmart(packet, worldListSize);
			
			for (final WorldDef w : Server.getServerConfig().getWorldListBuilder().worldList) {
				BufferUtils.putSmart(packet, w.worldId);
				packet.write((byte)1);
				packet.write((byte)2);
				packet.writeRS2String("World1");
				packet.writeRS2String("play.runecore.org"); // ip // address
			}
			
			packet.writeInteger(-626474014);
		}
		
		if(status[0]) {
			for (final WorldDef w : Server.getServerConfig().getWorldListBuilder().worldList) {
				BufferUtils.putSmart(packet, 1); // world id
				packet.writeShort((short) 5); // player count
			}
		}
		
		OutputStream finalBuffer = new OutputStream();
		finalBuffer.write(0).writeShort((short) packet.getData().length).writeByte(packet.getData());
		
		return finalBuffer;
	}
	
	public static void setWorldLocations(OutputStream buffer) {
		for(WorldDef def: Server.getServerConfig().getWorldListBuilder().worldList) {
			BufferUtils.putSmart(buffer, def.loc.opcode);
			buffer.writeRS2String("Nigeria");
		}
	}

}