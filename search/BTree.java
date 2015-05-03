package search;

public class BTree {
	
	private int flag; // flag used to represent if a new Node should be added
    int btreeLevel = 0; // the height of the B-Tree
    int btreeCount = 0; // the number of keys in the B-Tree
    int nodeSum = 0;  // the number of Nodes in the B-Tree
    private int level; // the height of the current Node
    private Node newTree; // the new Node
    public Node root; // the root Node
    Result result = new Result(); // Search result
    
    private String insValue; // the value to be inserted
    private String insKey; // the key to be inserted
    
	public class Node {
    	
        public static final int M = 9;
        
        
        public static final int MIN_KEY_AMOUNT = (M-1)/2;// Minimum of keys in a Node
    
        public int numKeys;//the number of Keys(Values) in a Node
        
        public String[] keys=new String[MIN_KEY_AMOUNT*2];
        public String[] values=new String[MIN_KEY_AMOUNT*2];
        public Node[] children = new Node[MIN_KEY_AMOUNT*2+1];                
    }
    
    public class Result {
        public int btreeDisp = 0;
        public String resultValue = null;  
    }
    
    
	
      
    /**
     * Construction
     * @param key the key of the first entry
     * @param value the value of the first entry
     */
    public BTree(String key, String value)
    {  	
    	insValue = value;
    	insKey = key;   	
    	root=new Node();   
    	root.numKeys = 1;   
    	root.keys[0] = insKey;   
    	root.values[0] = insValue;    
        btreeLevel++;   
        nodeSum++;
        btreeCount++;
    }
    
    /**
     * Search the key in the B-Tree
     * @param key the key to be searched
     * @param t where the search begins from
     * @return the value and the height
     */
    public Result search(String key,Node t){
        int i = 0,j,m;
        
        level = btreeLevel-1;
        while (level >= 0){
            j=t.numKeys-1;
            for(i=0; i<j; ) {
                m=(j+i)/2;               
                if(key.compareToIgnoreCase(t.keys[m].substring(t.keys[m].indexOf(":")+1,t.keys[m].length())) > 0)
                    i=m+1;
                else
                    j=m;
            }            

            // if it is found
            if (key.compareToIgnoreCase(t.keys[i].substring(t.keys[i].indexOf(":")+1,t.keys[i].length())) <= 0 
            		&& key.compareToIgnoreCase(t.keys[i].substring(0,t.keys[i].indexOf(":"))) >= 0){
                Result r=new Result();
                r.btreeDisp = i;                
                r.resultValue = t.values[i];
                return r;
            }
            
            if (key.compareToIgnoreCase(t.keys[i].substring(t.keys[i].indexOf(":")+1,t.keys[i].length())) > 0)
                i++;
           
            t = t.children[i];
            level--;
        }
        
        // if it is not found
        Result noresult=new Result();
        noresult.btreeDisp = -1;
        return noresult;
    }
    
    /**
     * insert a Node
     * @param key the key to be inserted
     * @param val the value to be inserted
     * @param t the Node to insert
     * @return new root
     */
    public Node insert(String key,String val,Node t){
        insValue=val;
        
        level=btreeLevel;
        internalInsert(key, t);
        if (flag == 1)  // if the node is full
            t=newRoot(t); 
        return t;
    }
    
    /**
     * insert a key into a Node
     * @param key the key to be inserted
     * @param t the Node to insert
     */
    private void internalInsert(String key,Node t){
        int i,j,m;
        
        level--;
        if (level < 0){
            newTree = null;
            insKey = key;
            btreeCount++;
            flag = 1; // set the flag
            return;
        }
        j=t.numKeys-1;
        for(i=0; i<j; ) {
            m=(j+i)/2;            
            if(key.compareToIgnoreCase(t.keys[m])>0)
                i=m+1;
            else
                j=m;
        }
        if (key.compareToIgnoreCase(t.keys[ i ]) == 0) { 
            flag = 0;
        }
        if (key.compareToIgnoreCase(t.keys[ i ]) > 0)
            i++;
        Node n = t.children[i];
        internalInsert(key,n);
        
        if (flag == 0)
            return;
        
        // new key to be inserted to the current Node
        if (t.numKeys < 2*Node.MIN_KEY_AMOUNT) { // if the Node is not full
            insInNode(t, i); // insert the key and value into the current Node
            flag = 0; // set the flag
        } else // if the Node is full
            splitNode(t, i); // split the Node
    }
    
    /**
     * insert a key and value into the current Node
     * @param t the Node to insert
     * @param i the position to insert
     */
    private void insInNode(Node t, int d){
        int i;
        
        // move all keys which are greater than the key to be inserted right, move all corresponding children right
        for(i = t.numKeys; i > d; i--){
            t.keys[ i ] = t.keys[i-1];
            t.values[ i ] = t.values[i-1];
            t.children[i+1] = t.children[ i ];
        }
        // insert the key and child
        t.keys[ i ] = insKey;
        if(newTree!=null){
            t.children[i+1] = newTree;
        }
        t.values[ i ] = insValue;
        t.numKeys++;
    }
    
    /**
     * Split a Node
     * @param t the Node to be split
     * @param i the position to insert
     */
    private void splitNode(Node t, int d){
        int i,j;
        Node temp;
        String tempK;
        String tempV;
        
        // create the new Node
        temp = new Node();
        
        if (d > Node.MIN_KEY_AMOUNT) { // to insert to the right half of the current Node            
        	// move (m-1) keys and children from (2*M-1) to (M+1) to the new Node, and leave a position to insert the new key
            for(i=2*Node.MIN_KEY_AMOUNT-1,j=Node.MIN_KEY_AMOUNT-1; i>=d; i--,j--) {
                temp.keys[j] = t.keys[ i ];
                temp.values[j] = t.values[ i ];
                temp.children[j+1] = t.children[i+1];
            }
            for(i=d-1,j=d-Node.MIN_KEY_AMOUNT-2; j>=0; i--,j--) {
                temp.keys[j] = t.keys[ i ];
                temp.values[j] = t.values[ i ];
                temp.children[j+1] = t.children[i+1];
            }           
            // move the rightmost child to be the leftmost child of the new Node
            temp.children[0] = t.children[Node.MIN_KEY_AMOUNT+1];            
            // insert the key and child in the new Node
            temp.keys[d-Node.MIN_KEY_AMOUNT-1] = insKey;
            if(newTree!=null)
            {
            	temp.children[d-Node.MIN_KEY_AMOUNT] = newTree;
            }
            temp.values[d-Node.MIN_KEY_AMOUNT-1] = insValue;
            // set the key and value which will be inserted into the upper level Node
            insKey = t.keys[Node.MIN_KEY_AMOUNT];
            insValue = t.values[Node.MIN_KEY_AMOUNT];
            
        } else {
        	// move m keys and children from (2*M-1) to (M) to the new Node
            for(i=2*Node.MIN_KEY_AMOUNT-1,j=Node.MIN_KEY_AMOUNT-1; j>=0; i--,j--) {
                temp.keys[j] = t.keys[ i ];
                temp.values[j] = t.values[ i ];
                temp.children[j+1] = t.children[i+1];
            }
            if (d == Node.MIN_KEY_AMOUNT) // to insert to the middle of the current Node
            {
            	// let the child to be inserted be the leftmost child of the new Node
                if(newTree!=null)
                {
                    temp.children[0] = newTree;
                }
            }
            // give the key and the value to be inserted to upper level Node
            else { // (d<Node.MIN_KEY_AMOUNT) to insert to the left half of the current Node
            	// move the current Node's rightmost child to the new Node's leftmost child
                temp.children[0] = t.children[Node.MIN_KEY_AMOUNT];
                // save the key and value to be inserted to the upper level Node
                tempK = t.keys[Node.MIN_KEY_AMOUNT-1];
                tempV = t.values[Node.MIN_KEY_AMOUNT-1];
                // move all keys which are greater than the key to be inserted right, move all corresponding children right
                for(i=Node.MIN_KEY_AMOUNT-1; i>d; i--) {
                    t.keys[ i ] = t.keys[i-1];
                    t.values[ i ] = t.values[i-1];
                    t.children[i+1] = t.children[ i ];
                }
                // insert the key and the right child to the Node
                t.keys[d] = insKey;
                if(newTree!=null)
                {
                    t.children[d+1] = newTree;
                }
                t.values[d] = insValue;
                // set the key and value to be inserted to the upper level Node
                insKey = tempK;
                insValue = tempV;
            }
        }
        t.numKeys =Node.MIN_KEY_AMOUNT;
        temp.numKeys = Node.MIN_KEY_AMOUNT;
        newTree = temp;
        nodeSum++;
    }
    
    /**
     * Create the new root
     * @param t current root
     * @return the new root
     */
    private Node newRoot(Node t){
        Node temp=new Node();
        temp.numKeys = 1;
        temp.children[0] = t;
        
        if(newTree!=null)
        {
            temp.children[1] = newTree;
        }
        
        temp.keys[0] = insKey;
        temp.values[0] = insValue;
        btreeLevel++;
        nodeSum++;
        return(temp);
    }   
}

