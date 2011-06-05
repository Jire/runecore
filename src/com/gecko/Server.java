package com.gecko;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.gecko.network.PipelineFactory;
import com.gecko.task.TaskScheduler;
import com.gecko.util.ServerConfiguration;
import com.gecko.util.XStreamParser;

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
	 * The XStream instance
	 */
	public static XStreamParser xStream;
	
	/**
	 * Server config 
	 */
	private static ServerConfiguration serverConfig = new ServerConfiguration();
	
	
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
	
	/**
	 * Invoked on startup of the JVM application
	 * @param args The program arguments
	 */
	public static void main(String[] args) {
		/**
		 * Read arguements
		 */
		int port = Integer.parseInt(args[0]);
		
		/**
		 * Setup the server configuration
		 * and read files/cache
		 */
		getServerConfig().setupServer();
		
		/**
		 * Setup the networking
		 */
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setPipelineFactory(new PipelineFactory());		
		bootstrap.bind(new InetSocketAddress(port));
		
		/**
		 * Done reading msg
		 */
		logger.info("Server listening: " + port);
	}

	/**
	 * @param serverConfig the serverConfig to set
	 */
	public static void setServerConfig(ServerConfiguration serverConfig) {
		Server.serverConfig = serverConfig;
	}

	/**
	 * @return the serverConfig
	 */
	public static ServerConfiguration getServerConfig() {
		return serverConfig;
	}
}