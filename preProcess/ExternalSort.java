package preProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class ExternalSort {
	
	//the maximum number of records can be read in the input buffer
	public static final int INPUT_BUFFER_SIZE = 1000 ;
	
	//the size of the output buffer
	public static final int OUTPUT_BUFFER_SIZE = 10000;
	
	//Read the data from several sorted files, sort all the data together, and output the result into processed folder
    //files contains a set of files
	static void merge(ArrayList<File> files) throws IOException {
    	BufferedReader[] br = new BufferedReader[files.size()];
        ArrayList<Queue<String>> st = new ArrayList<Queue<String>>();
        String[] filesStr= new String[files.size()];
        
        // read data from each file, and store it in the corresponding input buffer. Each input buffer is a queue
        for(int i=0;i<files.size();i++) {
        	br[i] = new BufferedReader(new FileReader(files.get(i)));
        	Queue<String> str = new LinkedList<String>();
        	for(int j=0;j<INPUT_BUFFER_SIZE;j++){
        		String s = br[i].readLine();
        		if (s != null){
        			str.add(s);
        		}
        		else
        			break;
        	}
        	
        	// store all the first element of each input buffer in filesStr[]
        	filesStr[i] = str.poll();
        	st.add(str);
        }
               
        File file = new File("processed/index.txt"); // create the index file  
        BufferedWriter myIndex = new BufferedWriter(new FileWriter(file));        
        int k = 0, count = 0; // counters
        String[] outputBuffer= new String[OUTPUT_BUFFER_SIZE];       
        
        // k-way merge-sort, depends on how many input file
        // select the MIN string from filesStr[], and move it to the output buffer
        while (!stringAllNull(filesStr)){
        	int index = stringMin(filesStr); // call stringMin() to find the index of the MIN string
        	outputBuffer[k++] = filesStr[index]; // move it to the output buffer     	
        	
        	// if the output buffer is full, save the data from the buffer to a file in processed folder 
        	//and write an index of this file in index.txt
        	if(k == OUTPUT_BUFFER_SIZE){
       		
	        	BufferedWriter bw = new BufferedWriter(new FileWriter("processed/subfile" + count + ".txt"));
	        	
	        	// create the index
	        	String startName = outputBuffer[0].substring(0, outputBuffer[0].indexOf(",", outputBuffer[0].indexOf(",")+1));
	        	String endName = outputBuffer[OUTPUT_BUFFER_SIZE-1].substring(0, outputBuffer[OUTPUT_BUFFER_SIZE-1].indexOf(",", outputBuffer[OUTPUT_BUFFER_SIZE-1].indexOf(",")+1));
	        	String temp = startName + ":" + endName + ":" + "subfile" + count + ".txt";
	        	count++;
	        	
	        	// save the data from the buffer to a file in processed folder
	        	for (int j = 0; j < OUTPUT_BUFFER_SIZE; j++) {
	        		bw.write(outputBuffer[j]);
	            	bw.write("\r\n");
                }
	        	
	        	// write the index of the output file in index.txt
	        	myIndex.write(temp);
	        	myIndex.write("\r\n");
	        	bw.close();
	        	k = 0;
        	}
        	
        	// move the next element from input buffer to filesStr[]
        	if(!st.get(index).isEmpty()){
        		filesStr[index] = st.get(index).poll();
        	}
        	else // if the input buffer is empty, and the file has no more data, set a "|"
        		{
        		String s = br[index].readLine();
        		if(s == null){
        			filesStr[index] = "|"; // use "|" to represent that all data in this file has been processed
        		}
        		else {// if the input buffer is empty, refill it
        			filesStr[index] = s;
        			for(int j=0;j<INPUT_BUFFER_SIZE;j++){
        				s = br[index].readLine();
                		if (s != null){
                			st.get(index).add(s);			
                		}
                		else
                			break;
                	}
        		}
        	}
        }
        
        // when all data has been processed, save the last part of data from the output buffer
        // save the data from the buffer to a file in processed folder, and write an index of this file in index.txt
        if(outputBuffer.length > 0){
        	String startName = outputBuffer[0].substring(0, outputBuffer[0].indexOf(",", outputBuffer[0].indexOf(",")+1));
        	String endName = outputBuffer[k-1].substring(0, outputBuffer[k-1].indexOf(",", outputBuffer[k-1].indexOf(",")+1));
        	String temp = startName + ":" + endName + ":" + "subfile" + count + ".txt";
	        BufferedWriter bw = new BufferedWriter(new FileWriter("processed/subfile" + count + ".txt"));
	    	for (int j = 0; j < k; j++) {
	    		bw.write(outputBuffer[j]);
	        	bw.write("\r\n");
	        }
	    	myIndex.write(temp);
	    	myIndex.write("\r\n");
	    	bw.close();   	
        }
        
        // close all files
        myIndex.close();
    	for(int i=0;i<files.size();i++){
        	br[i].close();
        }
    }

	// find the MIN string, and return the index of that string
	public static int stringMin(String[] s){
    	int smin = 0;
    	for (int i=1;i<s.length;i++){
    		if (s[i].compareTo(s[smin])<0)
    			smin = i;
    	}
    	return smin;
    }
	
	// check if all files have been processed
	public static boolean stringAllNull(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (!(array[i].compareTo("|") == 0)) {
                return false;
            }
        }
        return true;
    }
}
