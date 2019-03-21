import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class RMIServer extends UnicastRemoteObject implements MethodImpl
{
	public RMIServer() throws RemoteException
	{
		System.out.println("Server Initiated ..");
	}
	
	public int sqr(int p1) throws RemoteException
	{
		return p1*p1;
	}

	@Override
	public int ID(int p1) throws RemoteException {
		return 0;
	}

	public int add(int p1, int p2) throws RemoteException
	{
		return p1+p2;
	}

	public static void main(String []args) throws Exception
	{
		RMIServer server = new RMIServer();
		Naming.rebind("ID",server);

		RMIServer server1 = new RMIServer();
		Naming.rebind("ADD",server1);
		try
		{

		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		
	}
}