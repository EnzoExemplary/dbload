package main;

import java.util.ArrayList;

public class Page {
	private boolean full = false;
	private int freeBytes = -1;
	private ArrayList<Record> records = new ArrayList<Record>();
	
	public Page(int size) {
		freeBytes = size;
	}
	
	public int getFreeBytes() {
		return freeBytes;
	}
	
	public boolean isFull() {
		return full;
	}
	
	public void addRecord(Record record) {
		records.add(record);
		freeBytes -= record.getSize();
		
		if(freeBytes >= 0) {
			full = true;
		}
	}
	
	//Return page in string format
	public String toString() {
		String string = "";
		
		for(Record record : records) {
			string += (record.toString() + "\n");
		}
		
		return string;
	}
}
