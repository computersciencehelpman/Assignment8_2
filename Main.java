package com.coderscampus.assignment8;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("Running");
		
		Assignment8 assignment8 = new Assignment8();
		Map<Integer, Integer> frequencyMap = new ConcurrentHashMap<>();
		
		//Executor service for running CompletableFuture tasks
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		//List to hold all CompletableFuture tasks
		 List<CompletableFuture<Void>> futures = new ArrayList<>();
		 
		 //Fetch and process chunks asynchronously
		 for (int j = 0; j < 1000; j++) {
			 CompletableFuture<Void> future = CompletableFuture.supplyAsync(assignment8::getNumbers, executor)
					                                    .thenAccept(numbers -> {
					                                    	for (int number : numbers) {
					                                    		frequencyMap.merge(number, 1, Integer::sum);
					                                    	}
					                                    });
			 futures.add(future);
		 }
		 
		 //Wait for all tasks to complete
		 CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
		 try {
	            allOf.get();
	        } catch (InterruptedException | ExecutionException e) {
	            e.printStackTrace();
	        }
		 
		 //Shutdown the executor service
		 executor.shutdown();
		
	    
		 
		 System.out.println("Done");
		 
		 //Print the frequencies
		 printFrequencies(frequencyMap);
	}
		 public static void printFrequencies(Map<Integer, Integer> frequencyMap) {
		      frequencyMap.entrySet()
		           .stream()
		           .sorted(Map.Entry.comparingByKey())
		           .forEach(entry -> System.out.println("Element "+entry.getKey() + " occurs: "+entry.getValue() + " times"));
		 
	}

}
