package com.gecko;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.gecko.network.PipelineFactory;
import com.gecko.task.TaskScheduler;

/**
 * Represents the game server.
 * @author Thomas Nappo
 */
public class Server {
	
	/**
	 * Logs for the server.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());
	
	/**
	 * Creates and bootstraps the server to a channel.
	 */
	private static final ServerBootstrap bootstrap = new ServerBootstrap(
			new NioServerSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool()));;
	
	/**
	 * Schedules tasks to be run.
	 */
	private static final TaskScheduler taskScheduler = new TaskScheduler();
	
	/**
	 * Gets the server's {@link TaskScheduler}
	 * @return The server's task scheduler which schedules task to be run.
	 */
	public static TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setPipelineFactory(new PipelineFactory());
		bootstrap.bind(new InetSocketAddress(port));
		
		logger.info("Server listening: " + port);
	}

}