package rmi.core.manager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Manager extends Remote {
	public void setTranslatedVal(String val , String transVal,String language) throws RemoteException;
	public void notifyMangr() throws RemoteException;
}
