
package com.wingdemo.utils;

public class Utils {

	/**
	 * Monitor java memory consumption
	 * 
	 */
	public static void memoryMonitor() {
		// Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory(); 
        System.out.println("Used memory is megabytes: "
                + (memory/1024L /1024L));
	}
}
