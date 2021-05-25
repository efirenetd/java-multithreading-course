package org.efire.net.atomicreference;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class StackApp {

    public static void main(String[] args) throws InterruptedException {
        //var stack = new StandardStack<Integer>();
        var stack = new LockFreeStack<Integer>();
        var random = new Random();

        for (int i = 0; i < 100_000; i++) {
            stack.push(random.nextInt());
        }

        int pushingThread = 2;
        int poppingThread = 2;

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < pushingThread; i++) {
            var thread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }
        for (int i = 0; i < poppingThread; i++) {
            var thread = new Thread(() -> {
                for(;;) {
                    stack.pop();
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for (Thread r : threads) {
            r.start();
        }

        Thread.sleep(10_000);

        System.out.println(String.format("%,d, no. of operation were performed in 10 secs", stack.getCounter()));
    }

    static class LockFreeStack<T> {
        AtomicReference<StackNode> head = new AtomicReference<>();
        AtomicInteger counter = new AtomicInteger(0);
        public void push(T value) {
            StackNode<T> newHeadNode = new StackNode<>(value);

            while (true) {
                StackNode<T> currentHeadNode = head.get();
                newHeadNode.next = currentHeadNode;
                if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                }
            }
            counter.incrementAndGet();
        }

        public T pop() {
            StackNode<T> currentHeadNode = head.get();
            StackNode<T> newHeadNode;
            while (currentHeadNode != null) {
                newHeadNode = currentHeadNode.next;
                if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                    currentHeadNode = head.get();
                }
            }
            counter.incrementAndGet();
            return currentHeadNode == null ? null : currentHeadNode.value;
        }

        public int getCounter() {
            return counter.get();
        }
    }

    static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        public synchronized void push(T value) {
            var newHead = new StackNode<T>(value);
            newHead.next = head;
            head = newHead;
            counter++;
        }

        public synchronized T pop() {
            if (head == null) {
                counter++;
                return null;
            }
            T value = head.value;
            head = head.next;
            counter++;
            return value;
        }

        public int getCounter() {
            return counter;
        }
    }

    static class StackNode<T> {
        public T value;
        public StackNode<T> next;

        public StackNode(T value) {
            this.value = value;
            this.next = next;
        }
    }
}
