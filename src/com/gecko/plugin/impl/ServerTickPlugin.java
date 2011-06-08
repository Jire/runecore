package com.gecko.plugin.impl;

import com.gecko.Server;
import com.gecko.model.Player;
import com.gecko.plugin.Plugin;

/**
 * ServerTickPlugin.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 7 Jun 2011
 */
public class ServerTickPlugin implements Plugin {

	/**
	 * activation
	 */
	@Override
	public void activation() {
		// TODO Auto-generated method stub

	}

	/**
	 * loop
	 */
	@Override
	public void loop() {
		
		for(Player p : Server.getOnlinePlayers()) {
			p.getWalkingQueue().processNextMovement();
			p.tick();
		}
		
		for(Player p : Server.getOnlinePlayers()) {
			try {
				new PlayerUpdatingPlugin().loop(p);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		for(Player player : Server.getOnlinePlayers()) {
			player.getUpdateFlags().reset();
			player.setTeleporting(false);
			player.setMapRegionChanging(false);
			player.resetTeleportTarget();
		}
		
	}

	/**
	 * needsLoop
	 */
	@Override
	public boolean needsLoop() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * remove
	 */
	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
