package org.efire.net.exercises.two;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ComplexCalculation {

    public static void main(String[] args) throws InterruptedException {
        ComplexCalculation c = new ComplexCalculation();
        BigInteger result = c.calculateResult(BigInteger.valueOf(10), BigInteger.valueOf(2),
                BigInteger.valueOf(10), BigInteger.valueOf(2));
        System.out.println("Result is: "+result);

    }
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result = BigInteger.ZERO;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        PowerCalculatingThread oneThread = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread twoThread = new PowerCalculatingThread(base2, power2);
        List<PowerCalculatingThread> powerCalculatingThreads = Arrays.asList(oneThread, twoThread);
        for (Thread calcThread: powerCalculatingThreads
             ) {
            calcThread.start();
        }
        for (Thread calcThread: powerCalculatingThreads
        ) {
            calcThread.join();
        }
        for (PowerCalculatingThread calculatedThread : powerCalculatingThreads) {
            result = result.add(calculatedThread.getResult());
        }

        return result;
    }

    private static class PowerCalculatingThread extends Thread
    {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;
    
        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }
    
        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
            for (BigInteger i = BigInteger.ZERO ; i.compareTo(power) !=0 ; i = i.add(BigInteger.ONE)) {
                this.result = result.multiply(base);
            }
            System.out.println(base + "^" + power + " = "+result);
        }
        public BigInteger getResult() { return result; }
    }
}