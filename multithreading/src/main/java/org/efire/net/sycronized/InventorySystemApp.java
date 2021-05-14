package org.efire.net.sycronized;

public class InventorySystemApp {

    public static void main(String[] args) throws InterruptedException {
        final var inventoryCounter = new InventoryCounter();
        final var incrementInventory = new IncrementInventory(inventoryCounter);
        final var decrementInventory = new DecrementInventory(inventoryCounter);

        incrementInventory.start();
        incrementInventory.join();

        decrementInventory.start();
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

        private int increment() {
            return items++;
        }
        private int decrement() {
            return items--;
        }

        public int getItems() {
            return items;
        }
    }
}
