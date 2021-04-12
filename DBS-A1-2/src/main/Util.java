package main;

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
	
}
