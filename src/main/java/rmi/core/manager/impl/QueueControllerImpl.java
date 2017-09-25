package rmi.core.manager.impl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import rmi.common.tools.GlobalConstants;
import rmi.common.tools.RedisHandler;
import rmi.common.tools.Singleton;
import rmi.core.CommonComponent;
import rmi.core.manager.Manager;

public class QueueControllerImpl implements Runnable, CommonComponent {
	private String popVal;
	private static RedisHandler redisHandler = new RedisHandler(
			Singleton.getInstance(GlobalConstants.JedisPoolConfig, "localhost"));
	private Manager manager;
	private final static Logger LOGGER = Logger.getLogger(GlobalConstants.DEFAULT_LOGGER_NAME);

	public QueueControllerImpl(Manager manager) {
		this.manager = manager;
	}

	public void loadProperties() {

	}

	public void init() throws NotBoundException {

	}

	public void run() {
		while (true) {
			popVal = redisHandler.BLPop(0, "QInf").get(1);
			if (popVal != null) {
				try {
					this.manager.executeJob(popVal);
				} catch (RemoteException e) {
					e.printStackTrace();
					LOGGER.warning(e.toString());
				}
			}
		}
	}

}
