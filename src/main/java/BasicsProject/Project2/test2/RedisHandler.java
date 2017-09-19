package BasicsProject.Project2.test2;

import java.util.List;
import java.util.logging.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisHandler {
	private final static Logger LOGGER = Logger
			.getLogger(GlobalConstants.DEFAULT_LOGGER_NAME);

	private JedisPool JedisPool=null;
	private JedisPoolConfig JedisPoolConfig = new JedisPoolConfig();

	public RedisHandler(JedisPool JedisPool) {
		this.JedisPool=JedisPool ;
		// JedisPool JedisPool =
		// Singleton.getInstance(JedisPoolConfig,"localhost");
	}

	/*
	 * public static JedisPool getInstance() { if (JedisPool == null) {
	 * JedisPool= new JedisPool(new JedisPoolConfig(), "localhost"); } return
	 * JedisPool; }
	 */

	public void set(String keytoset, String ValtoSet) {

		try {
			Jedis j1 = Singleton.getInstance(JedisPoolConfig, "localhost")
					.getResource();
			j1.set(keytoset, ValtoSet);
			j1.close();
		} catch (Exception e) {
			LOGGER.warning(e.toString());
		}
	}

	public String get(String keytoset) {
		String _Val = null;

		try {
			Jedis j1 = Singleton.getInstance(JedisPoolConfig, "localhost")
					.getResource();
			_Val = j1.get(keytoset);
			j1.close();
		} catch (Exception e) {
			LOGGER.warning(e.toString());
		}
		return _Val;
	}

	public void LPush(String List, String ValtoSet) {

		try {
			Jedis j1 = Singleton.getInstance(JedisPoolConfig, "localhost")
					.getResource();
			j1.lpush(List, ValtoSet);
			j1.close();
		} catch (Exception e) {
			LOGGER.warning(e.toString());
		}
	}

	public List<String> lrange(String List, int startidx, int endIdx) {
		List<String> _Val = null;
		try {
			Jedis j1 = Singleton.getInstance(JedisPoolConfig, "localhost")
					.getResource();
			_Val = j1.lrange(List, startidx, endIdx);
			j1.close();
		} catch (Exception e) {
			LOGGER.warning(e.toString());
		}
		return _Val;
	}
	
	public String LPop(String List) {
		String _Val = null;
		try {
			Jedis j1 = JedisPool.getResource();
			_Val=j1.lpop(List);
			j1.close();
		} catch (Exception e) {
			LOGGER.warning(e.toString());
		}
		return _Val ;
	}
	
	public Boolean existList(String List) {
		boolean _val=false;
		try {
			Jedis j1 = JedisPool.getResource();
			if (j1.lindex(List,0)==null) {
				_val=false;
			}else {
				_val=true;
			}
			
			j1.close();
		} catch (Exception e) {
			LOGGER.warning(e.toString());
		}
		return _val ;
		
	}

}