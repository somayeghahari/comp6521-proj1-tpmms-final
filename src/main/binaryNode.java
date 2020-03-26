package main;

import java.util.Scanner;
import java.util.ArrayList;

public class binaryNode {
	int employeeID;
	int date;
	String data;
	ArrayList<Scanner> fs;
    binaryNode left;
    binaryNode right;
 
    binaryNode(int empID, int empDate, String String, Scanner fs) {
        this.employeeID = empID;
        this.date = empDate;
        this.data = String;
        this.fs = new ArrayList<Scanner>();
        this.fs.add(fs);
        right = null;
        left = null;
    }

}
