package util;

public class Validation {

	public boolean regex(String input, Regex regex) {
		return input.matches(regex.getRegexString());
	}

	public boolean stringRequired(String input) {
		return input.length() >= 1;
	}

	public boolean greaterThan(int input, int constraint) {
		return input > constraint;
	}

	public boolean lowerThan(int input, int constraint) {
		return input < constraint;
	}

	public boolean equalsTo(int input, int constraint) {
		return input == constraint;
	}
	
}
