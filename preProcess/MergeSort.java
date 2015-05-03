package preProcess;

public class MergeSort{
	public static void mergeSort(Comparable [] arr){
		Comparable[] tmp = new Comparable[arr.length];
		mergeSort(arr, tmp,  0,  arr.length - 1);
	}

	private static void mergeSort(Comparable [] arr, Comparable [] tmp, int left, int right){
		if( left < right ){
			int mid = (left + right) / 2;
			mergeSort(arr, tmp, left, mid);
			mergeSort(arr, tmp, mid + 1, right);
			merge(arr, tmp, left, mid + 1, right);
		}
	}

    private static void merge(Comparable[] arr, Comparable[] tmp, int left, int right, int rightEnd ){
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(arr[left].compareTo(arr[right]) <= 0)
                tmp[k++] = arr[left++];
            else
                tmp[k++] = arr[right++];

        while(left <= leftEnd)    // Copy rest of first half
            tmp[k++] = arr[left++];

        while(right <= rightEnd)  // Copy rest of right half
            tmp[k++] = arr[right++];

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
        	arr[rightEnd] = tmp[rightEnd];
    }
 }