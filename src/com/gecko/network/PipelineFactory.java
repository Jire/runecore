package com.gecko.network;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.gecko.network.codec.*;

/**
 * Configures a new pipeline for the server.
 * @author Thomas Nappo
 */
public class PipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline next = Channels.pipeline();
		
		next.addLast("encoder", new Encoder());
		/* the decoder is later switched over */
		next.addLast("decoder", new FrontDecoder());
		next.addLast("handler", new ChannelHandler());
		
		return next;
	}

}