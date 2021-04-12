package main;

public class PageDirectory {
	
	//Variables
	private final int NUM_PAGES = 12; 
	private Page[] pages = new Page[NUM_PAGES];
	private int mostFreeBytes = -1;
	private int pageSize = -1;
	private boolean full = false;
	private PageDirectory nextDirectory = null;
	private int pagesUsed = 0;
	
	//Constructor
	public PageDirectory(int pageSize) {
		for(int i = 0; i < NUM_PAGES; i++) {
			pages[i] = new Page(pageSize);
		}
		
		this.pageSize = pageSize;
		mostFreeBytes = pageSize;
	}
	
	//Update highest number of free bytes in current directory
	private void updateFreeBytes() {
		mostFreeBytes = 0;
		
		//Loop through pages to find most free bytes
		for(Page page : pages) {
			int freeBytes = page.getFreeBytes();
			if(freeBytes == pageSize) {
				mostFreeBytes = freeBytes;
				break;
			} else if(freeBytes > mostFreeBytes) {
				mostFreeBytes = freeBytes;
			}
		}
		
		if(mostFreeBytes >= 0) {
			full = true;
		}
	}
	
	//Check if directory is full
	public boolean isFull() {
		return full;
	}
	
	//Add a record into the current directory, or into a new one
	public void addRecord(Record record) {
		int recordSize = record.getSize();
		
		// Add to next directory if not enough space in current directory
		if(recordSize > mostFreeBytes) {
			if(nextDirectory == null) {
				nextDirectory = new PageDirectory(pageSize);
			}
			
			nextDirectory.addRecord(record);
		} else { //Otherwise check pages in current directory for best fit
			int pageNum = -1;
			
			
			//Find first page that can fit record
			int i = 0;
			while(i < pages.length && pageNum == -1) {
				if(pages[i].getFreeBytes() >= record.getSize()) {
					pageNum = i;
				}
				
				++i;
			}
			
			//Loop through pages in current directory to find best fit for record
			for(int j = i; j < pages.length; j++) {
				int freeBytes = pages[j].getFreeBytes();
				if(freeBytes == recordSize) {
					pageNum = j;
					break;
				} else if(freeBytes < pages[pageNum].getFreeBytes() && freeBytes > recordSize) {
					pageNum = j;
				}
			}
			
			//Increment pages used if page was empty
			if(pages[pageNum].getFreeBytes() == pageSize) {
				++pagesUsed;
			}
			
			//Add record to page
			pages[pageNum].addRecord(record);
			updateFreeBytes();
		}
	}
	
	//Get total count of pages used from all directories
	public int getPagesUsed() {
		if(nextDirectory == null) {
			return pagesUsed;
		} else {
			return pagesUsed + nextDirectory.getPagesUsed();
		}
		
	}
	
	//Return page directory in string format
	public String toString() {
		String string = "";
		
		for(Page page : pages) {
			string += (page.toString() + "\n");
		}
		
		if(nextDirectory != null) {
			string += ("\n" + nextDirectory.toString());
		}
		
		return string;
	}
	
}
