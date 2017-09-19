package BasicsProject.Project2.test2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class TranslateAPI {
	public static final String API_KEY = "trnsl.1.1.20170915T064247Z.d95873f904cdc01a.5b418c26069eadff544087b8ef75c0cd7131d9db";
	
	private static String request(String URL) throws IOException {
		String recieved=null;
		try {
			URL url = new URL(URL);
			URLConnection urlConn = url.openConnection();
			urlConn.addRequestProperty("User-Agent", "Google");
			
			InputStream inStream = urlConn.getInputStream();
			
			recieved = new BufferedReader(new InputStreamReader(inStream)).readLine();
			
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recieved;
	}
	
	public static Map<String, String> getLangs() throws IOException {
		Map<String, String> languages = new HashMap<String, String>();
		
		try {
			String langs = request("https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=" + API_KEY + "&ui=en");
			langs = langs.substring(langs.indexOf("langs")+7);
			langs = langs.substring(0, langs.length()-1);
			
			String[] splitLangs = langs.split(",");
			

			for (String s : splitLangs) {
				String[] s2 = s.split(":");
				
				String key = s2[0].substring(1, s2[0].length()-1);
				String value = s2[1].substring(1, s2[1].length()-1);
				
				languages.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return languages;
	}
	
	public static String translate(String text, String sourceLang, String targetLang) throws IOException {
		String response =null;
		try {
			response = request("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + API_KEY + "&text=" + text + "&lang=" + sourceLang + "-" + targetLang);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.substring(response.indexOf("text")+8, response.length()-3);
	}
	
	public static String detectLanguage(String text)  {
		String response =null;
		try {
			 response = request("https://translate.yandex.net/api/v1.5/tr.json/detect?key=" + API_KEY + "&text=" + text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.substring(response.indexOf("lang")+7, response.length()-2);
	}
	
	public static String getKey(Map<String, String> map, String value) {
		for (String key : map.keySet()) {
			if (map.get(key).equalsIgnoreCase(value)) {
				return key;
			}
		}
		return null;
	}
}