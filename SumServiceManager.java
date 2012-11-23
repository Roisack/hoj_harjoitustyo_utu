import java.net.*;
import java.io.*;

public class SumServiceManager{
	
	private SumServiceThread[] threadArray;
	private int[] portArray;
	
	public static int[] sumArray = new int[10];
	public static int[] sumCounterArray = new int[10];
	
	/*
	 *	Creates and starts n threads to listen.
	 */
	public void createSumThreads(int n){
		
		threadArray = new SumServiceThread[n];
		portArray = new int[n];
		
		
		for(int i = 0; i < n; i++){
			
			threadArray[i] = new SumServiceThread(i);
			portArray[i] = threadArray[i].getPort();
			threadArray[i].start();

		}
		
	}
	
	/*
	 * Closes all created threads.
	 */
	public void closeSumService(){
		
		for(int i = 0; i < threadArray.length; i++){
			threadArray[i] = null;
		}
	}
	
	/*
	 *	Returns an int[] size n. Contains ports of created threads.
	 */
	public int[] getPortArray(){
		return portArray;
	}
	
	/*
	 *	Returns an int[] size n. Contains the number of sums done for each thread.
	 */
	public int[] getSumCounterArray(){
		return sumCounterArray;
	}
	
	/*
	 *	Returns an int[] size n. Contains the sum total for each thread.
	 */
	public int[] getSumArray(){
		return sumArray;
	}
	
}