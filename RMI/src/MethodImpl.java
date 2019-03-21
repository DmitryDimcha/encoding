import java.rmi.*;
interface MethodImpl extends Remote
{
	public int ID(int p1) throws RemoteException;
	public int add(int p1,int p2) throws RemoteException;
}