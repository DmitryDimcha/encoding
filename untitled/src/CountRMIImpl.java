
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CountRMIImpl extends UnicastRemoteObject
        implements CountRMI {
    private int sum;

    public CountRMIImpl(String name) throws RemoteException {
        super();
        try {
            Naming.rebind(name, this);
            sum = 0;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int sum() throws RemoteException {
        return sum;
    }

    public void sum(int val) throws RemoteException {
        sum = val;
    }

    public int increment() throws RemoteException {
        sum++;
        return sum;
    }
}