package com.gecko.network.codec;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import com.gecko.Constants;
import com.gecko.model.Player;
import com.gecko.model.Session;
import com.gecko.network.PacketSender;
import com.gecko.network.io.ISAACCipher;
import com.gecko.network.io.OutputStream;
import com.gecko.util.BufferUtils;
import com.gecko.util.NameUtils;

/**
 * Decodes first-hand client handshaking, updating, and login.
 * @author Thomas Nappo
 */
@SuppressWarnings("unused")
public class FrontDecoder extends ReplayingDecoder<FrontDecoder.State> {
	
	/**
	 * Generates server keys.
	 */
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * Logs for the decoder.
	 */
	private static final Logger logger = Logger.getLogger(FrontDecoder.class.getName());
	
	private int nameHash; // instance name-hash for security
	private long serverKey; // randomly generated key for security

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, State state) throws Exception {
		logger.info("State=" + state.name());
		switch (state) {
		case HANDSHAKE:
			if (buffer.readableBytes() < 2) return false;
			
			/* get the operation */
			int stage = buffer.readUnsignedByte();
			logger.info("Stage=" + stage);
			switch (stage) {
			case 15: // Update
				revCheck(buffer);
				
				/* notify client we passed revision check */
				channel.write(new OutputStream().write(0));
				checkpoint(State.ON_DEMAND);
				break;
			case 14: // Login
				nameHash = buffer.readUnsignedByte();
				
				channel.write(new OutputStream()
						.write(0) // notification
						/* generate random key and send to client */
						.writeLong(serverKey = RANDOM.nextLong()));
				
				checkpoint(State.FINISH);
				break;
			case 131: // World update
				checkpoint(State.WORLD_UPDATE);
				break;
			}
			break;
		case ON_DEMAND:
			if (buffer.readableBytes() < 8) return false;
			buffer.skipBytes(8); // request bytes

			/*
			 * here's where we give the client the update
			 * keys. these "trick" the client to pass the on-demand
			 * update request stage.
			 * 
			 * TODO: write update server?
			 */
			channel.write(new OutputStream().write(Constants.UPDATE_KEYS)).addListener(ChannelFutureListener.CLOSE);
			return true;
		case WORLD_UPDATE:
			/*
			 * here's where we give the client the world
			 * keys. these bypass the client's world list
			 * stage.
			 * 
			 * TODO: write world server?
			 */
			channel.write(new OutputStream().write(Constants.WORLD_LIST_DATA)).addListener(ChannelFutureListener.CLOSE);
			return true;
		case FINISH:
			if (buffer.readableBytes() < 3) return false;
			
			/*
			 * This is the login type.
			 * 
			 * 16 = normal
			 * 18 = reconnection
			 */
			int loginType = buffer.readUnsignedByte();
			
			if (loginType != 16 && loginType != 18) {
				throw new IOException("Incompatible login type!");
			}
			
			int loginSize = buffer.readUnsignedShort();
			
			if (buffer.readableBytes() < loginSize) return false;
			
			byte[] payload = new byte[loginSize];
			buffer.readBytes(payload);
			
			int loginSizeC = loginSize - (36 + 1 + 1 + 2);
			if (loginSizeC < 0) return false;
			
			revCheck(buffer);
			
			boolean highMem = buffer.readByte() == 0; 
			
			/* Not sure what this is, value is always 19. */
			buffer.readByte();
			
			/* Skip over the cache indices */
			for (int i = 0; i < 16; i++) buffer.readInt();
			
			/* RSA security enforcement */
			if (buffer.readUnsignedByte() != 10) {
				throw new IOException("Invalid RSA code!");
			}
			
			/*
			 * We would perform the key check here but
			 * it's disabled in the client.
			 */
			
			/*
			 * These keys will later be used for ciphering.
			 */
			int[] sessionKeys = new int[4];
			for (int i = 0; i < sessionKeys.length; i++)
				sessionKeys[i] = buffer.readInt();
			
			/*
			 * This is the user identification key.
			 * 
			 * The UID we receive must match our predefined
			 * UID, which ensures any received login request
			 * was from our client.
			 */
			if (buffer.readInt() != 59872) {
				/*
				 * We would stop it here, but the client
				 * just sends random numbers, making this useless.
				 */
			}
			
			/*
			 * Now required security procedures have passed.
			 * We decrypt the username and password received.
			 */
			String username = NameUtils.longToName(buffer.readLong());
			String password = BufferUtils.readRS2String(buffer);
			
			/* Start the input ciphering. */
			ISAACCipher inCipher = new ISAACCipher(sessionKeys);
			
			/* Increase each key by 50 to get it ready for out ciphering */
			for (int i = 0; i < 4; i++) sessionKeys[i] += 50;
			
			/* Start the output ciphering */
			ISAACCipher outCipher = new ISAACCipher(sessionKeys);
			
			/*
			 * Login has been completed.
			 */
			logger.info("Login request: (" + username + "," + password + ")");
			Session session = new Session(username, password, channel);
			
			// TODO: Pass check and stuff...
			
			/*
			 * By default the return code is 2, which is
			 * a successful login.
			 */
			int returnCode = 2;
			
			/*
			 * TODO: Check to see if login is possible.
			 */
			
			OutputStream loginResponse = new OutputStream().write(returnCode);
			
			/*
			 * Since the login was not succesful we write out
			 * the response and close the connection channel.
			 */
			if (returnCode != 2) {
				channel.write(loginResponse).addListener(ChannelFutureListener.CLOSE);
				return false;
			}
			
			/*
			 * Player object construction is done here to be conservative
			 * about instantiating new massive objects without need.
			 */
			Player player = new Player(session);
			
			/*
			 * TODO: Write out the proper data.
			 */
			channel.write(loginResponse.write(0, 0, 0, 0));
			
			/* 
			 * We swap the decoder over. Decoding that is done beyond this point
			 * are client-sent packets.
			 */
			player.getChannel().getPipeline().replace("decoder", "decoder", new Decoder(player));
			
			/*
			 * Finish off the login by providing the client with
			 * needed frames.
			 */
			player.getPacketSender().login();
			
			return true;
		}
		return false;
	}
	
	/**
	 * Performs a revision check with a buffer.
	 * @param buffer The buffer to read from.
	 * @throws IOException Should the revision not match.
	 */
	private void revCheck(ChannelBuffer buffer) throws IOException {
		if (buffer.readInt() != 525) throw new IOException("Revision mismatch!");
	}
	
	public FrontDecoder() {
		super(false);
		/*
		 * though it may sound a bit funky, all front
		 * decoding starts at the handshake state.
		 * 
		 * the decoder reads a u_byte which signals the
		 * operation (state) to perform
		 */
		checkpoint(State.HANDSHAKE);
	}
	
	/**
	 * The state of front decoding.
	 * @author Thomas Nappo
	 */
	static enum State {
		
		/**
		 * A front-decoding state where the decoder
		 * reads an unsigned byte which signals the
		 * next operation (state) to perform after
		 * verifying the client is compatible.
		 */
		HANDSHAKE,
		
		/**
		 * A front-decoding state where the client
		 * is requesting an on-demand update.
		 * 
		 * <p>As of now, the server sends update keys
		 * which "trick" the client to pass the update state.</p>
		 */
		ON_DEMAND,
		
		/**
		 * A front-decoding state where the client
		 * is requesting the updated world data.
		 * 
		 * <p>As of now, the server sends world keys
		 * which "trick" the client to pass the world update state.</p>
		 */
		WORLD_UPDATE,
		
		/**
		 * The final front-decoding state where the
		 * server transfers the client into the game
		 * server and switches the decoder over to
		 * {@link Decoder}.
		 */
		FINISH;
		
	}

}