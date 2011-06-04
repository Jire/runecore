package com.gecko.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public final class LineCounter {
	
	private static final String PROJECT_PATH = "./src";
	
	private static final String SECURITY_KEY = "73e98a75643c98f638716ec71707fec0";
	
	private static final int PROJECT_ID = 120;
	
	public static void main(String[] args) {
		System.out.println("Loading files...");
		List<File> files = listRecursive(new File(PROJECT_PATH), new Filter<File>() {
			@Override
			public boolean accept(File t) {
				return t.getName().endsWith(".java") || t.getName().endsWith(".lua") || t.getName().endsWith(".rb") || t.getName().endsWith(".js");
			}
		});
		System.out.println("Parsing line count...");
		int lineCount = 0;
		for(File file : files) {
			lineCount += lineCount(file);
		}
		System.out.println(lineCount+" lines in "+files.size()+" files");
		doUpdate(files.size(), lineCount);
	}
	
	public static void doUpdate(int fileCount, int lineCount) {
		try {
			URL url = new URL("http://nikkii.us/projectsig/projectinfo.php?set&projectid="+PROJECT_ID+"&securitykey="+SECURITY_KEY+"&value="+fileCount+","+lineCount);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String resp = reader.readLine();
			if(resp.equals("SUCCESS")) {
				System.out.println("Successfully updated.");
			} else {
				System.out.println("Failed to update, check your security key");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public interface Filter<T> {
		public boolean accept(T t);
	}

	public static LinkedList<File> listRecursive(File file, Filter<File> filter) {
		LinkedList<File> files = new LinkedList<File>();
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(listRecursive(f, filter));
			} else {
				if(filter.accept(f))
					files.add(f);
			}
		}
		return files;
	}
	
	public static int lineCount(File file) {
		int count = 0;
		try {
			LineNumberReader ln = new LineNumberReader(new FileReader(file));
			while(true) {
				String line = ln.readLine();
				if(line == null)
					break;
				if(!line.trim().equals(""))
					count++;
			}
			ln.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
}