package view.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertBox {

	private String message;
	private Alert alertBox;
	private Alert.AlertType alertType;
	private Optional<ButtonType> btnType;

	public AlertBox(String message, AlertType alertType) {
		this.message = message;
		this.alertType = alertType;
		createAlertBox();
	}

	private void createAlertBox() {
		switch (alertType) {
		case CONFIRMATION:
			alertBox = new Alert(AlertType.CONFIRMATION);
			break;
		case ERROR:
			alertBox = new Alert(AlertType.ERROR);
			break;
		case INFORMATION:
			alertBox = new Alert(AlertType.INFORMATION);
			break;
		case WARNING:
			alertBox = new Alert(AlertType.WARNING);
			break;
		case NONE:
			alertBox = new Alert(AlertType.NONE);
			break;
		}
		alertBox.setTitle("Flextur");
		alertBox.setHeaderText(null);
		alertBox.setContentText(this.message);
		btnType = alertBox.showAndWait();
	}

	public void executeOnConfirm(Execute exe) {
		if (btnType.isPresent()) {
			if (btnType.get() == ButtonType.OK) {
				exe.execute();
			}
		}
	}

}
