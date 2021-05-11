package org.efire.net.exercise1;

import java.util.List;

public class MultiExecutor {

    private List<Runnable> tasks;

    public MultiExecutor(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    public void executeAll() {
        for (Runnable r: tasks) {
            var t = new Thread(r, r.getClass().getSimpleName());
            t.start();
        }
    }
}
