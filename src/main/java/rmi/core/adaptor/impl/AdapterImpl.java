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

import rmi.common.GlobalVariables;
import rmi.common.properties.AdaptorProperties;
import rmi.common.properties.ManagerProperties;
import rmi.common.tools.GlobalConstants;
import rmi.core.CommonComponent;
import rmi.core.adaptor.Adaptor;
import rmi.core.adaptor.Worker;
import rmi.core.manager.Manager;

public class AdapterImpl extends UnicastRemoteObject implements Adaptor,
		CommonComponent {

	private static final long serialVersionUID = 1L;

	private static String adpthost;
	private static String adptPort;
	private static String mnghost;
	private static String mngPort;
	private Manager Mangr = null;
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(3);

	public AdapterImpl() throws RemoteException, NotBoundException {
		loadProperties();
		init();
		yandisCnxReset();
	}

	public void loadProperties() {
		adpthost = AdaptorProperties.getHost();
		adptPort = AdaptorProperties.getPort();
		mnghost = ManagerProperties.getHost();
		mngPort = ManagerProperties.getPort();
	}

	public void init() throws NotBoundException {
		try {

			LocateRegistry.createRegistry(Integer.parseInt(adptPort));
			Naming.rebind("//" + adpthost + ":" + adptPort + "/MyServer", this);

			System.out.println("Server-Side Ready");

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	// Reset Counter of Api ynadex request
	private void yandisCnxReset() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Refresh Request Capacity");
				GlobalVariables.conxLimit = GlobalConstants.conxLimit;
				ConnectToAdapter();
				try {
					Mangr.notifyMangr();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}, 24 * 60 * 60 * 1000, 24 * 60 * 60 * 1000); // 24 hours

	}

	public int conxLimit() {
		return GlobalVariables.conxLimit;
	}

	public void translate(String val) throws RemoteException {
		ConnectToAdapter();
		executor.submit(new Worker(val, Mangr));
	}

	public static void main(String[] args) throws RemoteException,
			MalformedURLException {

	}

	private void ConnectToAdapter() {
		if (Mangr == null) {
			try {
				Mangr = (Manager) Naming.lookup("rmi://" + mnghost + ":"
						+ mngPort + "/MyServer");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public int ActiveThread() throws RemoteException {
		return executor.getActiveCount();
	}

}
