package org.efire.net;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        var second_way_thread = new NewThread("Second Way Thread");
        System.out.println("Before the start thread:" +Thread.currentThread().getName());
        second_way_thread.start();
        System.out.println("After the start thread:" +Thread.currentThread().getName());
    }

    static class NewThread extends Thread {
        public NewThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println("Running new thread "+ this.currentThread().getName());
        }
    }
}
