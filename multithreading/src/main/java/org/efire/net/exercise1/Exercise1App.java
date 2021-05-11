package org.efire.net.exercise1;

import java.util.ArrayList;

public class Exercise1App {

    public static void main(String[] args) {
        var tasks = new ArrayList<Runnable>();
        tasks.add(new Task1());
        tasks.add(new Task2());
        var multiExecutor = new MultiExecutor(tasks);
        multiExecutor.executeAll();
    }
}
