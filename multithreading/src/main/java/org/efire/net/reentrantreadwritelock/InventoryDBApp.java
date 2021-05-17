package org.efire.net.reentrantreadwritelock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InventoryDBApp {

    private static final int HIGHEST_PRICE = 1000;

    public static void main(String[] args) throws InterruptedException {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();

        Random random = new Random();
        for (int i = 0; i < 100_000; i++) {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }

        //One instance of Writer Thread
        Thread writerThread = new Thread(() -> {
           while(true) {
               inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
               inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
               try {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
               }
           }
        });
       writerThread.setDaemon(true);
       writerThread.start();

       //Allocate 7 Threads for reading operations and create list to store
        int numberOfReaderThread = 7;
        List<Thread> readerThreads = new ArrayList<>();
        for (int readerIndex = 0; readerIndex < numberOfReaderThread; readerIndex++) {
            Thread reader = new Thread(() -> {
                for (int i = 0; i < 100_000; i++) {
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });
            reader.setDaemon(true);
            readerThreads.add(reader);
        }

        long startReadingTime = System.currentTimeMillis();
        for (Thread readerThread: readerThreads) {
            readerThread.start();
        }

        for (Thread readerThread: readerThreads) {
            readerThread.join();
        }

        long endReadingTime = System.currentTimeMillis();

        System.out.println(String.format("Reading took %d ms", endReadingTime - startReadingTime));
    }
    public static class InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCount = new TreeMap<>();
        private ReentrantLock lockObject = new ReentrantLock();
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Lock readerLock = reentrantReadWriteLock.readLock();
        private Lock writerLock = reentrantReadWriteLock.writeLock();

        public int getNumberOfItemInPriceRange(int lowerbound, int upperbound) {
            //lockObject.lock();
            readerLock.lock();
            try {
                var fromKey = priceToCount.ceilingKey(lowerbound);
                var toKey = priceToCount.floorKey(upperbound);

                if (fromKey == 0 && toKey == 0) {
                    return 0;
                }

                var rangeOfPrice = priceToCount.subMap(fromKey, true, toKey, true);

                int sum = 0;
                for (int numberOfPrice : rangeOfPrice.values()
                ) {
                    sum += numberOfPrice;
                }

                return sum;
            }finally {
                //lockObject.unlock();
                readerLock.unlock();
            }
        }

        public void addItem(int price) {
            //lockObject.lock();
            writerLock.lock();
            try {
                var numberOfItemsForPrice = priceToCount.get(price);
                if (numberOfItemsForPrice == null) {
                    priceToCount.put(price, 1);
                } else {
                    priceToCount.put(price, numberOfItemsForPrice + 1);
                }
            } finally {
                //lockObject.unlock();
                writerLock.unlock();
            }
        }

        public void removeItem(int price) {
            //lockObject.lock();
            writerLock.lock();
            try {
                var numberOfItemsForPrice = priceToCount.get(price);
                if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                    priceToCount.remove(price);
                } else {
                    priceToCount.put(price, numberOfItemsForPrice - 1);
                }
            }finally {
                //lockObject.unlock();
                writerLock.unlock();
            }
        }
    }


}
