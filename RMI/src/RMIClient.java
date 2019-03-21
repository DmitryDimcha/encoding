import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class RMIClient
{
	public static void main(String []args)
	{
		try
		{
			MethodImpl ob = (MethodImpl)Naming.lookup("rmi://localhost/ID");
			int i= ob.ID(1);
			System.out.println("Я "+i+" клиент!");

			MethodImpl ob2 = (MethodImpl)Naming.lookup("rmi://localhost/ADD");
			int j= ob2.add(10,20);
			System.out.println("Add = "+j);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}