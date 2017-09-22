package rmi.common.properties;

import rmi.common.BaseProperties;

public class AdaptorProperties extends BaseProperties{

	private static AdaptorProperties adaptor = new AdaptorProperties();	
	
	public AdaptorProperties() {
		super("adaptor.properties");
	}
	
	private static AdaptorProperties getInstance()
	{
		if(adaptor == null) adaptor = new AdaptorProperties();
		return adaptor;
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
