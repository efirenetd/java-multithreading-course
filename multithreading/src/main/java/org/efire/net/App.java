package org.efire.net;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        var newThread = new Thread(() -> {
            //Throw unchecked exception
            throw new RuntimeException("Error occurred");
        });
        newThread.setName("Misbehaving Thread");
        newThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A critical error happens in the thread!!");
            }
        });
        newThread.start();
    }
}
