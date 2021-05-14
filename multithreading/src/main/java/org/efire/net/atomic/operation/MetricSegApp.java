package org.efire.net.atomic.operation;

import java.util.Random;

public class MetricSegApp {

    //To verify the output of this sample, the expected Ave: is around 5ms
    // because in the BusinessLogic class, we set a Thread.sleep with random number bound to 10
    // so the average will be around 5 to 6 ms
    public static void main(String[] args) {
        var metrics = new Metrics();
        var businessLogic1 = new BusinessLogic(metrics);
        var businessLogic2 = new BusinessLogic(metrics);
        var businessLogic3 = new BusinessLogic(metrics);
        var businessLogic4 = new BusinessLogic(metrics);
        var businessLogic5 = new BusinessLogic(metrics);
        var metricsPrinter = new MetricsPrinter(metrics);

        businessLogic1.start();
        businessLogic2.start();
        businessLogic3.start();
        businessLogic4.start();
        businessLogic5.start();
        metricsPrinter.start();
    }

    //This will capture the average time in the console
    // and will run parallel to the BusinessLogic
    static class MetricsPrinter extends Thread {
        private Metrics metrics;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                final var currentAverage = metrics.getAverage();

                System.out.println("Current average is : "+ currentAverage);

            }
        }
    }

    //Simulate long running business logic
    static class BusinessLogic extends Thread {
        private Metrics metrics;
        private Random random = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                }

                long end = System.currentTimeMillis();

                metrics.addSample(end - start);
            }
        }
    }

    static class Metrics {
        private long count = 0;
        //double primitive type is not thread safe
        //  - add volatile to the average property
        private volatile double average = 0.0;

        //This method with count and average variable will be shared
        // because Metrics with shared to multiple thread.
        //  - Adding synchronized keyword to be atomic
        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        public double getAverage() {
            return average;
        }
    }
}
