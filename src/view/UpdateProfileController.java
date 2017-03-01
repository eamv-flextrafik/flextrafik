package view;

import java.net.URL;
import java.util.ResourceBundle;

import domain.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.FTController;
import logic.State;
import util.ErrorsList;
import util.Observable;
import util.Observer;
import util.Validation;
import view.util.AlertBox;
import view.util.CloseEventHandler;

public class UpdateProfileController implements Initializable, FlexTrafikController, Observer {

	private FTController ftController;
	private Stage stage;
	private ErrorsList<Node> errorsList = new ErrorsList<>();
	private Validation validate = new Validation();

	@FXML
	private TextField textUsername;
	@FXML
	private TextField textPassword;
	@FXML
	private TextField textName;
	@FXML
	private TextField textPhonenumber;
	@FXML
	private Button btnUpdate;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void setLogicController(FTController ftsController) {
		this.ftController = ftsController;
		this.ftController.registrateObserver(this);
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void init() {
		CloseEventHandler.closeStage(stage, () -> ftController.unRegistrateObserver(this));

		textUsername.setText(User.instance().getUserName());
		textPassword.setText(User.instance().getPassword());
		textName.setText(User.instance().getName());
		textPhonenumber.setText(Integer.valueOf(User.instance().getPhoneNumber()).toString());

		textPassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					if (validate.stringRequired(textPassword.getText())) {
						errorsList.removeError(textPassword);
						textPassword.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textPassword);
						textPassword.setStyle("-fx-border-color: red");
					}
					buttonState();
				}
			}
		});

		textName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					if (validate.stringRequired(textName.getText())) {
						errorsList.removeError(textName);
						textName.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textName);
						textName.setStyle("-fx-border-color: red");
					}
					buttonState();
				}
			}
		});

		textPhonenumber.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					if (validate.stringRequired(textPhonenumber.getText())) {
						errorsList.removeError(textPhonenumber);
						textPhonenumber.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textPhonenumber);
						textPhonenumber.setStyle("-fx-border-color: red");
					}
					buttonState();
				}
			}
		});
	}

	public void btnUpdate(ActionEvent event) {
		User.instance().setUserName(textUsername.getText());
		User.instance().setPassword(textPassword.getText());
		User.instance().setName(textName.getText());
		User.instance().setPhoneNumber(Integer.valueOf(textPhonenumber.getText()));
		ftController.updateUser(User.instance());
	}

	@Override
	public void update(Observable observable, Object state) {
		if (state == State.USER_UPDATED) {
			new AlertBox("Din profil blev redigeret", AlertType.INFORMATION);
		}

		if (state instanceof ErrorsList) {
			new AlertBox("Felter blev ikke korrekt udfyldt eller mangles", AlertType.WARNING);
		}

	}

	private void buttonState() {
		if (errorsList.hasErrors()) {
			btnUpdate.setDisable(true);
		} else {
			btnUpdate.setDisable(false);
		}
	}

}
