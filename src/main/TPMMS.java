package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


/**
 * main method of two-pass multiway merge sort
 * @author somayeghahari
 *
 */
public class TPMMS {
	
	public static void phase1(String inputFilePath, String outputFilePath, String inpuFile1Name) {
		System.out.println("\nStart of phase1 for file..."+ inpuFile1Name);

        long diskWriteTime = 0;
        int tempFileNo = 0;//Sorted chunks 
        long removedTuplesNo = 0;
        int i = 0;
		long diskReadTime = 0;
		long tuplesNoPerChunk = Globals.calTuplesNoPerchunk();
        File folder = new File(inputFilePath);
		File[] files = folder.listFiles();
		File[] fr = new File[files.length];
		ArrayList<Scanner> fs = new ArrayList<Scanner>();
		ArrayList<String> chunk = new ArrayList<String>();
		
		for (File file: files) {
			fr[i] = new File(file.getAbsolutePath());
			try {
				fs.add(new Scanner(fr[i]));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		//System.out.println(tuplesNoPerChunk);
		for (Scanner oneFs: fs) {
			while (oneFs.hasNext()) {
				System.gc();

				readDataFromInputFile(oneFs, chunk, tuplesNoPerChunk);
	        	//System.out.println("  "+(tempFileNo+1)+":");
	            long startTime = System.nanoTime();
				 Globals.TOTAL_IO_NUMBER += chunk.size()/Globals.TUPLES_NO_PER_BLOCK;
		         if((chunk.size()%Globals.TUPLES_NO_PER_BLOCK)!=0)
		        	 Globals.TOTAL_IO_NUMBER ++;
	            
	            if (!chunk.isEmpty()) 
	            {
	            	Collections.sort(chunk);
	                //Globals.TUPLES_NO += chunk.size();
	                diskReadTime += System.nanoTime() - startTime;
	                Globals.TOTAL_IO_NUMBER += chunk.size()/Globals.TUPLES_NO_PER_BLOCK;
			         if((chunk.size()%Globals.TUPLES_NO_PER_BLOCK)!=0)
			        	 Globals.TOTAL_IO_NUMBER ++;
	                if(!chunk.isEmpty()) 
	                {
		                startTime = System.nanoTime();
		                tempFileNo++;
		                FileWriter fileWriter;
						try 
						{
							fileWriter = new FileWriter(outputFilePath + "temp" + tempFileNo +"-" + inpuFile1Name);
							writeDataBlockToFile(chunk, fileWriter);
							//Globals.TUPLES_NO += chunk.size();
			                //fileWriter.close();
			                diskWriteTime += System.nanoTime() - startTime;
						} catch (IOException e) 
						{
							e.printStackTrace();
						}
	                }
	            }
				else
					fs.remove(oneFs);      
	        }
		}	

		System.out.println("\n  Time to read and sort input file: "+ inpuFile1Name+" is: "+(diskReadTime/Globals.NANO_SECOND) + " ms");
		//System.out.println("  Number of temporary files: "+tempFileNo);
		System.out.println("  Time to write sorted tuples to temporary files is: "+(diskWriteTime/Globals.NANO_SECOND) + " ms");

	}
	
	public static boolean readDataFromInputFile(Scanner fs, ArrayList<String> at, long numOfLines) {
		while(numOfLines-- > 0) {	
			if (fs.hasNext()) {
				String temp = new String();
				temp = fs.nextLine();
				at.add(temp);
			}
			else 
				return false;
		}
		return true;
	}
	
	public static void writeDataBlockToFile(ArrayList<String> writeBlock, FileWriter fileWriter){
		PrintWriter printWriter = new PrintWriter(fileWriter);
		while (writeBlock.size() != 0) {
			printWriter.println(writeBlock.get(0));
			writeBlock.remove(0);
		}
		printWriter.close(); 
	}
	

	public static void phase2(String outputFilePath) {
		System.out.println("\nStart of phase2...");
		readAndMerge newMerge = new readAndMerge();
		int i = 0;
		long diskReadTime = 0;
		File folder = new File(outputFilePath);
		File[] files = folder.listFiles();
		File[] fr = new File[files.length];
		ArrayList<Scanner> fs = new ArrayList<Scanner>();
		
		for (File file: files) {
			fr[i] = new File(file.getAbsolutePath());
			try {
				fs.add(new Scanner(fr[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}			
			i++;
		}
		long startTime = System.nanoTime();
		readAndMerge.mergeFiles(fs);
		diskReadTime += System.nanoTime() - startTime;
//		System.out.println("  Total time to merge all the temporary files is: "+(diskReadTime/Globals.NANO_SECOND) + " ms");
		
	}
	
	public static void main(String[] args) {
		System.gc();
        
		String inputFilePath = "files/input/";
		String outputFilePath = "files/output/";
		String inputFile1Name = "sample1.txt";
		String inputFile2Name = "sample2.txt";
		//String inpuFile2Path = "files/input/sample2.txt";
		System.out.println("***** Two-Pass Multiway Merge Sort *****");
		System.out.println("Input files are: "+inputFile1Name +" and "+inputFile2Name);
		System.out.println("Available memory at the beginning is: "+ (Runtime.getRuntime().freeMemory() / 1048576) +" MB");
		
		phase1(inputFilePath, outputFilePath, inputFile1Name);
		phase1(inputFilePath, outputFilePath, inputFile2Name);
		
		phase2(outputFilePath);
		
		System.out.println("The number of output tuples: " + Globals.TUPLES_NO);
		System.out.println("The number of blocks holding the output tuples: "+
				(Globals.TUPLES_NO/Globals.TUPLES_NO_PER_BLOCK + ((Globals.TUPLES_NO%Globals.TUPLES_NO_PER_BLOCK!=0)?1:0 )) )  ;
		System.out.println("The number of disk I/O's to perform the task:   "+ Globals.TOTAL_IO_NUMBER );

		System.out.println("Total time: " + Globals.E_TIMER/Globals.NANO_SECOND+" s");



	}

}
