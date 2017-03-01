package util;

public class ThreadSleeper {

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException exc) {
		}
	}

}
