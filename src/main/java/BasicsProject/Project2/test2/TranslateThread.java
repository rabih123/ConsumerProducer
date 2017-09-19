package BasicsProject.Project2.test2;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TranslateThread implements Runnable {

	private static String pushVal = null;
	private List<String> trnslQueue;

	public TranslateThread(List<String> trnslQueue) {
		this.trnslQueue = trnslQueue;
	}

	public void run() {
		while (true) {
			try {
				Translate();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean Translate() throws InterruptedException, IOException {
		synchronized (trnslQueue) {
			RedisHandler r1 = new RedisHandler(Singleton.getInstance(
					GlobalConstants.JedisPoolConfig, "localhost"));

			while (trnslQueue.isEmpty()
					|| Objects.equals(
							TranslateAPI.detectLanguage(trnslQueue.get(0)),
							"en")) {
				trnslQueue.wait();
			}

			pushVal = TranslateAPI.translate(trnslQueue.get(0),
					TranslateAPI.detectLanguage(trnslQueue.get(0)),
					TranslateAPI.getKey(GlobalConstants.langs, "english"));
			r1.LPush("Quen", pushVal);
			System.out.println("Value Translated from: " + trnslQueue.get(0)
					+ " to " + pushVal + " ");
			trnslQueue.remove(0);
			trnslQueue.notify();
			return true;

		}
	}

}
