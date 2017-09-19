package BasicsProject.Project2.test2;

import java.util.Map;

import redis.clients.jedis.JedisPoolConfig;

public class GlobalConstants {
	public static final String DEFAULT_LOGGER_NAME = "Global_Logger";
	public static final JedisPoolConfig JedisPoolConfig = new JedisPoolConfig();
	public static String detctedLang = null;
	public static String pushVal = null;
	public static Map<String, String> langs = null;
	public static String translatedValue = null;

}
