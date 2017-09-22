package rmi.common.properties;

import rmi.common.BaseProperties;

public class ManagerProperties extends BaseProperties{

	private static ManagerProperties manager = new ManagerProperties();
	
	private ManagerProperties() {
		super("manager.properties");
	}
	
	private static ManagerProperties getInstance()
	{
		if(manager == null) manager = new ManagerProperties();
		return manager;
	}

	public static String getPort()
	{
		return getInstance().getProp().getProperty("Port");
	}
	
	public static String getHost()
	{
		return String.valueOf(getInstance().getProp().getProperty("Host"));
	}
	
	public static String getUser()
	{
		return getInstance().getProp().getProperty("user");
	}
}

