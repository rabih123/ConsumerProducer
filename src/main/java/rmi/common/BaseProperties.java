package rmi.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseProperties {
	private final static Properties prop = new Properties();
	protected static InputStream input = null;

	public BaseProperties(String path) {
		try {
			input = new FileInputStream(System.getProperty("user.dir") + "/" + path);
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected Properties getProp()
	{
		//TODO check if already loaded
		return prop;
	}
}
