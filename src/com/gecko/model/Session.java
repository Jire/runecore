package com.gecko.model;

import org.jboss.netty.channel.Channel;

/**
 * A beggining connection session between the client
 * and server.
 * <i><p>This design was used to prevent incorrect logins
 * from creating large objects, which allows attackers to crash
 * the server via overload.</p>
 * 
 * <p>After a login session has been accepted a {@link Player} object
 * is created with this session.</p></i>
 * 
 * @author Thomas Nappo
 */
public class Session {
	
	private final String username, password;
	private final Channel channel;
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public Session(String username, String password, Channel channel) {
		this.username = username; this.password = password;
		this.channel = channel;
	}

}