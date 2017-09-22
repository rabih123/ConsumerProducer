package rmi.run;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.core.adaptor.impl.AdapterImpl;

public class AdaptorInit {
	public static void main(String[] args) throws NotBoundException {
		try {
			new AdapterImpl();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
