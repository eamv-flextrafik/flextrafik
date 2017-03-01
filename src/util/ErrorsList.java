package util;

import java.util.ArrayList;
import java.util.List;

public class ErrorsList<T> {

	private List<T> errors = new ArrayList<>();

	public void addError(T t) {
		if (!this.contains(t)) {
			errors.add(t);
		}
	}

	public boolean contains(T t) {
		return errors.contains(t);
	}

	public void removeError(T t) {
		if (this.contains(t)) {
			errors.remove(t);
		}
	}

	public boolean hasErrors() {
		return (errors.size() > 0);
	}

	public List<T> getErrors() {
		return errors;
	}
	
}
