package org.efire.net.threadtermination;

import java.math.BigInteger;

public class ThreadInterruptSample {

    public static void main(String[] args) {
        var thread = new Thread(new LongComputationTask(
                new BigInteger("20000000"), new BigInteger("1000000000")));
        thread.start();
        thread.interrupt();
    }

    static class LongComputationTask implements Runnable {

        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base+"^"+power+" = "+pow(base, power));
        }

        private static BigInteger pow(BigInteger base, BigInteger power) {
            var result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO ; i.compareTo(power) !=0 ; i = i.add(BigInteger.ONE)) {
                if (Thread.interrupted()) {
                    System.out.println("Prematurely computed");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
    static class BlockingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupt blocking task");
            }
        }
    }
}
