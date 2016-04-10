package com.example.mattiaspernhult.p2.models;

import java.util.LinkedList;

/**
 * Created by mattiaspernhult on 15-10-08.
 */
public class Buffer {

    private LinkedList<String> buffer = new LinkedList<>();

    public synchronized void put(String element) {
        buffer.addLast(element);
        notifyAll();
    }

    public synchronized String get() {
        while (buffer.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return buffer.removeFirst();
    }

}
