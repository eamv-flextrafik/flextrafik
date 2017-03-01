package util;

public enum Month {

	Januar, Februar, Marts, April, Maj, Juni, Juli, August, September, Oktober, November, December;

	public static int toInt(String monthName) {
		Month[] months = Month.values();
		int index = 0;
		for (Month month : months) {
			if (month.toString().equalsIgnoreCase(monthName)) {
				index = month.ordinal() + 1;
			}
		}
		return index;
	}

	public static String of(int monthValue) {
		Month[] months = Month.values();
		return months[monthValue - 1].toString();
	}
	
}
