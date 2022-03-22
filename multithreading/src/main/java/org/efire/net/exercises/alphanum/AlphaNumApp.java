package org.efire.net.exercises.alphanum;

import java.util.Arrays;

/**
 *   Create 2 threads, one printing the alphabets, two printing nums
 *   The output should print like ( a:1 b:2 c:3 d:4)
 */
public class AlphaNumApp {

    public static void main(String[] args) throws InterruptedException {

        AlphaNumPrinter alphaNumPrinter = new AlphaNumPrinter();
        Thread alphaThread = new Thread(new AlphaRun(alphaNumPrinter));
        Thread numThread = new Thread(new NumRun(alphaNumPrinter));


        alphaThread.start();
        numThread.start();

    }

    static class AlphaRun implements Runnable {

        AlphaNumPrinter printer;

        public AlphaRun(AlphaNumPrinter printer) {
            this.printer = printer;
        }

        @Override
        public void run() {
            var strings = Arrays.asList("a", "b", "c", "d", "e");
            for (String s: strings) {
                try {
                    printer.print(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class NumRun implements Runnable {

        AlphaNumPrinter printer;

        public NumRun(AlphaNumPrinter printer) {
            this.printer = printer;
        }

        @Override
        public void run() {
            int num[] = {1, 2, 3, 4, 5};
            for (int i = 0; i < num.length; i++) {
                try {
                    printer.print(num[i]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // This is a shared class
    static class AlphaNumPrinter {
        Object syncher = new Object();
        private int state = 0;

        public void print(String letter) throws InterruptedException {
            synchronized (syncher) {
                while (true) {
                    if (state == 0) {
                        System.out.print(letter);
                        state = 1;
                        syncher.notify();
                        return;
                    } else {
                        syncher.wait();
                    }
                }
            }
        }
        public void print(int num) throws InterruptedException {
            synchronized (syncher) {
                while (true) {
                    if (state == 1) {
                        System.out.print(":"+num + " ");
                        state = 0;
                        syncher.notify();
                        return;
                    } else {
                        syncher.wait();
                    }
                }
            }
        }
    }
}
