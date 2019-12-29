import java.io.*;
import java.util.*;


class RBT {
    private final int red = 0;
    private final int black = 1;

    private final RBNode extNode = new RBNode(new Node(-1,-1,-1,-1));
    public boolean checkflag = true;
    public class RBNode {  //Class that represents a node in the red black tree
        public Node building = new Node(0,0,0,0);
        public int color = black;
        public RBNode left = extNode;
        public RBNode right = extNode;
        public RBNode parent = extNode;
        //constructor to initialize the node with the node of the heap
        RBNode(Node building) {
            this.building = building;
        }       
    }

    
    
    
    private RBNode root = extNode;
    

    //find a node in the red black tree
    private RBNode findRBNode(RBNode node, RBNode rootNode) {
        if (root == extNode) {
            return null;
        }
        if (node.building.buildingNum < rootNode.building.buildingNum && rootNode.left != extNode)  {           
            return findRBNode(node, rootNode.left);
        } else if (node.building.buildingNum > rootNode.building.buildingNum && rootNode.right != extNode) {
        	return findRBNode(node, rootNode.right);
        } else if (node.building.buildingNum == rootNode.building.buildingNum) {
            return rootNode;
        }
        return null;
    }

  //Insert caller function
    public void insert(Node temp){
    	
        RBNode redblacknode = new RBNode(temp);
        insert(redblacknode);

    }
    //insert a node in the red black tree
    private void insert(RBNode node) {
        RBNode temp = root;
        if (root == extNode && checkflag) {
            root = node;
            node.color = black;
            node.parent = extNode;
        } else {
            node.color = red;
            while (checkflag) {
                if (node.building.buildingNum < temp.building.buildingNum) {
                    if (temp.left == extNode) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.building.buildingNum >= temp.building.buildingNum && checkflag) {
                    if (temp.right == extNode) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            adjustTree(node);
        }
    }

    //adjust the red black tree after an insertion to satisfy the red black properties
    private void adjustTree(RBNode node) {
    	
        while (node.parent.color == red && checkflag) {
            RBNode newNode = extNode;
            if (node.parent == node.parent.parent.left)
            {
                newNode = node.parent.parent.right;

                if (newNode != extNode && newNode.color == red) {
                    node.parent.color = black;
                    newNode.color = black;
                    node.parent.parent.color = red;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    node = node.parent;
                    leftRotation(node,checkflag);
                }
                node.parent.color = black;
                node.parent.parent.color = red;
                 
                rightRotation(node.parent.parent,checkflag);
                
            } 
            else 
            {
                newNode = node.parent.parent.left;
                if (newNode != extNode && newNode.color == red) {
                    node.parent.color = black;
                    newNode.color = black;
                    node.parent.parent.color = red;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    node = node.parent;
                    rightRotation(node,checkflag);
                }
                node.parent.color = black;
                node.parent.parent.color = red;
                leftRotation(node.parent.parent,checkflag);
            }
        }
        root.color = black;
    }

    //function that performs left rotation
    public void leftRotation(RBNode node,boolean checkflag) {
        if (node.parent != extNode) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != extNode) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {
            RBNode right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = extNode;
            root = right;
        }
    }

  //function that performs right rotation
    public void rightRotation(RBNode node,boolean checkflag) {
        if (node.parent != extNode) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != extNode) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
            RBNode left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = extNode;
            root = left;
        }
    }
    
    //swap after rotations
    public void swap(RBNode first, RBNode second){
        if(first.parent == extNode){
            root = second;
        }else if(first == first.parent.left){
            first.parent.left = second;
        }else
            first.parent.right = second;
        second.parent = first.parent;
    }

  //caller to delete function
    public void delete(Node temp){
        RBNode node = new RBNode(temp);
        delete(node);
    }
    
    //delete a node of the red black tree
    public boolean delete(RBNode node){
        if((node = findRBNode(node, root))==null)
        	return false;
        RBNode node1;
        RBNode temp = node;
        int temp_color = temp.color;

        if(node.left == extNode){
            node1 = node.right;
            swap(node, node.right);
        }else if(node.right == extNode){
            node1 = node.left;
            swap(node, node.left);
        }else{
            temp = findMin(node.right);
            temp_color = temp.color;
            node1 = temp.right;
            if(temp.parent == node)
                node1.parent = temp;
            else{
                swap(temp, temp.right);
                temp.right = node.right;
                temp.right.parent = temp;
            }
            swap(node, temp);
            temp.left = node.left;
            temp.left.parent = temp;
            temp.color = node.color;
        }
        if(temp_color==black)
            adjustDelete(node1);
        return true;
    }
    
    //adjust the red black after deletion of a node
    public void adjustDelete(RBNode node){
        while(node!=root && node.color == black)
        {
            if(node == node.parent.left){
                RBNode sib = node.parent.right;
                if(sib.color == red){
                    sib.color = black;
                    node.parent.color = red;
                    leftRotation(node.parent,checkflag);
                    sib = node.parent.right;
                }
                if(sib.left.color == black && sib.right.color == black){
                    sib.color = red;
                    node = node.parent;
                    continue;
                }
                else if(sib.right.color == black){
                    sib.left.color = black;
                    sib.color = red;
                    rightRotation(sib,checkflag);
                    sib = node.parent.right;
                }
                if(sib.right.color == red){
                    sib.color = node.parent.color;
                    node.parent.color = black;
                    sib.right.color = black;
                    leftRotation(node.parent,checkflag);
                    node = root;
                }
            }
            else
            {
                RBNode sib = node.parent.left;
                if(sib.color == red){
                    sib.color = black;
                    node.parent.color = red;
                    rightRotation(node.parent,checkflag);
                    sib = node.parent.left;
                }
                if(sib.right.color == black && sib.left.color == black){
                    sib.color = red;
                    node = node.parent;
                    continue;
                }
                else if(sib.left.color == black){
                    sib.right.color = black;
                    sib.color = red;
                    leftRotation(sib,checkflag);
                    sib = node.parent.left;
                }
                if(sib.left.color == red){
                    sib.color = node.parent.color;
                    node.parent.color = black;
                    sib.left.color = black;
                    rightRotation(node.parent,checkflag);
                    node = root;
                }
            }
        }
        node.color = black;
    }


    RBNode findMin(RBNode node){
        while(node.left!=extNode){
            node = node.left; }
        return node;
    }

    //Check for the nodes between the two given nodes
    public List<Node> nodesPrint(RBNode node, RBNode node1,RBNode node2,List<Node> nodesList) {
        if (node == extNode) {
            return nodesList;
        }
        nodesPrint(node.left, node1, node2, nodesList);
        if(node.building.buildingNum <= node2.building.buildingNum && node.building.buildingNum >= node1.building.buildingNum) {
            nodesList.add(node.building);
        }
        nodesPrint(node.right, node1, node2, nodesList);
        return nodesList;
    }

    
    public void inorder(RBNode node) {
    	if(node==null) {
    		return;
    	}
    	inorder(node.left);
    	System.out.print(node.building.buildingNum+" ");
    	inorder(node.right);
    	
    }
    

    //print a single node in the node, if the node is not present in the tree then (0,0,0) will be printed to the file
    public void print(int buildingNum){
//    	System.out.println("inside single print");
        Node temp = new Node(buildingNum,0,0,-1);
        String filestr = "output_file.txt";
        RBNode node = new RBNode(temp);
        RBNode requiredNode = findRBNode(node, root);
        if(requiredNode==null) {
            try(FileWriter fw = new FileWriter(filestr, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            { out.println("("+0+","+0+","+0+")");
            } catch (IOException e) { 
            }
        } else {
            try (FileWriter fw = new FileWriter(filestr, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println("(" + requiredNode.building.buildingNum + "," + requiredNode.building.executed_time + "," + requiredNode.building.total_time + ")");
            } catch (IOException e) {
                
            }
        }
    }

    //prints the nodes in the given range to the file, if the building is not present in the tree (0,0,0) will be printed in the file
    public void print_range(int num1,int num2){
//    	inorder(root);
        RBNode node1 = new RBNode(new Node(num1,0,0,-1));
        List<String> printList = new  ArrayList<>();
        RBNode node2 = new RBNode(new Node(num2,0,0,-1));        
        String filestr = "output_file.txt";
        List<Node> nodesList = new  ArrayList<>();
        nodesList = nodesPrint(root,node1,node2,nodesList);
        
        //when no nodes are present in the tree in the given range
        if(nodesList.size()==0) 
        {
        	try(FileWriter fw = new FileWriter(filestr, true);
                  BufferedWriter bw = new BufferedWriter(fw);
                  PrintWriter out = new PrintWriter(bw))
              { out.println("("+0+","+0+","+0+")");
              } catch (IOException e) {
                         }
        }
        else 
        {
        	for(Node node: nodesList){
                if(nodesList.indexOf(node)!=nodesList.size()-1){            
                printList.add("("+node.buildingNum+","+node.executed_time+","+node.total_time+")");}
                if(nodesList.indexOf(node)==nodesList.size()-1){                	
                    printList.add("("+node.buildingNum+","+node.executed_time+","+node.total_time+")"); }
            }
            try(FileWriter fw = new FileWriter(filestr, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            { out.println(Arrays.toString(printList.toArray()).substring(Arrays.toString(printList.toArray()).indexOf("[") + 1, Arrays.toString(printList.toArray()).indexOf("]")).replaceAll("\\s+",""));
            } catch (IOException e) {
                        }
        }
            
        
    }
}