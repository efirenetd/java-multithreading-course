package org.efire.net.threadjoin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class FactorialThreadApp {

    public static void main(String[] args) throws InterruptedException {
        var inputNumbers = Arrays.asList(10000000L, 345L, 355L, 232L, 45L, 23L, 56L);
        var factorialThreads = new ArrayList<FactorialThread>();

        for (Long input: inputNumbers) {
            factorialThreads.add(new FactorialThread(input));
        }
        for (FactorialThread thread : factorialThreads) {
            thread.setDaemon(true);
            thread.start();
        }
        for (FactorialThread factorialThread : factorialThreads) {
            factorialThread.join(2000);
        }
        for (int i = 0 ; i < inputNumbers.size() ; i++) {
            FactorialThread f = factorialThreads.get(i);
            if (f.isFinished()) {
                System.out.println("The factorial of "+ f.inputNumber + " is: "+f.getResult()+"\n");
            } else {
                System.out.println("Calculation still in progress for "+f.inputNumber+"\n");
            }
        }
    }

    static class FactorialThread extends Thread {

        protected long inputNumber;
        protected BigInteger result = BigInteger.ZERO;
        protected boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial(long n) {
            var tempResult = BigInteger.ONE;
            for (long i = n; i > 0 ; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
