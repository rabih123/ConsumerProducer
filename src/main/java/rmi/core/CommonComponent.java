package rmi.core;

import java.rmi.NotBoundException;

public interface CommonComponent
{
	void loadProperties();
	void init() throws NotBoundException;
}
