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
		
		String searchText = args[0].toString();
		String pageSizeStr = args[1];
		String filename = ("heap." + pageSizeStr + ".txt");
		HeapFile heap;
		
		//Convert Search Text to appropriate binary
		int startIndex = (searchText.length() - 1);
		int endIndex = searchText.length();
		while(searchText.charAt(startIndex) != ' ') {
			--startIndex;
		}
		int sensorID = Integer.parseInt(searchText.substring(++startIndex, endIndex));
		String searchTextBinary = (Util.toBinary(searchText.substring(0, startIndex)) 
				+ Util.toBinary(sensorID)); 
		
		try {
			//Start Timer
			long startTime = System.nanoTime();
			
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
	    		} else if(line.equals("") || line.equals("\n")) {
	    			++i;
	    		} else {
	    			String record = heap.loadBinaryRecord(line);
	    			if(!(record.equals(""))) {
	    				matchingRecords.add(record);
	    			}
	    		}
	    	}
			
	    	//Print out matching records to console (if any)
	    	System.out.println("Records found matching search '" + searchText + "':");
	    	for(String record : matchingRecords) {
	    		System.out.println(record);
	    	}
	    	
	    	//End Timer
	    	long endTime = System.nanoTime();
	    	double runTime = ((double)(endTime - startTime)) / Math.pow(10, 6);
	    	
	    	System.out.println("\nTime to complete: " + runTime + "ms");
	    	
	    	br.close();
	    	fr.close();
			
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
}
