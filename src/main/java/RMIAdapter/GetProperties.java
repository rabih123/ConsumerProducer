package RMIAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {
	public static void main(String[] args) {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "config.properties";
    		input = GetProperties.class.getClassLoader().getResourceAsStream(filename);
    		if(input==null){
    	            System.out.println("Sorry, unable to find " + filename);
    		    return;
    		}

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			System.out.println(prop.getProperty("Host"));
			System.out.println(prop.getProperty("Port"));
			System.out.println(prop.getProperty("user"));

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
}
