package rmi.core.manager.impl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.common.tools.RedisHandler;
import rmi.core.CommonComponent;
import rmi.core.manager.Manager;

public class QueueControllerImpl implements Runnable, CommonComponent
{
    private String popVal;
    private Object lockObject;
    private RedisHandler redisHandler;
    private Manager manager;

    public QueueControllerImpl(Object lockObject) {

    }

    public void loadProperties()
    {
	// TODO Auto-generated method stub

    }

    public void init() throws NotBoundException
    {
	// TODO Auto-generated method stub

    }

    public void run()
    {
	while (true)
	{
	    popVal = redisHandler.BLPop(0, "QInf").get(1);
	    if (popVal != null)
	    {
		this.manager.executeJob(popVal);

	    }
	}
    }

}
