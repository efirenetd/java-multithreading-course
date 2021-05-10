package org.efire.net;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        System.out.println( "Hello World!" );
        var newThread = new Thread(() -> {
            System.out.println("Running newThread - "+Thread.currentThread().getName());
            System.out.println("Get newThread Priority: "+ Thread.currentThread().getPriority());

        });
        newThread.setName("New Worker Thread");
        newThread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("Before the new newThread start - We are in Thread - "+Thread.currentThread().getName());
        newThread.start();
        System.out.println("After the new newThread start - We are in Thread - "+Thread.currentThread().getName());

        Thread.sleep(15_000);
    }
}
