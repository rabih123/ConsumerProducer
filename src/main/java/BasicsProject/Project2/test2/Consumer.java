package BasicsProject.Project2.test2;

import java.util.List;
import java.util.Map;
import java.util.Objects;

class Consumer implements Runnable {
	private final List<String> taskQueue;
	private String lang = null;
	private static String pushVal = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Consumer(Map<String, List> Queuelist, String lang) {
		this.taskQueue = Queuelist.get(lang);
		this.lang = lang;
	}

	public void run() {
		while (true) {
			try {
				consume();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	private boolean consume() throws InterruptedException {
		synchronized (taskQueue) {
			RedisHandler r1 = new RedisHandler(Singleton.getInstance(
					GlobalConstants.JedisPoolConfig, "localhost"));
			while (taskQueue.isEmpty()
					|| (Objects.equals(Thread.currentThread().getName(), "t"
							+ TranslateAPI.detectLanguage(taskQueue.get(0))) == false)) {
			//	System.out.println("Queue is empty "
				//		+ Thread.currentThread().getName() + " is waiting");
				taskQueue.wait();
			}

			// System.out.println(Thread.currentThread().getName() + " " +
			// GlobalConstants.detctedLang);
			pushVal = taskQueue.get(0);
			r1.LPush("Qu" + lang, pushVal);
			System.out.println("Value Inserted into  Qu" + lang + " : "
					+ pushVal + " ");
			taskQueue.remove(0);
			taskQueue.notifyAll();
			return true;

		}
	}
}
