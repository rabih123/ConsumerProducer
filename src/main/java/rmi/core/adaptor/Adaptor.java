package rmi.core.adaptor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Adaptor extends Remote
{
	public void execute(String val) throws RemoteException;
	public int conxLimit() throws RemoteException;
	public int ActiveThread() throws RemoteException;
	public void connectToManager(String mnghosta,String mngPorta) throws RemoteException;
}

