package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class readAndMerge {
	
	public static binaryTree dataTree;
	public static ArrayList<String> writeBlock;
	
	readAndMerge(){
		dataTree = new binaryTree();
		writeBlock = new ArrayList<String>();
	}
	
	public static void mergeFiles(ArrayList<Scanner> scannerList){
		int n = scannerList.size();
		int linesRead = 0;
		int linesWrote = 0;
		int blockSize = Globals.BLOCK_SIZE_OF_MERGE;
		long ttt=0;
		returnDataSet dataSet = new returnDataSet();
		for (Scanner fs: scannerList) {
			if(loadDataFromFiles(fs, dataTree, blockSize)) {
				linesRead += blockSize;
				continue;
			}	
			else
				scannerList.remove(fs);
		}	
		while (true) {
			ttt= System.nanoTime();

			while ((writeBlock.size() != blockSize) && dataTree.root != null) {
				dataTree.root = dataTree.findMinAndDelete(dataTree.root, dataSet);
				writeBlock.add(dataSet.data);
				for (Scanner fs: dataSet.fs) {
					if(!loadDataFromFiles(fs, dataTree, 1))
						scannerList.remove(fs);
				}
			}
			Globals.E_TIMER += System.nanoTime()-ttt;
			linesRead += writeBlock.size();
			linesWrote += writeBlock.size();
			writeDataBlockToFile();
			if (dataTree.root == null)
				break;
		}
		Globals.TUPLES_NO+=linesWrote;
		Globals.TOTAL_IO_NUMBER = linesRead /Globals.TUPLES_NO_PER_BLOCK;
		if((linesRead%Globals.TUPLES_NO_PER_BLOCK)!=0)
			Globals.TOTAL_IO_NUMBER++;
	}
	
	public static boolean loadDataFromFiles(Scanner fs, binaryTree bt, int numOfLines) {
		while(numOfLines-- > 0) {	
			if (fs.hasNext()) {
				bt.add(fs.nextLine(), fs);
			}
			else 
				return false;
		}
		return true;
	}
	
	public static void writeDataBlockToFile(){
		
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("files/final/mergedOutputFile.txt", true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			while (writeBlock.size() != 0) {
				printWriter.println(writeBlock.get(0));
				writeBlock.remove(0);
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
