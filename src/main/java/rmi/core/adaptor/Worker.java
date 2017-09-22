package rmi.core.adaptor;

import java.io.IOException;

import rmi.common.GlobalVariables;
import rmi.common.tools.TranslateAPI;
import rmi.core.manager.Manager;

public class Worker implements Runnable {
	private String val;
    private String detectLang;
    private String Transval;
    private Manager Mangr;

	public Worker(String val,Manager Mangr) {
		this.val = val;
		this.Mangr = Mangr;
	}

	public void run() {
		detectLang=TranslateAPI.detectLanguage(val);
		System.out.println("Value Received");
		
		try {
			Transval=TranslateAPI.translate(val,detectLang,"en");
			GlobalVariables.conxLimit=GlobalVariables.conxLimit-2;
			Mangr.setTranslatedVal(val, Transval,detectLang);
			System.out.println("Available Connection : " + GlobalVariables.conxLimit);
			Mangr.notifyMangr();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
