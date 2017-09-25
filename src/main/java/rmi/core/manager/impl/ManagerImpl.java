package rmi.core.manager.impl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import rmi.common.properties.AdaptorProperties;
import rmi.common.properties.ManagerProperties;
import rmi.common.tools.GlobalConstants;
import rmi.common.tools.RedisHandler;
import rmi.common.tools.Singleton;
import rmi.core.CommonComponent;
import rmi.core.adaptor.Adaptor;
import rmi.core.adaptor.AdaptorSettings;
import rmi.core.manager.Manager;

public class ManagerImpl extends UnicastRemoteObject implements
		CommonComponent, Manager {
	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger
			.getLogger(GlobalConstants.DEFAULT_LOGGER_NAME);
	private static String mnghost;
	private static String mngPort;

	private static RedisHandler r1 = new RedisHandler(Singleton.getInstance(
			GlobalConstants.JedisPoolConfig, "localhost"));
	private List<Adaptor> adaptors = new ArrayList<Adaptor>();
	private Object lockObject = new Object();
	private List<AdaptorSettings> adaptorSettings = new ArrayList<AdaptorSettings>();
	private Adaptor runingAdaptor;

	public ManagerImpl() throws RemoteException, NotBoundException {
		loadProperties();
		init();
	}

	public void loadProperties() {
		mnghost = ManagerProperties.getHost();
		mngPort = ManagerProperties.getPort();
		for (int i = 0; i < 3; i++) {
			adaptorSettings.add(new AdaptorSettings());
			adaptorSettings.get(i).host = AdaptorProperties.getHost();
			adaptorSettings.get(i).port = Integer.parseInt(AdaptorProperties
					.getPort()) + i;
		}
	}

	public void init() throws NotBoundException {

		try {

			// Create manager registry
			LocateRegistry.createRegistry(Integer.parseInt(mngPort));
			Naming.rebind("//" + mnghost + ":" + mngPort + "/MyServer", this);

		} catch (NumberFormatException e) {
			e.printStackTrace();
			LOGGER.warning(e.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
			LOGGER.warning(e.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			LOGGER.warning(e.toString());
		}

		connectAdaptors();

		Thread runManager = new Thread(new QueueControllerImpl(this));
		runManager.start();

		System.out.println("Starting Process");
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
		synchronized (lockObject) {
			lockObject.notify();
		}
	}

	public void executeJob(String popVal) {

		synchronized (lockObject) {
			try {

				while (getActiveAdaptor() == null
						|| runingAdaptor.conxLimit() <= 0) {
					try {
						if (runingAdaptor.conxLimit() <= 0) {
							System.out
									.println("Connection reached the Maximun");
						} else {
							System.out.println("No Workers Available");
						}
						lockObject.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						LOGGER.warning(e.toString());
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				LOGGER.warning(e.toString());
			}
		}
		try {
			System.out.println("Value Produced from Queue : " + popVal);
			runingAdaptor.execute(popVal);
		} catch (RemoteException e) {
			e.printStackTrace();
			LOGGER.warning(e.toString());
		}

	}

	public void connectAdaptors() {
		for (AdaptorSettings Adpt : adaptorSettings) {
		try {
			
				adaptors.add((Adaptor) Naming.lookup("rmi://" + Adpt.host + ":"
						+ Adpt.port + "/MyServer"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			LOGGER.warning(e.toString());
		} catch (RemoteException e) {
			System.out.println("Connection to Adaptor On Port :" + Adpt.port + " Is Refused.");
			LOGGER.warning(e.toString()); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
			LOGGER.warning(e.toString());
		} catch (NotBoundException e) {
			e.printStackTrace();
			LOGGER.warning(e.toString());
		}
		}
	}

	private Adaptor getActiveAdaptor() {
		for (Adaptor Adpt : adaptors) {
			try {
				if (Adpt.ActiveThread() < 3) {
					System.out.println("1");
					runingAdaptor = Adpt;
					return Adpt;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				LOGGER.warning(e.toString());
			}

		}
		return null;
	}
}
