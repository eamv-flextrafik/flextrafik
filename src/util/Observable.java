package util;

import java.util.ArrayList;
import java.util.List;

public interface Observable {

	public List<Observer> observers = new ArrayList<>();

	default public void registrateObserver(Observer observer) {
		if (observer != null && !observers.contains(observer)) {
			observers.add(observer);
		}
	}

	default public void notifyObservers(Observable observable, Object state) {
		for (Observer observer : observers) {
			observer.update(observable, state);
		}
	}

	default public void unRegistrateObserver(Observer observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

}
