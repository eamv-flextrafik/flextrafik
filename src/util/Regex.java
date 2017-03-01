package util;

public enum Regex {

	NUMERIC("[0-9]+"),
	NUMERIC_DOUBLE("[1-9]*\\.?[0-9]+"),
	NUMERIC_OR_EMPTY("[0-9]*"),
	ONE_NUMBER_FROM_ONE_TO_FIVE("[1-5]{1}"),
	TIME("([0-1][0-9]|2[0-3]):[0-5][0-9]");
	
	private String regex;
	
	private Regex(String regex) {
		this.regex = regex;
	}
	
	public String getRegexString() {
		return this.regex;
	}
	
}
