package org.efire.net;

import java.util.ArrayList;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final int MAX_PASSWORD = 9999;
    public static void main(String[] args) {
        var threads = new ArrayList<Thread>();
        var random = new Random();
        var vault = new Vault(random.nextInt(MAX_PASSWORD));
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceHackerThread());
        for (Thread t: threads) {
            t.start();
        }
    }

    static class Vault {
        private int password;

        public Vault(int password) {
            this.password = password;
        }

        public boolean isCorrectPassword(int password) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.password == password;
        }
    }

    static abstract class HackerThread extends Thread {
        protected Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("Starting new thread: "+this.getName());
            super.start();
        }
    }

    static class AscendingHackerThread extends HackerThread {

        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = 0; i < MAX_PASSWORD; i++) {
                if (vault.isCorrectPassword(i)) {
                    System.out.println(this.getName()+ " guess the password");
                    System.exit(0);
                }
            }
        }
    }

    static class DescendingHackerThread extends HackerThread {

        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = MAX_PASSWORD; i > 0 ; i--) {
                if (vault.isCorrectPassword(i)) {
                    System.out.println(this.getName() + " guess the password");
                    System.exit(0);
                }
            }
        }
    }

    static class PoliceHackerThread extends Thread {
        @Override
        public void run() {
            for (int i = 10; i > 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
            System.out.println("Game Over Hackers");
            System.exit(0);
        }
    }
}
