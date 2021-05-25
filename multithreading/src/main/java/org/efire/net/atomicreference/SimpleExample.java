package org.efire.net.atomicreference;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleExample {

    public static void main(String[] args) {
        String oldName = "old name";
        String newName = "new name";
        var ar = new AtomicReference<>(oldName);

        //ar.set("Unexpected name");
        if (ar.compareAndSet(oldName, newName)) {
            System.out.println("New value is: "+ar.get());
        } else {
            System.out.println("Nothing happens, value: "+ar.get());
        }
    }
}
