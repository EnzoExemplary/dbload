package main;

import java.util.ArrayList;

public class HeapFile {
	private PageDirectory pageDirectory;
	private int recordCount = 0;
	private ArrayList<Object> variableTypes = new ArrayList<Object>();
	private String searchTextBinary = "";
	private final char INPUT_DELIMITER = ',';
	private final String BINARY_SPACE = "00100000";
	private final char BINARY_DELIMITER = '$';
	private final int DATE_TIME_COLUMN = 1;
	private final int SENSOR_ID_COLUMN = 7;
	
	public HeapFile(int pageSize, String searchTextBinary) {
		pageDirectory = new PageDirectory(pageSize);
		this.searchTextBinary = searchTextBinary;
		
		//Manually load in variable types
		variableTypes.add(new Integer(0));
		variableTypes.add(new String(""));
		variableTypes.add(new Integer(0));
		variableTypes.add(new String(""));
		variableTypes.add(new Integer(0));
		variableTypes.add(new String(""));
		variableTypes.add(new Integer(0));
		variableTypes.add(new Integer(0));
		variableTypes.add(new String(""));
		variableTypes.add(new Integer(0));
	}

	//Get total count of all records in heap
	public int getRecordCount() {
		return recordCount;
	}
	
	//Get Total count of all pages used in heap
	public int getPagesUsed() {
		return pageDirectory.getPagesUsed();
	}
	
	//Return heap in string format
	public String toString() {
		String string = "";
		
		string = pageDirectory.toString();
		
		return string;
	}
	
	//Separate all fields from record String
	private void separateString(String string, char delimiter, ArrayList<String> list) {
		int startString = 0;
		int endString = 0;
		
		while(!(endString >= string.length())) {
			
			while(endString != (string.length()) && string.charAt(endString) != delimiter) {
				++endString;
			}
			list.add(string.substring(startString, endString));
			startString = ++endString;
		}
	}

	//Load full binary record into heap - Returns String of record if record matches search 
	public String loadBinaryRecord(String line) {
		String matchingRecord = "";
		ArrayList<String> separatedBinary = new ArrayList<String>();
		
		separateString(line, BINARY_DELIMITER, separatedBinary);
		Record record = new Record(separatedBinary);
		pageDirectory.addRecord(record);
		
		//If record date_time & sensor_id columns match search, convert record and return as matching record
		String relevantFields = separatedBinary.get(DATE_TIME_COLUMN) + BINARY_SPACE + separatedBinary.get(SENSOR_ID_COLUMN);
		if(relevantFields.equals(searchTextBinary)) {
			
			int i = 0;
			while(i < variableTypes.size()) {
				if(variableTypes.get(i) instanceof String) {
					matchingRecord += Util.fromStringBinary(separatedBinary.get(i));
				} else {
					matchingRecord += Util.fromIntBinary(separatedBinary.get(i));
				}
				
				++i;
				//Add divider for fields if not on last field
				if(i < variableTypes.size()) {
					matchingRecord += " | ";
				}
			}
		}
		
		return matchingRecord;
	}
	
	
}
