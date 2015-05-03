package preProcess;

import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  
import java.util.ArrayList;

public class Process {
	
	public static final int MAX_RECORDS=500000;
	
	// Open a source file, split it into several parts with the number of MAX_RECORDS, 
	// sort each part using merge-sort, and save the sorted data into processed folder
    public static void input(String name) throws IOException {
    	// open the file according to the file name
    	String fileName = "input/" + name;
    	File file = new File(fileName);           
    	BufferedReader input = new BufferedReader(new FileReader(file));
        
        ArrayList<String> tmp = new ArrayList<String>();
        String tmpString = null;
        int count = 0, k = 0; //counters
        
        while((tmpString = input.readLine()) != null){            
        	tmp.add(tmpString);
            count++;
            // in order to avoid "out of memory" error, split the original file into several parts which have MAX_RECORDS records. 
            // Each part will be sorted separately
            // when the counter equals to MAX_RECORDS, sort the data using Merge-sort and output the sorted data into the processed folder
            if(count == MAX_RECORDS){
            	String[] result = new String[tmp.size()];
    	        tmp.toArray(result); 
    	        MergeSort.mergeSort(result); // call MergeSort.sort() to sort the data
    	        String outputName = name.substring(0, name.indexOf(".")) + "-" + k + ".txt";
    	        k++;
    	        count = 0;
    	        tmp.clear();
    	        output(outputName, result); // call output() to save the sorted data into the processed folder  	        
            }            
        }
        
        // output the last part of the file. This part may have less than MAX_RECORDS items
        if(!tmp.isEmpty()){	        
	        String[] result = new String[tmp.size()];
	        tmp.toArray(result);      
	        MergeSort.mergeSort(result); // sort the data
	        String outputName = name.substring(0, name.indexOf(".")) + "-" + k + ".txt";
	        k++;
	        output(outputName, result); // call output() to save the sorted data into the processed folder
        }
        
        // close BufferedReader
        input.close();                
    }
    
    
	
    // Save the sorted data into the processed folder
	// result stores the sorted data
    public static void output(String name, String[] result) throws IOException {       
    	String fileName = "processed/" + name; 
        FileWriter output = new FileWriter(fileName);
        
        for (int lineCounter = 0; lineCounter < result.length; ++lineCounter)  {  
            output.append(result[lineCounter] + "\r\n");  
        }  
        output.flush();  
        output.close();
    }
    
	//pre-process the original data file
	public static void main(String[] args) throws IOException {
		
		long startTime=System.currentTimeMillis();
		// get all names of the files in the input folder
		File input=new File("input\\");
		String inputFile[];
		inputFile=input.list();
		
		// delete all files in the processed folder
		File output=new File("processed\\");		
		File[] delete;		
		delete = output.listFiles();
		for(int i=0; i<delete.length; i++){
			delete[i].delete();
		}
		
		// process every file in the input folder
		for(int i=0; i<inputFile.length; i++){						
			input(inputFile[i]);
		}
		
		// get all names of the files in the processed folder
		String outputFile[];
		outputFile=output.list();
		
		// open every file
		ArrayList<File> list = new ArrayList<File>();
		for(int j=0; j<outputFile.length; j++){		
			File f = new File("processed/"+ outputFile[j]);
			list.add(f);
		}
		
		// sort all data using External-sort
		ExternalSort.merge(list);
		
		// delete intermediate files
    	for(int i=0; i<list.size(); i++){							
			list.get(i).delete();
		}
    	long endTime=System.currentTimeMillis();
    	System.out.println("The total pre-processing time is "+ ((endTime-startTime)/1000) +" seconds");
    }
}