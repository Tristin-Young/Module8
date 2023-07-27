import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {

	//create array and chunk size variables
	public static final int ARRSIZE = 200000000;
	private static final int CHUNKSIZE = 2000000;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
	
		//create array of random numbers
		
		//initialize array
		int[] randomArray = new int[ARRSIZE];
		//initialize random object
		Random rand = new Random();
		//iterate over array and assign each index a random number between 1 and 10
		for(int i = 0; i < ARRSIZE; i++) {
			randomArray[i] = rand.nextInt(10) + 1;
		}
		
		
		//single thread addition
		
		//get current system time in ms (start time)
		long startTime = System.currentTimeMillis();
		//initial single threaded sum to 0
		long singleSum = 0;
		//iterate over array and add each index value to singleSum
		for (int i = 0; i < randomArray.length; i++) {
		    singleSum += randomArray[i];
		}
		//get current time in ms (end time)
		long endTime = System.currentTimeMillis();
		//print results to user
		System.out.println("Single threaded sum is " + singleSum + "\nTime taken: " + (endTime - startTime) + " ms");
		
		
		//multi threaded addition
		
		//creating an ExecutorService service to be a fixed thread pool
		//number of threads is the number of processors free
		ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		//need a list to store the sums of the future thread output
		List<Future<Long>> chunkSums = new ArrayList<>();
		//get current system time in ms (start time)
		startTime = System.currentTimeMillis();
		//break up the larger array into chunks
		for(int i = 0; i < ARRSIZE; i += CHUNKSIZE) {
			//set start of chunk to current index
			int start = i;
			//set end of chunk to either 2 million, or the index plus chunk size
			int end = Math.min(ARRSIZE, i + CHUNKSIZE);
			//add a service to add the indexes in the chunk
			chunkSums.add(service.submit(() -> {
				//create a variable for the current sum
				long sum = 0;
				//iterate through each index of the chunk
				for(int m = start; m < end; m++) {
					//add the current value to sum
					sum += randomArray[m];
				}
				//return the sum of the chunk
				return sum;
			}));
		}
		//initialize multi threaded sum
		long multiSum = 0;
		//get result from each Future and add it to multiSum.
		for(Future<Long> item : chunkSums) {
			multiSum += item.get();
		}
		//get current time in ms (end time)
		endTime = System.currentTimeMillis();
		//print results to user
		System.out.println("Multi threaded sum is " + multiSum + "\nTime taken: " + (endTime - startTime) + " ms");
		
		
		
	}

}
