package rmi.core.adaptor;

import java.rmi.NotBoundException;

import rmi.core.CommonComponent;

public class AdaptorSettings implements CommonComponent {
	public String host;
	public int port;
	
	public AdaptorSettings() {
		
	}
	
	public void loadProperties() {
	}

	public void init() throws NotBoundException {
		// TODO Auto-generated method stub
		
	}

}
