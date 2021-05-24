package org.efire.net.exercises.waitnotify;

public class SimpleCountDownLatch {
    private int count;

    public SimpleCountDownLatch(int count) {
        this.count = count;
        if (count < 0) {
            throw new IllegalArgumentException("count cannot be negative");
        }
    }

    /**
     * Causes the current thread to wait until the latch has counted down to zero.
     * If the current count is already zero then this method returns immediately.
    */
    public void await() throws InterruptedException {
        /**
         * Fill in your code
         */
        synchronized (this) {
            while (count > 0) {
                wait();
            }
        }
        return;
    }

    /**
     * Decrements the count of the latch, releasing all waiting threads if the count reaches zero.
     * If the current count equals zero then nothing happens.
     */
    public void countDown() {
        synchronized (this) {
            if (count == 0) {
                notifyAll();
            }
            if (count > 0) {
                count--;
            }
        }

    }

    /**
     * Returns the current count.
    */
    public int getCount() {
        /**
         * Fill in your code
         */
        synchronized (this) {
            return count;
        }
    }
}
