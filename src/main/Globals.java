package main;

/**
 * Gloabs class includes all the global methods and variables
 * @author somayeghahari
 *
 */
public class Globals {

	/**
	 * end timer
	 */
	public static long E_TIMER=0;
	public static long TUPLES_NO=0;
	public static long TOTAL_IO_NUMBER=0;
	/**
	 * block size 4K
	 */
	public static int BLOCK_SIZE = 4000;
	
	/**
	 * time for one micro second
	 */
	public static int NANO_SECOND = 1000000000;
	
	/**
	 * tuple size
	 */
	public static int TUPLE_SIZE = 100;
	
	/**
	 * size of block for merging
	 */
	public static int BLOCK_SIZE_OF_MERGE = 40;
	
	/**
	 * number of tuples per block
	 */
	public static int TUPLES_NO_PER_BLOCK = BLOCK_SIZE / TUPLE_SIZE;
	/**
	 * calculate number of blocks per chunk
	 * @return number of blocks per chunk based on free memory
	 */
	public static long calBlockNoPerchunk() {
		return (long) (Runtime.getRuntime().freeMemory() * 0.85)/BLOCK_SIZE;//??
	}
	
	/**
	 * calculate number of tuples per chunk
	 * @return number of tuples per chunk based on free memory
	 */
	public static long calTuplesNoPerchunk() {
		return (long) (( Runtime.getRuntime().freeMemory()*0.85)/(TUPLE_SIZE*3));
	}

	
}
