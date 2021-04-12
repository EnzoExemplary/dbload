import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class dbload {

	public static void main(String[] args) {
	    if (args.length < 3) {
	        System.err.println("ERROR: Incorrect number of arguments");
	        System.exit(1);
	    }
		
	    //Load in args & initialise heap
		String commandType = args[0];
		String pageSizeStr = args[1];
		String filename = args[2];
		HeapFile heap;
		
		if(commandType.equals("-p")) {
			try {
				//Parse page size and create heap
				int pageSize = Integer.parseInt(pageSizeStr);
				heap = new HeapFile(pageSize);
				
				//Start Timer
				long startTime = System.nanoTime();
				
				//Initialise variables required to load records to heap
				FileReader fr = new FileReader(filename);
		    	BufferedReader br = new BufferedReader(fr);
		    	String line = null;
		    	boolean header = true;
		    	boolean firstRecord = false;

				//Load headers and then each following record into heap
		    	while((line = br.readLine()) != null) {
		    		if(header) {
		    			heap.loadHeaders(line);
		    			header = false;
		    			firstRecord = true;
		    		} else {
		    			heap.loadRecord(line, firstRecord);
		    			if(firstRecord) {
		    				firstRecord = false;
		    			}
		    		}
		    	}
		    	
		    	long endTime = System.nanoTime();
		    	double runTime = ((double)(endTime - startTime)) / Math.pow(10, 6);
		    	
		    	//Write heap to file and close readers
		    	writeToFile(heap, pageSize, runTime);
		    	br.close();
		    	fr.close();
		    
		    //Catch possible exceptions
			}catch (NumberFormatException e) {
				System.err.println("Invalid page size");
		        System.exit(1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
		        System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
		        System.exit(1);
			}
			
		}else{
			System.err.println("Unknown command type");
	        System.exit(1);
		}
	}
	
	
	//Write give heap to file as "heap.pageSize.txt"
	private static void writeToFile(HeapFile heap, int pageSize, double runTime) {
		try {
			String filename = "heap." + pageSize + ".txt";
			FileWriter fw = new FileWriter(filename);
			
			fw.write("Num. Records: " + heap.getRecordCount() 
						+ " | Num. Pages Used: " + heap.getPagesUsed()
						+ " | Time Taken (milliseconds): " + runTime + " ms\n\n");
			fw.write(heap.toString());
			
			fw.close();
	
		} catch (IOException e) {
			e.printStackTrace();
	        System.exit(1);
		}
	}
}
