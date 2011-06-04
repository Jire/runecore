package com.gecko.util;

import com.gecko.network.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * RuneCore | 525
 * RegionKeys.java
 * @version 1.0.0
 * @author SiniSoul (SiniSoul@live.com)
 */
public class RegionKeys {
    
    static Map<Short, int[]> keystore = new HashMap<Short, int[]>();
    
    static void load() throws IOException {
        File data = new File("./data/regiondata.dat");
        if(!data.exists())
            throw new IOException("Region keys file does not exist.");
        FileInputStream inputstream = new FileInputStream(data);
        int available = inputstream.available();
        if(available <= 0)
            throw new IOException("Region keys file is empty.");
        byte[] bytes = new byte[available];
        inputstream.read(bytes);
        InputStream stream = new InputStream(bytes);
        int entries = stream.available()/18;
        int region = -1;
        int[] keys = new int[4];
        while(entries-- > 0) {
            region = stream.readShort();
            for(int pos = 0; pos < 4; pos++)
                keys[pos] = stream.readInteger();
            keystore.put((short) region, keys);
        }
    }
    
    public static void main(String[] args) throws IOException {
        load();
    }
}
