package RMIAdapter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class RemoteMapAdapter extends UnicastRemoteObject
implements RemoteMap {
// Constructors
// The owner publishes this service in the RMI registry.
@SuppressWarnings("unchecked")
public RemoteMapAdapter( @SuppressWarnings("rawtypes") Hashtable adaptee ) throws RemoteException {
   this.adaptee = adaptee;
}
// RemoteMap role
public int size() throws RemoteException {
   return adaptee.size(); }
public boolean isEmpty() throws RemoteException {
   return adaptee.isEmpty(); }
public boolean containsKey( Object key ) throws RemoteException {
   return adaptee.containsKey( key ); }
public boolean containsValue( Object value ) throws RemoteException {
   return adaptee.contains( value ); }
public Object get( Object key ) throws RemoteException {
   return adaptee.get( key ); }
public Object put( Object key, Object value ) throws RemoteException {
   return adaptee.put( key, value ); }
public Object remove( Object key ) throws RemoteException {
   return adaptee.remove( key ); }
public void clear() throws RemoteException {
   adaptee.clear(); }
// Fields
protected Hashtable<Object, Object> adaptee;
}
