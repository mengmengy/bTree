package search;

public class BinarySearch {
 
	/**
     * Binary Search
     * @param data a String array contains data to be searched in
     * @param indexValue the target string
     * @return the index of the target string, return -1 if not found
     */
	public static int searchValue(String [] data, String indexValue) {
        int start = 0;
        int middle = 0;
        int end = data.length - 1;
         
        while (start <= end) {
            middle = (start + end)/2;
                        
            if (data[middle].substring(0, data[middle].lastIndexOf(",")).compareToIgnoreCase(indexValue) < 0) {
                start = middle + 1;
            } else if (data[middle].substring(0, data[middle].lastIndexOf(",")).compareToIgnoreCase(indexValue) > 0) {
                end = middle - 1;
            } else {
                return middle;
            }                         
        }   
        return -1;
    }
}