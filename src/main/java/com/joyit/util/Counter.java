package com.joyit.util;

public class Counter {
    private final int start;
    private int counter;

    public Counter(int start) {
        this.start = start;
        counter = start;
    }

    public int getCounter() {
        return counter;
    }

    public void increase() {
        counter++;
    }

    public void reset() {
        counter = start;
    }
}
