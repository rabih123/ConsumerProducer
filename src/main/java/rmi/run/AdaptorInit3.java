package rmi.run;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.core.adaptor.impl.AdapterImpl;

public class AdaptorInit3 {

	public static void main(String[] args) {
		try {
			new AdapterImpl(2);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
