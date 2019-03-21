import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class CountRMIClient {
    public static void main(String args[]) { // Create and install the security manager
        System.setSecurityManager(new RMISecurityManager());

        try {
            CountRMI myCount = (CountRMI) Naming.lookup("rmi://"
                    + args[0] + "/" + "my CountRMI");

            // Set Sum to initial value of 0
            System.out.println("Setting Sum to 0");
            myCount.sum(0);

            // Calculate Start time
            long startTime = System.currentTimeMillis();

            // Increment 1000 times
            System.out.println("Incrementing");
            for (int i = 0; i < 1000; i++) {
                myCount.increment();
            }

            // Calculate stop time; print out statistics
            long stopTime = System.currentTimeMillis();
            System.out.println("Avg Ping = "
                    + ((stopTime - startTime) / 1000f)
                    + " msecs");
            System.out.println("Sum = " + myCount.sum());
        } catch (Exception e) {
            System.err.println("System Exception" + e);
        }
        System.exit(0);
    }
}