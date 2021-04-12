package main;

import java.util.Arrays;

public class Util {

	//Convert searchText to a binary string
	public static String toBinary(String binaryString) { 
		String binary = "";
		
		byte[] bytes = binaryString.getBytes();
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

	//Convert binaryString to text string
	public static String fromStringBinary(String field) {
		String string = ""; 
		
		int beginIndex = 0;
		int endIndex = 7;
		while(!(endIndex > field.length())) {
			int charCode = fromIntBinary(field.substring(beginIndex, endIndex));
			string += (new Character((char)charCode));
			
			beginIndex += 8;
			endIndex += 8;
		}
		
		return string;
	}
	
	//Convert binaryString to integer
	public static int fromIntBinary(String field) {
		return Integer.parseInt(field, 2);
	}
	
}
