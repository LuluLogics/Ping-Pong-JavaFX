package com.example.pongstage1.Experimentation;

public class HeapSizeExperiment {

    public static void main(String[] args) {
        System.out.println("Max heap size: " + Runtime.getRuntime().maxMemory() + " bytes");

        long objectCount = 0;
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                long[] largeArray = new long[100000];
                objectCount++;

                if (objectCount % 100 == 0) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Objects created: " + objectCount + ", Elapsed Time: " + elapsedTime + "ms");
                }
            }
        } catch (OutOfMemoryError e) {
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("Total objects created before OutOfMemoryError: " + objectCount);
            System.out.println("Time until OutOfMemoryError: " + totalTime + "ms");
        }
    }
}
