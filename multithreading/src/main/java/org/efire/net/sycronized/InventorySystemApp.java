package org.efire.net.sycronized;

public class InventorySystemApp {

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
        private int items = 0;

        Object lockingObject = new Object();

        private void increment() {
            //... more codes here
            synchronized(lockingObject) {
                items++;
            }
            //... more codes here
        }
        private void decrement() {
            //... more codes here
            synchronized(lockingObject) {
                items--;
            }
            //... more codes here
        }

        public int getItems() {
            synchronized (lockingObject) {
                return items;
            }
        }
    }
}
