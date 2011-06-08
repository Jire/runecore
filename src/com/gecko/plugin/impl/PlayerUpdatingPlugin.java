package com.gecko.plugin.impl;

import java.util.Iterator;

import com.gecko.Server;
import com.gecko.common.Packet.Type;
import com.gecko.model.Player;
import com.gecko.model.mask.UpdateFlags;
import com.gecko.model.mask.UpdateFlags.UpdateFlag;
import com.gecko.model.player.Apperance;
import com.gecko.network.io.OutputStream;
import com.gecko.plugin.Plugin;
import com.gecko.util.Misc;
import com.gecko.util.NameUtils;

/**
 * PlayerUpdatingPlugin.java RuneCore 525 www.runecore.org
 * 
 * @author Canownueasy, Sinisoul + Harry Andreas 7 Jun 2011
 */
public class PlayerUpdatingPlugin implements Plugin {

	/**
	 * loop
	 */
	public void loop(Player p) {

		OutputStream updateBlock = new OutputStream();
		OutputStream updatePacket = new OutputStream(187, Type.SHORT);
		updateThisPlayerMovement(updatePacket, p);
		updatePlayer(updateBlock, p, false);
		updatePacket.writeBits(8, p.getLocalPlayers().size());
		for (final Iterator<Player> it$ = p.getLocalPlayers().iterator(); it$
				.hasNext();) {
			final Player localPlayer = it$.next();
			if (!Server.getOnlinePlayers().contains(localPlayer)
					&& Misc.distance(p.getLocation(), localPlayer.getLocation()) < 17) {
				updatePlayerMovement(updatePacket, localPlayer);
				if (localPlayer.getUpdateFlags().isUpdateRequired()) {
					updatePlayer(updatePacket, localPlayer, false);
				}
			} else {
				it$.remove();
				updatePacket.writeBits(1, 1);
				updatePacket.writeBits(2, 3);
			}
		}
		for (Player serverPlayer : Server.getOnlinePlayers()) {
			/**
			 * Check to see if there is more than 255 players in the players
			 * region
			 */
			if (p.getLocalPlayers().size() >= 255) {
				break;
			}

			/**
			 * Checks if the player is already in the list or it's your own
			 * player instance
			 */
			if (serverPlayer == p || p.getLocalPlayers().contains(serverPlayer)) {
				continue;
			}

			/**
			 * Adds the player to the local player list
			 */
			p.getLocalPlayers().add(serverPlayer);

			/**
			 * Adds the player to the updatePacket
			 */
			addNewPlayer(updatePacket, p, serverPlayer);

			/**
			 * Updates the player into the login block
			 */
			updatePlayer(updateBlock, serverPlayer, true);
		}
		if (!updateBlock.isEmpty()) {
			updatePacket.writeBits(11, 2047);
			updatePacket.writeByte(updateBlock.getData());
		}
		p.write(updatePacket);
	}

	/**
	 * Updates a player.
	 * 
	 * @param packet
	 *            The packet.
	 * @param otherPlayer
	 *            The other player.
	 * @param forceAppearance
	 *            The force appearance flag.
	 * @param noChat
	 *            Indicates chat should not be relayed to this player.
	 */
	public void updatePlayer(OutputStream packet, Player otherPlayer,
			boolean forceAppearance) {

		/*
		 * If no update is required and we don't have to force an appearance
		 * update, don't write anything.
		 */
		if (!otherPlayer.getUpdateFlags().isUpdateRequired()
				&& !forceAppearance) {
			return;
		}
		/*
		 * We can used the cached update block!
		 */
		synchronized (otherPlayer) {
			/*
			 * Calculate the bitmask.
			 */
			int mask = 0;
			final UpdateFlags flags = otherPlayer.getUpdateFlags();
			if (flags.get(UpdateFlag.GRAPHICS)) {
				mask |= 0x200;
			}
			if (flags.get(UpdateFlag.ANIMATION)) {
				mask |= 0x4;
			}
			if (flags.get(UpdateFlag.FORCED_CHAT)) {
				mask |= 0x80;
			}
			if (flags.get(UpdateFlag.CHAT)) {
				mask |= 0x2;
			}
			if (flags.get(UpdateFlag.FACE_ENTITY)) {
				mask |= 0x10;
			}
			if (flags.get(UpdateFlag.APPEARANCE) || forceAppearance) {
				mask |= 0x8;
			}
			if (flags.get(UpdateFlag.FACE_COORDINATE)) {
				mask |= 0x40;
			}
			if (flags.get(UpdateFlag.HIT)) {
				mask |= 0x1;
			}
			if (flags.get(UpdateFlag.HIT_2)) {
				mask |= 0x400;
			}
			/*
			 * Check if the bitmask would overflow a byte.
			 */
			if (mask >= 0x100) {
				/*
				 * Write it as a short and indicate we have done so.
				 */
				mask |= 0x20;
				packet.write((byte) (mask & 0xFF));
				packet.write((byte) (mask >> 8));
			} else {
				/*
				 * Write it as a byte.
				 */
				packet.write((byte) mask);
			}
			/*
			 * Append the appropriate updates.
			 */
			if (flags.get(UpdateFlag.GRAPHICS)) {
				// appendGraphicsUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.ANIMATION)) {
				// appendAnimationUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.FORCED_CHAT)) {
				// packet.putRS2String(player.getForceText());
			}
			if (flags.get(UpdateFlag.CHAT)) {
				// appendChatUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.FACE_ENTITY)) {
				// final Entity entity = otherPlayer.getInteractingEntity();
				// packet.putLEShortA(entity == null ? -1 : entity
				// .getClientIndex());
			}
			if (flags.get(UpdateFlag.APPEARANCE) || forceAppearance) {
				appendPlayerAppearanceUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.FACE_COORDINATE)) {
				// appendFaceLocationUpdate(packet, otherPlayer);
			}
			if (flags.get(UpdateFlag.HIT)) {
				// appendHitUpdate(otherPlayer, packet);
			}
			if (flags.get(UpdateFlag.HIT_2)) {
				// appendHit2Update(otherPlayer, packet);
			}
		}
	}

	/**
	 * Updates this player's movement.
	 * 
	 * @param packet
	 *            The packet.
	 */
	private void updateThisPlayerMovement(final OutputStream packet,
			Player player) {
		/*
		 * Check if the player is teleporting.
		 */
		if (player.isTeleporting() || player.isMapRegionChanging()) {
			/*
			 * They are, so an update is required.
			 */
			packet.writeBits(1, 1);

			/*
			 * This value indicates the player teleported.
			 */
			packet.writeBits(2, 3);

			/*
			 * These are the positions.
			 */
			packet.writeBits(7,
					player.getLocation().getLocalX(player.getLastKnownRegion()));

			/*
			 * This flag indicates if an update block is appended.
			 */
			packet.writeBits(1, player.getUpdateFlags().isUpdateRequired() ? 1
					: 0);
			/*
			 * These are the positions.
			 */
			packet.writeBits(7,
					player.getLocation().getLocalY(player.getLastKnownRegion()));
			/*
			 * This is the new player height.
			 */
			packet.writeBits(2, player.getLocation().getZ());

			/*
			 * They are, so an update is required.
			 */
			packet.writeBits(1, 1);
		} else {
			/*
			 * Otherwise, check if the player moved.
			 */
			if (player.getSprites().getPrimarySprite() == -1) {
				/*
				 * Signifies an update is required.
				 */
				packet.writeBits(1,
						player.getUpdateFlags().isUpdateRequired() ? 1 : 0);

				/*
				 * But signifies that we didn't move.
				 */
				if (player.getUpdateFlags().isUpdateRequired()) {
					packet.writeBits(2, 0);
				}
			} else {
				/*
				 * Check if the player was running.
				 */
				if (player.getSprites().getSecondarySprite() != -1) {
					/*
					 * The player walked, an update is required.
					 */
					packet.writeBits(1, 1);

					/*
					 * This indicates the player only walked.
					 */
					packet.writeBits(2, 2);

					/*
					 * This is the player's running direction.
					 */
					packet.writeBits(3, player.getSprites()
							.getSecondarySprite());

					/*
					 * This is the player's walking direction.
					 */
					packet.writeBits(3, player.getSprites().getPrimarySprite());

					/*
					 * This flag indicates an update block is appended.
					 */
					packet.writeBits(1, player.getUpdateFlags()
							.isUpdateRequired() ? 1 : 0);
				} else {
					/*
					 * The player ran, so an update is required.
					 */
					packet.writeBits(1, 1);

					/*
					 * This indicates the player ran.
					 */
					packet.writeBits(2, 1);

					/*
					 * And this is the running direction.
					 */
					packet.writeBits(3, player.getSprites().getPrimarySprite());

					/*
					 * And this flag indicates an update block is appended.
					 */
					packet.writeBits(1, player.getUpdateFlags()
							.isUpdateRequired() ? 1 : 0);
				}
			}
		}
	}

	public void updatePlayerMovement(OutputStream packet, Player otherPlayer) {
		/*
		 * Check which type of movement took place.
		 */
		if (otherPlayer.getSprites().getPrimarySprite() == -1) {
			/*
			 * If no movement did, check if an update is required.
			 */
			if (otherPlayer.getUpdateFlags().isUpdateRequired()) {
				/*
				 * Signify that an update happened.
				 */
				packet.writeBits(1, 1);

				/*
				 * Signify that there was no movement.
				 */
				packet.writeBits(2, 0);
			} else {
				/*
				 * Signify that nothing changed.
				 */
				packet.writeBits(1, 0);
			}
		} else if (otherPlayer.getSprites().getSecondarySprite() == -1) {
			/*
			 * The player moved but didn't run. Signify that an update is
			 * required.
			 */
			packet.writeBits(1, 1);

			/*
			 * Signify we moved one tile.
			 */
			packet.writeBits(2, 1);

			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
			packet.writeBits(3, otherPlayer.getSprites().getPrimarySprite());

			/*
			 * Write a flag indicating if a block update happened.
			 */
			packet.writeBits(1,
					otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		} else {
			/*
			 * The player ran. Signify that an update happened.
			 */
			packet.writeBits(1, 1);

			/*
			 * Signify that we moved two tiles.
			 */
			packet.writeBits(2, 2);

			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
			packet.writeBits(3, otherPlayer.getSprites().getPrimarySprite());

			/*
			 * Write the secondary sprite (i.e. run direction).
			 */
			packet.writeBits(3, otherPlayer.getSprites().getSecondarySprite());

			/*
			 * Write a flag indicating if a block update happened.
			 */
			packet.writeBits(1,
					otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		}
	}

	private void addNewPlayer(OutputStream packet, Player player,
			Player otherPlayer) {
		/*
		 * Write the player index.
		 */
		packet.writeBits(11, otherPlayer.getIndex());
		/*
		 * Write two flags here: the first indicates an update is required
		 */
		packet.writeBits(1, 1);

		/*
		 * Calculate the x and y offsets.
		 */
		final int yPos = otherPlayer.getLocation().getY()
				- player.getLocation().getY();
		final int xPos = otherPlayer.getLocation().getX()
				- player.getLocation().getX();

		/*
		 * Write the x and y offsets.
		 */
		packet.writeBits(5, yPos);
		packet.writeBits(5, xPos);

		/*
		 * We should discard client-side walk queues.
		 */
		packet.writeBits(1, 1);

		/*
		 * Lets do this for lolz.
		 */
		packet.writeBits(3, 1);
	}

	/**
	 * Appends an appearance update.
	 * 
	 * @param packet
	 *            The packet.
	 * @param otherPlayer
	 *            The player.
	 */
	private void appendPlayerAppearanceUpdate(OutputStream packet,
			Player otherPlayer) {
		final Apperance app = otherPlayer.getApperance();
		final OutputStream playerProps = new OutputStream();

		playerProps.write((byte) (app.getGender() & 0xFF)); // gender
		if ((app.getGender() & 0x2) == 2) {
			playerProps.write((byte) 0);
			playerProps.write((byte) 0);
		}

		playerProps.write((byte) 0); // skull icon
		playerProps.write((byte) 0); // skull icon

		if (!otherPlayer.getApperance().isNpc()) {

			for (int i = 0; i < 4; i++) {
				playerProps.write((byte) 0);
			}

			playerProps.writeShort(0x100 + app.getChest()); // Chest model
			playerProps.write((byte) 0); // Shield model
			playerProps.writeShort(0x100 + app.getArms()); // Arms model
			playerProps.writeShort(0x100 + app.getLegs()); // Legs model
			playerProps.writeShort(0x100 + app.getHead()); // Head model
			playerProps.writeShort(0x100 + app.getHands()); // Hands model
			playerProps.writeShort(0x100 + app.getFeet()); // Feet model
			playerProps.writeShort(0x100 + app.getBeard()); // Beard model

		} else {
			playerProps.writeShort(-1);
			playerProps.writeShort(otherPlayer.getApperance().getNpcIndex());
		}
		playerProps.write((byte) app.getHairColour()); // hairc
		playerProps.write((byte) app.getTorsoColour()); // torsoc
		playerProps.write((byte) app.getLegColour()); // legc
		playerProps.write((byte) app.getFeetColour()); // feetc
		playerProps.write((byte) app.getSkinColour()); // skinc

		playerProps.writeShort(0x328); // stand
		playerProps.writeShort(0x337); // stand turn
		playerProps.writeShort(0x333); // walk
		playerProps.writeShort(0x334); // turn 180
		playerProps.writeShort(0x335); // turn 90 cw
		playerProps.writeShort(0x336); // turn 90 ccw
		playerProps.writeShort(0x338); // run

		playerProps.writeLong(NameUtils.nameToLong(otherPlayer.session
				.getUsername()));
		playerProps.write((byte) 3);// combat
		playerProps.writeShort((short) 10);// total
		playerProps.write((byte) 0);

		final OutputStream propsPacket = playerProps;
		packet.write((byte) propsPacket.getLength());
		packet.writeBytesA(propsPacket.getData(), 0, propsPacket.getLength());
	}

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
		// TODO Auto-generated method stub

	}

	/**
	 * needsLoop
	 */
	@Override
	public boolean needsLoop() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * remove
	 */
	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}
}