package domain;

import util.Observable;

public class Price implements Observable {

	private double value;

	public Price() {
		
	}

	public Price(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
		notifyObservers(this, null);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
