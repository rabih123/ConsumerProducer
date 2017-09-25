package rmi.core.adaptor;

import java.io.IOException;
import java.util.logging.Logger;

import rmi.common.tools.GlobalConstants;
import rmi.common.tools.TranslateAPI;
import rmi.core.manager.Manager;

public class Worker implements Runnable {
	private final static Logger LOGGER = Logger
			.getLogger(GlobalConstants.DEFAULT_LOGGER_NAME);
	
	private String val;
	private String detectLang;
	private String Transval;
	private Manager Mangr;

	public Worker(String val, Manager Mangr) {
		this.val = val;
		this.Mangr = Mangr;
	}

	public void run() {
		detectLang = TranslateAPI.detectLanguage(val);
		System.out.println("Value Received");

		try {
			Transval = TranslateAPI.translate(val, detectLang, "en");
			AvailableConnection.DecrementConnex(2);
			Mangr.setTranslatedVal(val, Transval, detectLang);
			System.out.println("Available Connection : "
					+ AvailableConnection.getConnection());
			Mangr.notifyMangr();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.warning(e.toString());
		}
	}

}
