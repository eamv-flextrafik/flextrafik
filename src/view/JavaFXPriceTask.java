package view;

import domain.FlexDrive;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Task;
import logic.FTController;

public class JavaFXPriceTask {

	private Task<Void> task;

	public JavaFXPriceTask(FlexDrive flexDrive, FTController ftController) {
		ftController.calculatePrice(flexDrive);
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				boolean isPriceCalculated = false;
				while (!isPriceCalculated) {
					isPriceCalculated = ftController.isPriceCalculationDone();
					Thread.sleep(500);
				}
				double price = ftController.getPrice().getValue();
				updateMessage(String.valueOf(price));
				return null;
			}
		};
	}
	
	public ReadOnlyStringProperty messageProperty() {
		return task.messageProperty();
	}
	
	public void start() {
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}

}