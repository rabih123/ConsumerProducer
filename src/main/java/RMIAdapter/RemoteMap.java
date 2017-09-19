package RMIAdapter;

import java.rmi.RemoteException;

public interface RemoteMap extends java.rmi.Remote {
	   // Constants
	   public static final String SERVICENAME = "RemoteMap";
	   public int size() throws RemoteException;
	   public boolean isEmpty() throws RemoteException;
	   public boolean containsKey( Object key ) throws RemoteException;
	   public boolean containsValue( Object value ) throws RemoteException;
	   public Object get( Object key ) throws RemoteException;
	   public Object put( Object key, Object value ) throws RemoteException;
	   public Object remove( Object key ) throws RemoteException;
	   public void clear() throws RemoteException;
	}
