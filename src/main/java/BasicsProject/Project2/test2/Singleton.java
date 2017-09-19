package BasicsProject.Project2.test2;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Singleton {

	private static JedisPool jedisPool = null;

	/*
	 * A private Constructor prevents any other class from instantiating.
	 */
	private Singleton() {
	}

	/* Static 'instance' method */
	public static JedisPool getInstance(JedisPoolConfig config, String Host) {
		try {
			if (jedisPool == null) {
				jedisPool = new JedisPool(config, Host);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return jedisPool;

	}

}
