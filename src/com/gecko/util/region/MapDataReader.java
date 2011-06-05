package com.gecko.util.region;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.logging.Logger;

/**
 * MapDataReader.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 5 Jun 2011
 */
public class MapDataReader {
	/**
	 * Logging class.
	 */
	private static final Logger logger = Logger.getLogger(MapDataReader.class
			.getName());

	/**
	 * Prevent an instance being created.
	 */
	public MapDataReader() {
	}

	/**
	 * Loads mapdata into the specified map.
	 * 
	 * The map should have a key of <code>Integer</code> and value of
	 * <code>int[]</code>.
	 * 
	 * @param mapData
	 *            The map.
	 * @throws IOException
	 */
	public void load(Map<Integer, int[]> mapData) throws Exception {
		logger.info("Reading mapdata...");
		final DataInputStream in = new DataInputStream(new FileInputStream(
				"data/mapdata.dat"));
		int useableMapdata = 0;
		for (int i = 0; i < 16384; i++) {
			final int[] parts = new int[4];
			for (int j = 0; j < 4; j++) {
				parts[j] = in.readInt();
			}
			if (parts[0] != 0 && parts[1] != 0 && parts[2] != 0
					&& parts[3] != 0) {
				useableMapdata++;
			}
			mapData.put(i, parts);
		}
		logger.info("Loaded " + useableMapdata + " useable mapdata.");
	}

}
