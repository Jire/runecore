package com.gecko.network;

import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Handles incoming connections and messages.
 * @author Thomas Nappo
 */
public class ChannelHandler extends SimpleChannelHandler {
	
	/**
	 * Param logging.
	 */
	private static final Logger logger = Logger.getLogger(ChannelHandler.class.getName());
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Connection received: " + ctx.getChannel().getRemoteAddress());
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Connection closed: " + ctx.getChannel().getRemoteAddress());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}

}