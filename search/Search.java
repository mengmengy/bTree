package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Search {		
	
	// Build the B-Tree according to the index file, let the user provide a search request, and print the search result
	public static void main(String[] args) throws IOException{		
		// open the index file
		File file = new File("processed/index.txt");   
	    BufferedReader br = new BufferedReader(new FileReader(file));
		
	    // get the information from each index
	    String index = br.readLine();
	    String range = index.substring(0, index.lastIndexOf(":"));
	    String subFileName = index.substring(index.lastIndexOf(":")+1, index.length());
	    
	    // according to the index to build a B-tree
		BTree bt = new BTree(range, subFileName); // root		
		while((index = br.readLine()) != null) {// insert other keys into the B-Tree
			range = index.substring(0, index.lastIndexOf(":"));
			subFileName = index.substring(index.lastIndexOf(":")+1, index.length());
		    bt.root = bt.insert(range, subFileName, bt.root);
		}
		br.close();
		
		String search;
		
		// Read a request to search
		while(true) {
			System.out.println("Please input the name you want to search. (Format like: Aaron,Briese)");
			BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
			
			search=strin.readLine();
			long startTime=System.currentTimeMillis();			
			bt.result = bt.search(search, bt.root); // search the input name in B-Tree
			
			if(bt.result.btreeDisp == -1) // not found
				System.out.println("Sorry, didn't find the record you need.");
			else {
				// open the file and read all data the RAM
				File target = new File("processed/" + bt.result.resultValue);   
			    BufferedReader reader = new BufferedReader(new FileReader(target));
				String tmpString;
				ArrayList<String> tmp = new ArrayList<String>();			    
			    while((tmpString = reader.readLine()) != null){
		            tmp.add(tmpString);
		        }
		        reader.close();
		        String[] record = new String[tmp.size()];
			    tmp.toArray(record);
		        
			    // search the input name
			    int result = BinarySearch.searchValue(record, search);
			    
			    if(result == -1) 
			    	System.out.println("Sorry, didn't find the record you need.");
			    else 
			    	System.out.println(record[result]);
			}
			long endTime=System.currentTimeMillis();
			System.out.println("The total searching time is: "+(endTime-startTime)+"ms");
		}	    	
    }
}    
