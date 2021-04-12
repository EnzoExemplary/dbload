import java.util.ArrayList;

public class HeapFile {
	private PageDirectory pageDirectory;
	private int recordCount = 0;
	private ArrayList<String> headers = new ArrayList<String>();
	private ArrayList<Object> variableTypes = new ArrayList<Object>();
	private final char INPUT_DELIMITER = ',';
	
	public HeapFile(int pageSize) {
		pageDirectory = new PageDirectory(pageSize);
	}
	
	//Load record from string into heap (as binary)
	//First record required to load variable types (excludes headers)
	public void loadRecord(String record, boolean firstRecord) {
		ArrayList<String> originalFields = new ArrayList<String>();
		ArrayList<String> binaryFields = new ArrayList<String>();

		//Separate all fields from record String
		separateString(record, INPUT_DELIMITER, originalFields);

		//Determine variable types if first record input
		if(firstRecord) {
			for(String field : originalFields) {
				try {
					Integer.parseInt(field);
					variableTypes.add(new Integer(0));
				} catch(NumberFormatException e) {
					variableTypes.add(new String(""));
				}
			}
		}
		
		//Convert fields to binary based on variable type
		for(int i = 0; i < variableTypes.size(); ++i) {
			//Convert to integer then binary if field type is int
			if(variableTypes.get(i) instanceof Integer) {
				try {
					int field = Integer.parseInt(originalFields.get(i));
					String binary = Long.toBinaryString(field & 0xffffffffL | 0x100000000L ).substring(1);
					binaryFields.add(binary);
				} catch(NumberFormatException e) {
					System.err.println("Error loading record, could not load type: Integer");
			        System.exit(1);
				}
			} else { //Else convert String to binary
				String field = originalFields.get(i);
				byte[] bytes = field.getBytes();
				StringBuilder binaryBuilder = new StringBuilder();
				
				for(byte b : bytes) {
					int value = b;
					int j = 0;
					while(j < 8) {
						binaryBuilder.append((value & 128) == 0 ? 0 : 1);
						value <<= 1;
						++j;
					}
				}
				
				field = binaryBuilder.toString();
				binaryFields.add(field);
				
			}
		}
		
		//Create new record from binary fields and add to pageDirectory
		Record newRecord = new Record(binaryFields);
		pageDirectory.addRecord(newRecord);
		++recordCount;
	}
	
	//Load headers 
	public void loadHeaders(String header) {
		separateString(header, INPUT_DELIMITER, headers);
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
			
			while(endString != (string.length()) && string.charAt(endString) != INPUT_DELIMITER) {
				++endString;
			}
			list.add(string.substring(startString, endString));
			startString = ++endString;
		}
	}
	
	
}
