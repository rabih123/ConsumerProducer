package rmi.common.tools;

import java.io.IOException;
import java.util.Map;

import redis.clients.jedis.JedisPoolConfig;

public class GlobalConstants {
	
	 static {
		try {
			langs = TranslateAPI.getLangs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static final String DEFAULT_LOGGER_NAME = "Global_Logger";
	public static final JedisPoolConfig JedisPoolConfig = new JedisPoolConfig();
	public static String detctedLang = null;
	public static String pushVal = null;
	public static Map<String, String> langs = null;
	public static String translatedValue = null;
	public static final int conxLimit=1000000;
}
