package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class dbquery {
	
	public static void main(String[] args) {
	    if (args.length != 2) {
	        System.err.println("ERROR: Incorrect number of arguments");
	        System.exit(1);
	    }
		
		String searchText = args[0];
		String pageSizeStr = args[1];
		String filename = "heap." + pageSizeStr + ".txt";
		String searchTextBinary = toBinary(searchText);
		HeapFile heap;
		
		try {
			//Parse page size
			int pageSize = Integer.parseInt(pageSizeStr);
			heap = new HeapFile(pageSize, searchTextBinary);
			ArrayList<String> matchingRecords = new ArrayList<String>();
			
			//Initialise variables required to load records to heap
			FileReader fr = new FileReader(filename);
	    	BufferedReader br = new BufferedReader(fr);
	    	String line = null;
			
			//Load each record into heap, collecting any matching records
	    	int i = 0;
	    	while((line = br.readLine()) != null) {
	    		//Ignore first two lines
	    		if(i < 2) {
	    			++i;
	    		} else {
	    			String record = heap.loadBinaryRecord(line, searchTextBinary);
	    			if(!(record.equals(""))) {
	    				matchingRecords.add(record);
	    			}
	    		}
	    	}
			
			
		} catch (NumberFormatException e) {
			System.err.println("Invalid page size");
	        System.exit(1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static String toBinary(String searchText) { 
		String binary = "";
		
		byte[] bytes = searchText.getBytes();
		StringBuilder binaryBuilder = new StringBuilder();
		
		for(byte b : bytes) {
			int value = b;
			
			int i = 0;
			while(i < 8) {
				binaryBuilder.append((value & 128) == 0 ? 0 : 1);
				value <<= 1;
				++i;
			}
		}
		
		binary = binaryBuilder.toString();
		return binary;
	}

}
