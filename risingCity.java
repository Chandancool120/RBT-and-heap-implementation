import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//class that represents node of the heap
class Node{
	int buildingNum;
	int executed_time;
	int total_time;
	int initDay;

	public Node() {
		
	}
	//constructor to initialize the variables of the node
	public Node(int buildingNum,int executed_time,int total_time,int initDay) {
		this.buildingNum = buildingNum;
		this.executed_time = executed_time;
		this.total_time = total_time;
		this.initDay = initDay;

	}
	
}

//risingCity main class
public class risingCity {

	private static int universalTimer = -1;
	private static int heapSize=0;
	private int maxSize = 2000;
	private Node[] heap = new Node[maxSize+1];
	
	//constructor to initialize the heap
	public risingCity(Node node) {
		heap[0] = node;
	}
	
	public int parent(int i) {
		return i/2;
	}
	
	public int leftChild(int i) {
		return 2*i;
	}
	
	public int rightChild(int i) {
		return (2*i)+1;
	}
	
	//swap nodes of the heap
	public void swap(int x, int y) {
		Node temp = heap[x];
		heap[x]=heap[y];
		heap[y] = temp;
	}
	
	//insert a node into the heap, for every insertion the node that is changed is compared with parent and swapped 
	//(adjusted according to executed time and building number of node)
	public void insert(Node node) 
    { 
		heapSize +=1;
        heap[heapSize] = node; 
        int position=heapSize;
        while (position!=parent(position) && heap[position].executed_time <= heap[parent(position)].executed_time) {        	
        	if(heap[position].executed_time == heap[parent(position)].executed_time) {
        		if(heap[position].buildingNum < heap[parent(position)].buildingNum) {
        			swap(position, parent(position)); 
               
        		}
        	}
        	else {
        		swap(position, parent(position)); 
                 
        	}
        	position = parent(position);
            
        } 
    } 
	
	//minheapify function to minheapify the heap when minimum node is deleted
	public void minHeapify(int i) {
		int left = leftChild(i);
		int right = rightChild(i);
		int smallest=i;
		if(left<=heapSize && heap[i].executed_time<=heap[left].executed_time) {
			if(heap[i].executed_time==heap[left].executed_time) {
				if(heap[i].buildingNum<heap[left].buildingNum) {
					smallest = i;
				}
				else {
					smallest = left;
				}
			}
			else {
				smallest = i;
			}
		}
		else if(left<=heapSize) smallest = left;
		if(right<=heapSize && heap[smallest].executed_time>=heap[right].executed_time) {
			if(heap[smallest].executed_time==heap[right].executed_time) {
				if(heap[smallest].buildingNum>heap[right].buildingNum) {
					smallest = right;
				}
			}
			else {
				smallest = right;
			}
		}
		if(smallest!=i) {
			swap(smallest,i);
			minHeapify(smallest);
		}
	
	}
	
	//remove minimum node of the heap
	public Node removeMin() {
		Node remNode;
		remNode = heap[1];
		heap[1]=heap[heapSize];
		heapSize-=1;
		minHeapify(1);
		return remNode;
	}
	
	//caller to the minheapify function
	public void minHeapify() 
    { 
        for (int i=(heapSize)/2;i>=1;i--) { 
            minHeapify(i); 
        } 
    } 
	
	
	
	//print values of the node of the heap
	public void print() 
    { 
        for (int i = 1; i <= (heapSize) / 2; i++) { 
            System.out.print(" PARENT : " + heap[i].buildingNum 
                             + " LEFT CHILD : " + heap[2 * i].buildingNum 
                             + " RIGHT CHILD :" + heap[2 * i + 1].buildingNum); 
            System.out.println(); 
        } 
    } 
	
	//checks if building is completed
	public boolean buildingIsCompleted(Node node) {
		if(node.executed_time==node.total_time) return true;
		return false;
	}
	
	//function to increase the executed time of the building
	public Node processCurrentBuilding(Node node) {

		node.executed_time+=1;
		return node;
		
	}
	
	//print heap array function
	public void printHeapArray() {

		for(int i=1;i<=heapSize;i++) {
			System.out.println(heap[i].buildingNum+" "+heap[i].executed_time);
			
		}
		
	}
	public static void main(String[] args) {

		Node dummyNode = new Node(-1,-1,-1,-1);
		//initialize min heap
		risingCity minHeap = new risingCity(dummyNode);
		RBT bst = new RBT();
		String outputFile = "output_file.txt";
		File file = new File(args[0]); 
		String[][] filestr = new String[2000][2];
		BufferedReader br;
		String st; 
		List<Integer> allNodes = new ArrayList<>();
		int stringsCount = -1;
		
		//read data from file and parse them
		try {
			br = new BufferedReader(new FileReader(file));
			try {
				while ((st = br.readLine()) != null) {

					stringsCount+=1;
					String day = st.split(":")[0];
					String op = st.split(":")[1].replace(" ", "");
					filestr[stringsCount][0]=day;
					filestr[stringsCount][1]=op;

					
				}
				    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		

		
		Node tempNode;
		String str = filestr[0][1];					
		str = str.replaceAll("[^0-9]+", " ");
		int[] arr = new int[3];
		arr[0]=Integer.parseInt(str.trim().split(" ")[0]);
		arr[1]=Integer.parseInt(str.trim().split(" ")[1]);
		
		
		tempNode = new Node(arr[0],0,arr[1],universalTimer);
		minHeap.insert(tempNode);
		
		bst.insert(tempNode);
		

		Node remNode = minHeap.removeMin();
		Node prevRemNode=null;
		int allOperations=1;		
		int count = -1;
		boolean exit=true;
		boolean printflag=false;
//		System.out.println(filestr[1026][1]);
		//main code to loop over the days until all operations are performed
		while(allOperations<=stringsCount || allNodes.size()!=0 || heapSize!=0) {
			universalTimer+=1;
//			System.out.println("day "+universalTimer);
			count+=1;
			if(allOperations<=stringsCount && universalTimer==Integer.parseInt(filestr[allOperations][0])) {
				//insert operation
				if(filestr[allOperations][1].contains("Insert")) {
					str = filestr[allOperations][1];					
					str = str.replaceAll("[^0-9]+", " ");

					arr[0]=Integer.parseInt(str.trim().split(" ")[0]);
					arr[1]=Integer.parseInt(str.trim().split(" ")[1]);
					
					
					tempNode = new Node(arr[0],0,arr[1],universalTimer);

					minHeap.insert(tempNode);
					bst.insert(tempNode);
					if(allNodes.contains(arr[0])) {
						System.out.println("Duplicate building");
						System.exit(0);
					}
					allNodes.add(arr[0]);
					

					
				}
				else printflag=true;
				allOperations+=1;
				exit=false;

			}
			if(remNode==null) {
				remNode = minHeap.removeMin();
			}
//			System.out.println(allOperations);
//			System.out.println(heapSize);
			//print operation
			if(printflag) {
				str = filestr[allOperations-1][1];
				str = str.replaceAll("[^0-9]+", " ");
				String[] nums = str.trim().split(" ");
				
				if(nums.length==1) {
					
					bst.print(Integer.parseInt(nums[0]));

				}
				else {

					bst.print_range(Integer.parseInt(nums[0]),Integer.parseInt(nums[1]));
									}
				

				exit=false;
				printflag=false;
			}
			if(prevRemNode!=null) {
				try(FileWriter fw = new FileWriter("output_file.txt", true);
		                BufferedWriter bw = new BufferedWriter(fw);
		                PrintWriter out = new PrintWriter(bw))
		            { out.println("("+prevRemNode.buildingNum+","+(universalTimer)+")");
		            } catch (IOException e) { //exception
		                       }

				bst.delete(prevRemNode);
				exit=false;
				prevRemNode = null;
			}
			//exit condition
//			if(allOperations>=stringsCount && remNode.executed_time>=remNode.total_time && heapSize==0)
//				break;
			remNode = minHeap.processCurrentBuilding(remNode);
//			System.out.println(remNode.buildingNum);
//			
//			System.out.println(remNode);
			
			//check if building is completed
			if(minHeap.buildingIsCompleted(remNode)) {
				if(allNodes.contains(remNode.buildingNum)) {
					allNodes.remove(Integer.valueOf(remNode.buildingNum));
				}
				
				if(heapSize==0) {

					prevRemNode = remNode;
					
					
				}
				else {

					prevRemNode = remNode;

					remNode = minHeap.removeMin();

					count=-1;
				}
				exit=false;
			}
			
			else if(!minHeap.buildingIsCompleted(remNode) && count==4) {
				minHeap.insert(remNode);

				remNode = null;
				count=-1;
				exit=false;
			}
			
			
		}
	
			//print last processed node to the file
		if(prevRemNode!=null) {
			try(FileWriter fw = new FileWriter(outputFile, true);
	                BufferedWriter bw = new BufferedWriter(fw);
	                PrintWriter out = new PrintWriter(bw))
	            { out.println("("+prevRemNode.buildingNum+","+(universalTimer+1)+")");
	            } catch (IOException e) {
	                       }

			bst.delete(prevRemNode);
			prevRemNode = null;
		}


}
}