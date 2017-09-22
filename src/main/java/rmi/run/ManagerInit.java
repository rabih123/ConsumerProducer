package rmi.run;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.core.manager.impl.ManagerImpl;

public class ManagerInit {
	public static void main(String[] args) throws NotBoundException {
		try {
			new ManagerImpl();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
