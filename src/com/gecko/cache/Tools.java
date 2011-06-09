package com.gecko.cache;

import java.io.File;
import java.io.IOException;

/**
 * Tools.java
 * RuneCore 525 www.runecore.org
 * @author Canownueasy, Sinisoul + Harry Andreas
 * 9 Jun 2011
 */
public class Tools {
    
    /**
     * Bundle many objects into a set of an object and a string. 
     * @param handlers The objects to bundle. Object[] = {(String), (Object)};
     * @return The bundled handlers.
     */
    public static Object[][] bundle(Object[]... objs) {
        Object[][] bundle = new Object[objs.length][2];
        System.arraycopy(objs, 0, bundle, 0, objs.length);
        return bundle;
    }
    
    /**
     * Gets the integer value of an IP string.
     * @param ip The IP to get the value for.
     * @return The integer representation of the IP.
     */
    public int getIPValue(String ip) {
        int value = 0, offset = 0;
        String[] data = ip.replace(".", " ").split(" ");
        for(String s : data) {
            value += (Integer.parseInt(s) << (offset++ * 8));
        }
        return value;
    }
    
    /**
     * Formats a directory string to be proper and free of error.
     * -Replaces '\\' character to become '/'
     * -Places '/' character at end of string if one did not already exist.
     * @param directory Directory string to format.
     * @return The formated directory string.
     */
    public static String formatDirectory(String directory) {
        String f = directory.replace('\\', '/');
        return f.length() == 0 ? f :
               f.charAt(f.length() - 1) == '/' ? f : (f += '/');
    }

    /**
     * Creates a directory; using the java.io.File object.
     * @param directory Directory to create.
     * @return If the directory was successfully created.
     */
    public static boolean createDirectory(String directory) {
        return new File(directory).mkdir();
    }

    /**
     * Creates a file.
     * @param directory Of the file to create.
     * @param name Of the file to create; extension included.
     * @see create(java.io.File)
     * @return If the file was successfully created; or if the file already existed.
     */
    public static boolean create(String directory, String name) {
        try {
            return exists(directory, name) == true ? true :
                   create(new File(formatDirectory(directory) + name));
        } catch(IOException ex) {
            return false;
        }
    }

    /**
     * Creates a file; using the java.io.File object.
     * @param file File to create.
     * @return If the file was successfully created.
     * @throws IOException Thrown if file was not successfully created.
     */
    public static boolean create(File file) throws IOException {
        return file.createNewFile();
    }

    /**
     * Deletes a file.
     * @param directory
     * @param name
     * @see delete(java.io.File)
     * @return If the file was successfully deleted; or the file never existed.
     */
    public static boolean delete(String directory, String name) {
        return !exists(directory, name) == true ? true :
               delete(new File(formatDirectory(directory), name));
    }

    /**
     * Deletes a file; using the java.io.File object.
     * @param file File to delete.
     * @return If the file was successfully deleted.
     */
    public static boolean delete(File file) {
        return file.delete();
    }
    
    /**
     * Checks if a file exists.
     * @param directory Of the file to check.
     * @param name Of the file to check.
     * @return Returns if the file exists within the directory.
     */
    public static boolean exists(String directory, String name) {
        return exists(new File(formatDirectory(directory) + name));
    }

    /**
     * Checks if a file exists; using the java.io.File object.
     * @param file File to check.
     * @return Returns if the file exists within the directory.
     */
    public static boolean exists(File file) {
        return file.exists();
    }

    /**
     * Gets the extension of a file.
     * @param file File to get the extension from.
     * @return The file extension of the file.
     */
    public static String fileExtension(File file) {
        String ext = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        return ext != null ? ext : "";
    }
}
