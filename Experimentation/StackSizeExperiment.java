package com.example.pongstage1.Experimentation;

public class StackSizeExperiment {

    private static long depth = 0;

    public static void main(String[] args) {
        try {
            recursiveCall();
        } catch (StackOverflowError e) {
            System.out.println("Recursion depth at StackOverflowError: " + depth);
        }
    }

    public static void recursiveCall() {
        depth++;
        if (depth % 100 == 0) {
            long currentTime = System.currentTimeMillis();
            System.out.println("Current recursion depth: " + depth + ", Current Time: " + currentTime + "ms");
        }
        recursiveCall();
    }
}
