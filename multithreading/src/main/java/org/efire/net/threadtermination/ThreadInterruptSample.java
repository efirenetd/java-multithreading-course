package org.efire.net.threadtermination;

public class ThreadInterruptSample {

    public static void main(String[] args) {
        var thread = new Thread(new BlockingTask());
        thread.start();
    }

    static class BlockingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                System.out.println("Interrupt blocking task");
            }
        }
    }
}
