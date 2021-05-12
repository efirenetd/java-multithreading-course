package org.efire.net.exercises.one;

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
