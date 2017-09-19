package BasicsProject.Project2.test2;

import java.util.List;
import java.util.Map;
import java.util.Objects;

class Producer implements Runnable {

	private List<String> taskQueue;
	private final int MAX_CAPACITY;
	private static String popVal;
	@SuppressWarnings("rawtypes")
	Map<String, List> queuelist;
	private List<String> trnslQueue;

	@SuppressWarnings("rawtypes")
	public Producer(Map<String, List> Queuelist, int size,
			List<String> trnslQueue) {
		this.queuelist = Queuelist;
		this.MAX_CAPACITY = size;
		this.trnslQueue = trnslQueue;
	}

	public void run() {

		RedisHandler r1 = new RedisHandler(Singleton.getInstance(
				GlobalConstants.JedisPoolConfig, "localhost"));
		while (true) {
			try {
				popVal = r1.LPop("QInf");

				if (popVal != null) {
					produce(popVal);
					// Translate(popVal);
				} else {
					try {
						// System.out.println("Waiting to read values....");
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void produce(String val) throws InterruptedException {

		synchronized (queuelist.get(TranslateAPI.detectLanguage(val))) {
			while (queuelist.get(TranslateAPI.detectLanguage(val)).size() == MAX_CAPACITY) {
				System.out.println("Queue is full "
						+ Thread.currentThread().getName()
						+ " is waiting , size: " + taskQueue.size());
				queuelist.get(TranslateAPI.detectLanguage(val)).wait();

			}
			queuelist.get(TranslateAPI.detectLanguage(val)).add(val);

			System.out.println("--------------------------- \nLanguage : "
					+ GlobalConstants.langs.get(TranslateAPI
							.detectLanguage(val)));
			System.out.println("Value Produced From the Queue: " + val);
			queuelist.get(TranslateAPI.detectLanguage(val)).notify();

			if (!Objects.equals(TranslateAPI.detectLanguage(val), "en")) {
				Translate(popVal);
			}
		}
	}

	private void Translate(String val) throws InterruptedException {
		synchronized (trnslQueue) {
			while (trnslQueue.size() == 1
					|| Objects.equals(TranslateAPI.detectLanguage(val), "en")) {
				trnslQueue.wait();
			}
			trnslQueue.add(val);
			trnslQueue.notify();
		}
	}
}
