package rmi.core.manager.impl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;

import rmi.common.properties.AdaptorProperties;
import rmi.common.properties.ManagerProperties;
import rmi.common.tools.GlobalConstants;
import rmi.common.tools.RedisHandler;
import rmi.common.tools.Singleton;
import rmi.core.CommonComponent;
import rmi.core.adaptor.Adaptor;
import rmi.core.manager.Manager;

public class ManagerImpl extends UnicastRemoteObject implements
		CommonComponent, Manager {

	private static String mnghost;
	private static String mngPort;
	private static String adpthost;
	private static String adptPort;
	private static RedisHandler r1 = new RedisHandler(Singleton.getInstance(
			GlobalConstants.JedisPoolConfig, "localhost"));
	private String popVal;
	private Adaptor Adpt;
	private Object LockObject = new Object();

	public ManagerImpl() throws RemoteException, NotBoundException {
		loadProperties();
		init();
		StartReading();
	}

	private static final long serialVersionUID = 1L;

	public void loadProperties() {
		mnghost = ManagerProperties.getHost();
		mngPort = ManagerProperties.getPort();
		adpthost = AdaptorProperties.getHost();
		adptPort = AdaptorProperties.getPort();
	}

	public void init() throws NotBoundException {
		try {
			// Create manager registry
			LocateRegistry.createRegistry(Integer.parseInt(mngPort));
			Naming.rebind("//" + mnghost + ":" + mngPort + "/MyServer", this);

			// Connect to Adapter
			Adpt = (Adaptor) Naming.lookup("rmi://" + adpthost + ":" + adptPort
					+ "/MyServer");

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void StartReading() {
		System.out.println("Starting Process");

		Thread ttrns = new Thread(new Runnable() {

			public void run() {

				while (true) {
					popVal = r1.BLPop(0,"QInf").get(1);
					if (popVal != null) {

						synchronized (LockObject) {
							try {
								while (Adpt.conxLimit() <= 0
										|| Adpt.ActiveThread() == 3) {
									try {
										if (Adpt.conxLimit() <= 0) {
											System.out.println("Connection reached the Maximun");
										} else {
											System.out.println("No Workers Available");
										}
										LockObject.wait();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
						try {
							System.out.println("Value Produced from Queue : "+ popVal);
							Adpt.translate(popVal);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					} 
				}
			}
		}, "trhMng");

		ttrns.start();
	}

	public void setTranslatedVal(String val, String transVal, String language)
			throws RemoteException {
		r1.LPush("QuTranslated", transVal);
		Locale loc = new Locale(language);
		System.out.println("language : " + loc.getDisplayLanguage(loc));
		System.out.println("Value translated From  : " + val + " to "
				+ transVal);
	}

	public void notifyMangr() throws RemoteException {
		synchronized (LockObject) {
			LockObject.notify();
		}
	}
}
