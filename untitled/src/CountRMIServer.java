import java.rmi.*;
import java.rmi.server.*;

public class CountRMIServer {

    public static void main(String args[]) {

        // Create and install the security manager
        System.setSecurityManager(new RMISecurityManager());

        try {
            // Create CountRMIImpl
            CountRMIImpl myCount = new CountRMIImpl("my CountRMI");
            System.out.println("CountRMI Server ready.");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}