package BasicsProject.Project2.test2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ProccessClass {

	public static void main(String[] args) throws IOException,
			InterruptedException {

		System.out.println("Starting Process");

		int MAX_CAPACITY = 5;
		GlobalConstants.langs = TranslateAPI.getLangs();
		List<String> trnslQueue = new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Map<String, List> queueList = new HashMap<String, List>();

		Thread ttrns = new Thread(new TranslateThread(trnslQueue), "ttrns");
		ttrns.start();

		for (Entry<String, String> entry : GlobalConstants.langs.entrySet()) {
			queueList.put(entry.getKey(), new ArrayList<String>());
			Thread t = new Thread(new Consumer(queueList, entry.getKey()), "t"
					+ entry.getKey());
			t.start();
		}
		Thread tProducer = new Thread(new Producer(queueList, MAX_CAPACITY,
				trnslQueue), "Producer");
		tProducer.start();
	}

}
