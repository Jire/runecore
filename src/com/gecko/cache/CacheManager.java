package com.gecko.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.gecko.Constants;

/**
 * RuneForge | 317
 * Cache.java
 * @version 1.0.0
 * @author SiniSoul (SiniSoul@live.com)
 */
public class CacheManager {
    
    /**
     * The cache index reference file.
     */
    private RandomAccessFile reference;
    
    /**
     * The cache index files.
     */
    private RandomAccessFile[] indexes;
    
    /**
     * 
     * @param file
     * @param cache
     * @return 
     */
    public synchronized byte[] getFile(int cache, int file) throws IOException {
        if(cache < 0 || cache >= indexes.length)
            throw new IOException("Cache - "+cache+" is non existant.");
        RandomAccessFile cachefile = indexes[cache++];
        cachefile.seek(file * 6);
        byte[] info = new byte[520];
        {
            int pos;
            for(int i = 0; i < 6; i += pos) {
                pos = cachefile.read(info, 0, 6);
                if(pos == -1) return null;
            }
        }
        int size = ((info[0] & 0xff) << 16) + ((info[1] & 0xff) << 8) + (info[2] & 0xff);
        int filepos = ((info[3] & 0xff) << 16) + ((info[4] & 0xff) << 8) + (info[5] & 0xff);
        byte[] bytes = new byte[size];
        int offset = 0;
        for(int i = 0; offset < size; i++) {
            if(filepos == 0)
                return null;
            reference.seek(filepos * 520);
            int readoffset = 0;
            int blocksize = size - offset;
            if(blocksize > 512)
                blocksize = 512;
            int pos;
            for(; readoffset < blocksize + 8; readoffset += pos)
            {
                pos = reference.read(info, readoffset, (blocksize + 8) - readoffset);
                if(pos == -1) return null;
            }
            int k2 = ((info[0] & 0xff) << 8) + (info[1] & 0xff);
            int l2 = ((info[2] & 0xff) << 8) + (info[3] & 0xff);
            int i3 = ((info[4] & 0xff) << 16) + ((info[5] & 0xff) << 8) + (info[6] & 0xff);
            int j3 =  info[7] & 0xff;
            if(k2 != file || l2 != i || j3 != cache)
                return null;
            if(i3 < 0 || (long) i3 > reference.length() / 520L)
                return null;
            for(int a = 0; a < blocksize; a++)
                bytes[offset++] = info[a + 8];
            filepos = i3;
        }
        return bytes;
    }
    
    /**
     * Cache Constructor;
     * Initializes the class by pre-loading the reference/cache indexes. 
     */
    public CacheManager() throws FileNotFoundException { 
        File directory = new File(Constants.CACHE_DIRECTORY);
        /* Find the highest idx file in the cache directory */
        int peak = 0;
        for(File file : directory.listFiles()) {
            String ext = Tools.fileExtension(file);
            if(!ext.startsWith("idx")) 
                continue;
            int idx = Integer.parseInt(ext.substring(ext.indexOf("x") + 1, ext.length()));
            peak = idx > peak ? idx : peak;
        }    
        /* Sets the reference file */
        try {
            reference = new RandomAccessFile(directory.getPath() + "/main_file_cache.dat2", "r");
        } catch (FileNotFoundException ex) {
            throw ex;
        }
        /* Set the cache indexes */
        indexes = new RandomAccessFile[peak];
        try {
            for(int i = 0; i < peak; i++) {
            	if(i == 29)
            		break;
                indexes[i] = new RandomAccessFile(directory.getPath() + "/main_file_cache.idx"+i, "r");
            }
        } catch (FileNotFoundException ex) {
            throw ex;
        }
    }
}
