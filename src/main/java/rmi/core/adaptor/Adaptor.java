package rmi.core.adaptor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Adaptor extends Remote
{
	public void translate(String val) throws RemoteException;
	public int conxLimit() throws RemoteException;
	public int ActiveThread() throws RemoteException;
}

