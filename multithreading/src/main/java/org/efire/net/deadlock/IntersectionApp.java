package org.efire.net.deadlock;

import java.util.Random;

public class IntersectionApp {

    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        Thread threadA = new Thread(new TrainA(intersection));
        Thread threadB = new Thread(new TrainB(intersection));

        threadA.start();
        threadB.start();

    }

    static class TrainA implements Runnable {
        Intersection intersection;
        Random random = new Random();

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while(true) {
                long sleepTimer = random.nextInt(5);
                try {
                    Thread.sleep(sleepTimer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadA();
            }
        }
    }

    static class TrainB implements Runnable {
        Intersection intersection;
        Random random = new Random();

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while(true) {
                long sleepTimer = random.nextInt(5);
                try {
                    Thread.sleep(sleepTimer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadB();
            }
        }
    }

    static class Intersection {
        Object roadA = new Object();
        Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by :" + Thread.currentThread().getName());
                synchronized (roadB) {
                    System.out.println("Train is passing to road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void takeRoadB() {
            synchronized (roadA) {
                System.out.println("Road A is locked by :" + Thread.currentThread().getName());
                synchronized (roadB) {
                    System.out.println("Train is passing to road B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
