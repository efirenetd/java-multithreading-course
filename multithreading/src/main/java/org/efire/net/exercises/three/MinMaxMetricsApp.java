package org.efire.net.exercises.three;

import java.util.Random;

public class MinMaxMetricsApp {
    public static void main(String[] args) {
        var minMaxMetrics = new MinMaxMetrics();
        var aThread = new AThread(minMaxMetrics);
        aThread.start();
    }

    static class AThread extends  Thread {
        private MinMaxMetrics minMaxMetrics;
        private Random random = new Random();

        public AThread(MinMaxMetrics minMaxMetrics) {
            this.minMaxMetrics = minMaxMetrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                minMaxMetrics.addSample(random.nextInt(1000));

                System.out.println("Current Min:"+minMaxMetrics.getMin() +" Max: "+minMaxMetrics.getMax());
            }
        }
    }
    static class MinMaxMetrics {
        private volatile long min;
        private volatile long max;

        /**
         * Initializes all member variables
         */
        public MinMaxMetrics() {
            this.min  = Long.MAX_VALUE;
            this.max = Long.MIN_VALUE;
        }

        /**
         * Adds a new sample to our metrics.
         */
        public void addSample(long newSample) {
            synchronized (this) {
                System.out.println("Sample: " + newSample);
                this.min = Math.min(newSample, this.min);
                this.max = Math.max(newSample, this.max);
            }
        }

        /**
         * Returns the smallest sample we've seen so far.
         */
        public long getMin() {
            return min;
        }

        /**
         * Returns the biggest sample we've seen so far.
         */
        public long getMax() {
            return max;
        }
    }

}
