package rmi.core.adaptor.impl;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import rmi.common.properties.AdaptorProperties;
import rmi.common.tools.GlobalConstants;
import rmi.core.CommonComponent;
import rmi.core.adaptor.Adaptor;
import rmi.core.adaptor.AvailableConnection;
import rmi.core.adaptor.Worker;
import rmi.core.manager.Manager;

public class AdapterImpl extends UnicastRemoteObject implements Adaptor,
		CommonComponent {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger
			.getLogger(GlobalConstants.DEFAULT_LOGGER_NAME);

	private static String adpthost;
	private static int adptPort;
	private int adaptorCpunt;
	private Manager Mangr = null;
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(3);

	public AdapterImpl(int adaptorCpunt) throws RemoteException,
			NotBoundException {
		this.adaptorCpunt = adaptorCpunt;
		loadProperties();
		init();
		yandisCnxReset();
	}

	public void loadProperties() {
		adpthost = AdaptorProperties.getHost();
		adptPort = Integer.parseInt(AdaptorProperties.getPort()) + adaptorCpunt;
	}

	public void init() throws NotBoundException {
		try {

			LocateRegistry.createRegistry(adptPort);
			Naming.rebind("//" + adpthost + ":" + adptPort + "/MyServer", this);

			System.out.println("Server-Side Ready on Port :" + adptPort);

		} catch (NumberFormatException e) {
			LOGGER.warning(e.toString());
		} catch (RemoteException e) {
			LOGGER.warning(e.toString());
		} catch (MalformedURLException e) {
			LOGGER.warning(e.toString());
		}

	}

	// Reset Counter of Api ynadex request
	private void yandisCnxReset() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Refresh Request Capacity");
				AvailableConnection.ResetConnection();
				try {
					Mangr.notifyMangr();
				} catch (RemoteException e) {
					LOGGER.warning(e.toString());
				}
			}
		}, 24 * 60 * 60 * 1000, 24 * 60 * 60 * 1000); // 24 hours

	}

	public int conxLimit() {
		return AvailableConnection.getConnection();
	}

	public void execute(String val) throws RemoteException {
		executor.submit(new Worker(val, Mangr));
	}

	public void setupManagerAdapterConx(String mnghost,String mngPort) throws RemoteException {

			try {
				LocateRegistry.getRegistry(mnghost, Integer.parseInt(mngPort));
				Mangr = (Manager) Naming.lookup("rmi://" + mnghost + ":"
						+ mngPort + "/MyServer");
			} catch (MalformedURLException e) {
				LOGGER.warning(e.toString());
			} catch (NotBoundException e) {
				LOGGER.warning(e.toString());
			} catch (RemoteException e) {
				LOGGER.warning(e.toString());
			}
		
	}

	public int ActiveThread() throws RemoteException {
		return executor.getActiveCount();
	}
}
