package org.efire.net.sync;

import java.util.concurrent.atomic.AtomicInteger;

public class InventorySystemAppNoLock {

    public static void main(String[] args) throws InterruptedException {
        final var inventoryCounter = new InventoryCounter();
        final var incrementInventory = new IncrementInventory(inventoryCounter);
        final var decrementInventory = new DecrementInventory(inventoryCounter);

        incrementInventory.start();
        decrementInventory.start();

        incrementInventory.join();
        decrementInventory.join();

        System.out.println("Inventory items count is: "+inventoryCounter.getItems());
    }
    static class IncrementInventory extends Thread {
        private InventoryCounter inventoryCounter;

        public IncrementInventory(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10_000; i++) {
                inventoryCounter.increment();
            }
        }
    }
    static class DecrementInventory extends Thread {
        private InventoryCounter inventoryCounter;

        public DecrementInventory(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10_000; i++) {
                inventoryCounter.decrement();
            }
        }
    }
    static class InventoryCounter {
        AtomicInteger items = new AtomicInteger(0);
        private void increment() {
            items.incrementAndGet();
        }
        private void decrement() {
            items.decrementAndGet();
        }

        public int getItems() {
            return items.get();
        }
    }
}
