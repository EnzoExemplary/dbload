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
	
	public static String toBinary(int value) {
		String binary = Long.toBinaryString(value & 0xffffffffL | 0x100000000L ).substring(1);
		return binary;
	}

	//Convert binaryString to original text string
	public static String fromStringBinary(String field) {
		String output = "";
		int beginIndex = 0;
		int endIndex = 8;
		
		while(endIndex <= field.length()) {
			String substring = field.substring(beginIndex, endIndex);
			int charCode = Integer.parseInt(substring, 2);
			output += (char)charCode;
			
			beginIndex += 8;
			endIndex += 8;
		}
				
		return output;
	}
	
	//Convert binaryString to integer
	public static int fromIntBinary(String field) {
		return Integer.parseInt(field, 2);
	}
	
}
