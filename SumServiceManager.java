package hoj_harjoitus_osa1;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class SumServiceManager{
    
    /* The port range at which we will begin creating services */
    private int service_start_port = 3500;
    private SumServiceThread[] threadArray;
    private int[] portArray;
    private int number_of_threads = 0;
    
    public static int[] sumArray = new int[10];
    public static int[] sumCounterArray = new int[10];
    
    /*
     *	Creates and starts n threads to listen.
     */
    public void createSumThreads(int n) {
        number_of_threads = n;
        threadArray = new SumServiceThread[n];
        portArray = new int[number_of_threads];

        for(int i = 0; i < n; i++) {
            threadArray[i] = new SumServiceThread(i, service_start_port+i);
            portArray[i] = threadArray[i].getPort();
            threadArray[i].start();
        }
    }
	
    /*
     * Closes all created threads.
     * pre: Remote has already told all services to shut down via a TCP connection
     * post: This class no longer keeps track of the threads and excepts them to be already killed
     */
    public void closeSumService() {
        for(int i = 0; i < threadArray.length; i++){
            threadArray[i] = null;
        }
    }

    /*
     *	Returns an int[] size n. Contains ports of created threads.
     */
    public int[] getPortArray() {
        return portArray;
    }

    /*
     *	Returns an int[] size n. Contains the number of sums done for each thread.
     */
    public int[] getSumCounterArray() {
        return sumCounterArray;
    }

    /*
     *	Returns an int[] size n. Contains the sum total for each thread.
     */
    public int[] getSumArray() {
        return sumArray;
    }
    
    /**
     * Returns the number of sum services
     */
    public int getNumberOfServices() {
        return number_of_threads;
    }
    
    
    /**
     * Returns the sum of all performed sum operations
     */
    public int getSum() {
        int sum = 0;
        for (int i = 0; i < number_of_threads; i++) {
            sum += sumArray[i];
        }
        return sum;
    }
    
    /**
     * Returns the port number for the service which has the largest total sum
     */
    public int getLargestSumService() {
        int largest = 0;
        for (int i = 0; i < number_of_threads; i++) {
            if (sumArray[i] > largest) {
                largest = i;
            }
        }
        return portArray[largest];
    }
    
    /**
     * Returns the total number of numbers that have been summed by all services combined
     */
    public int getNumbersSummed() {
        int total_summed = 0;
        for (int i = 0; i < number_of_threads; i++) {
            total_summed += threadArray[i].getNumbersSummed();
        }
        return total_summed;
    }
}
