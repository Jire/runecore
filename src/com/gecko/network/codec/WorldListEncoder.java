package com.gecko.network.codec;

import org.jboss.netty.channel.Channel;

import com.gecko.Server;
import com.gecko.model.world.def.WorldDef;
import com.gecko.network.io.OutputStream;
import com.gecko.util.BufferUtils;

/**
 * WorldListEncoder.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 5 Jun 2011
 */
public class WorldListEncoder {
	
	
	/**
	 * Encodes the world list data
	 * @param chan
	 * @param status
	 */
	public static OutputStream encode(Channel chan, boolean... status) {
		
		int worldListSize = Server.getServerConfig().getWorldListBuilder().worldList.size();
		
		OutputStream packet = new OutputStream();
		
		packet.write((byte) (status[0] ? 1 : 0));
		packet.write((byte) (status[1] ? 1 : 0));
		
		if(status[1]) {
			BufferUtils.putSmart(packet, worldListSize);
			setWorldLocations(packet);
			BufferUtils.putSmart(packet, 0);
			BufferUtils.putSmart(packet, (worldListSize + 1));
			BufferUtils.putSmart(packet, worldListSize);
			
			for (final WorldDef w : Server.getServerConfig().getWorldListBuilder().worldList) {
				BufferUtils.putSmart(packet, w.worldId);
				packet.writeByte((byte) w.loc.opcode);
				packet.write(w.flag);
				BufferUtils.putJagString(packet, w.activity); // activity
				BufferUtils.putJagString(packet, w.address); // ip // address
			}
			
			packet.write(-626474014);
		}
		
		if(status[0]) {
			for (final WorldDef w : Server.getServerConfig().getWorldListBuilder().worldList) {
				BufferUtils.putSmart(packet, w.worldId); // world id
				packet.writeShort((short) 1337); // player count
			}
		}
		
		final OutputStream finalBuffer = new OutputStream();
		finalBuffer.writeByte((byte) 0);
		finalBuffer.writeShort((short) packet.getLength());
		finalBuffer.writeByte(packet.getData());
		
		return finalBuffer;
	}
	
	public static void setWorldLocations(OutputStream buffer) {
		for(WorldDef def: Server.getServerConfig().getWorldListBuilder().worldList) {
			BufferUtils.putSmart(buffer, def.loc.opcode);
			BufferUtils.putJagString(buffer, def.loc.name);
		}
	}

}