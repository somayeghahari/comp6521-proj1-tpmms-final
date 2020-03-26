package main;

import java.util.ArrayList;
import java.util.Scanner;

public class binaryTree {
	
	binaryNode root;
	
	binaryTree(){
		root = null;
	}
	
	public void add(String data, Scanner fs) {
		int empId = Integer.parseInt(data.substring(0, 8)); //0,8 for original
		int empDate = Integer.parseInt(String.join("",data.substring(8, 18).split("-"))); //8,18 for original and -
		root = addNode(root, empId, empDate, data, fs);
	}
    
    public binaryNode addNode(binaryNode current, int empId, int empDate, String String, Scanner fs) {
    	if (current == null) {
    		return new binaryNode(empId, empDate, String, fs);
    	}
    	if (empId < current.employeeID) {
    		current.left = addNode(current.left, empId, empDate, String, fs);
    	}
    	else if(empId > current.employeeID) {
    		current.right = addNode(current.right, empId, empDate, String, fs);
    	}
    	else {
    		binaryNode temp = null;
    		if (empDate >= current.date) {
    			temp = new binaryNode(empId, empDate, String,fs);
    		}
    		else {
    			temp = new binaryNode(empId, current.date, String,fs);
    		}
    		temp.right = current.right;
    		temp.left = current.left;
    		for (Scanner tempFs: current.fs) {
    			temp.fs.add(tempFs);
    		}
    		current = temp;
    		return current;
    	}
    	return current;
    }
    
    public binaryNode findMinAndDelete(binaryNode current, returnDataSet returnData) {
    	if (current != null) {
    		if (current.left != null) {
    			current.left = findMinAndDelete(current.left, returnData);
    			return current;
    		}
    		
    		else {
    	    	returnData.data = current.data;
    	    	returnData.fs = new ArrayList<Scanner>();
    	    	for (Scanner tempFs: current.fs) {
    	    		returnData.fs.add(tempFs);
    	    	}
    	    	current = ((current.right != null) ? (current.right):null);
    	    	return current;
    		}
    	}
    	else 
    		returnData = null;
    		return null;
    }
    
    public void printTree(binaryNode node) {
    	if (node != null) {
	    	printTree(node.left);
	    	System.out.println(node.data);
	    	printTree(node.right);
    	}
    }
}
