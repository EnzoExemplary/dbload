package main;

import java.util.ArrayList;

public class Record {
	private final char DELIMITER = '$';
	private String fields = "";
	private int bytes = 0;
	
	public Record(ArrayList<String> fields) {
		for(String field : fields) {
			countBytes(field);
			this.fields += (field + DELIMITER);
		}
	}
	
	//Count bytes in field (rounded up)
	private void countBytes(String field) {
		if(field.length() % 4 > 0) {
			bytes += ((field.length()) / 4 + 1);
		} else {
			bytes += (field.length() / 4);
		}
	}
	
	public int getSize() {
		return bytes;
	}
	
	public String getField(int fieldNum) {
		String field = "";
		int fieldStart = 0;
		int fieldEnd = 0;
		
		int i = 0;
		while(i < fieldNum) {
			
			while(fields.charAt(fieldStart) != DELIMITER && !(fieldStart > fields.length())) {
				++fieldStart;
			}
			
			++fieldStart;
			++i;
		}
		
		fieldEnd = fieldStart;
		while(fields.charAt(fieldEnd) != DELIMITER && !(fieldEnd > fields.length())) {
			++fieldEnd;
		}
		
		field = fields.substring(fieldStart, fieldEnd);
		
		return field;
	}
	
	//Return record in string format
	public String toString() {
		return fields;
	}

}
