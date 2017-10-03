package rmi.core.adaptor;

import rmi.common.tools.GlobalConstants;

public class AvailableConnection {
	private static AvailableConnection availableConnection = new AvailableConnection();
	private int connexLimit = GlobalConstants.conxLimit;

	private static AvailableConnection getInstance() {
		if (availableConnection == null)
			synchronized (AvailableConnection.class) {
				if (availableConnection == null) {
					availableConnection = new AvailableConnection();
				}
			}
		return availableConnection;
	}

	public static int getConnection() {
		synchronized (getInstance()) {
			return getInstance().connexLimit;
		}
	}

	public static void DecrementConnex(int val) {
		synchronized (getInstance()) {
			getInstance().connexLimit = getInstance().connexLimit - val;
		}
	}
	
	public static void ResetConnection() {
		synchronized (getInstance()) {
			getInstance().connexLimit = GlobalConstants.conxLimit;
		}
	}

}
