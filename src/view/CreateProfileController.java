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

public class CreateProfileController implements Initializable, FlexTrafikController, Observer {

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
	private TextField textCPRnumber;
	@FXML
	private Button btnOpret;

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
		
		textUsername.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					if (validate.stringRequired(textUsername.getText())
							&& ftController.usernameExist(textUsername.getText()) == false) {
						errorsList.removeError(textUsername);
						textUsername.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textUsername);
						textUsername.setStyle("-fx-border-color: red");
					}
					buttonState();
				}
			}
		});

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

		textCPRnumber.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					if (!validate.lowerThan(textCPRnumber.getText().length(), 10)
							&& !validate.greaterThan(textCPRnumber.getText().length(), 10)
							&& ftController.cprNumberExist(textCPRnumber.getText()) == false) {
						errorsList.removeError(textCPRnumber);
						textCPRnumber.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textCPRnumber);
						textCPRnumber.setStyle("-fx-border-color: red");
					}
					buttonState();
				}
			}
		});
	}

	public void btnOpret(ActionEvent event) {
		if (!errorsList.hasErrors()) {
			User.instance().setRoleId(2);
			User.instance().setUserName(textUsername.getText());
			User.instance().setPassword(textPassword.getText());
			User.instance().setName(textName.getText());
			User.instance().setPhoneNumber(Integer.valueOf(textPhonenumber.getText()));
			User.instance().setCprNumber(textCPRnumber.getText());
			ftController.createUser(User.instance());
		}
	}

	private void buttonState() {
		if (errorsList.hasErrors()) {
			btnOpret.setDisable(true);
		} else {
			btnOpret.setDisable(false);
		}
	}

	@Override
	public void update(Observable observable, Object state) {
		if (state == State.USER_CREATED) {
			new AlertBox("Din profil blev oprettet", AlertType.INFORMATION);
			stage.close();
		}
		if (state instanceof ErrorsList) {
			new AlertBox("Felter blev ikke korrekt udfyldt eller mangles", AlertType.WARNING);
		}
	}

}
