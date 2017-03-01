package view;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import domain.Car;
import domain.FlexDrive;
import domain.Price;
import domain.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.FTController;
import logic.State;
import util.ErrorsList;
import util.Observable;
import util.Observer;
import util.Regex;
import util.Validation;
import view.util.AlertBox;
import view.util.CloseEventHandler;
import view.util.FXTooltip;

public class RegisterFlexDriveController implements Initializable, FlexTrafikController, Observer {

	private FTController ftController;
	private Stage stage;
	private FlexDrive flexDrive = new FlexDrive();
	private Validation validate = new Validation();
	private User user = User.instance();
	private LocalDateTime dateTime;
	private ErrorsList<Node> errorsList = new ErrorsList<>();
	private Callback<DatePicker, DateCell> dayCellFactory;
	private String selectedFromMunicipality;
	private String selectedToMunicipality;

	@FXML
	private TextField textUser;
	@FXML
	private ComboBox<String> comboFromMunicipality;
	@FXML
	private ComboBox<String> comboToMunicipality;
	@FXML
	private DatePicker dateTrip;
	@FXML
	private TextField textTimestamp;
	@FXML
	private TextField textAddressFrom;
	@FXML
	private TextField textAddressTo;
	@FXML
	private TextField textDistance;
	@FXML
	private TextField textPassengersQuantity;
	@FXML
	private TextField textLuggageQuantity;
	@FXML
	private TextField textPramQuantity;
	@FXML
	private CheckBox checkChildCarSeat1;
	@FXML
	private CheckBox checkChildCarSeat2;
	@FXML
	private CheckBox checkAssistive1;
	@FXML
	private CheckBox checkAssistive2;
	@FXML
	private ComboBox<Car> comboCar;
	@FXML
	private TextArea textComment;
	@FXML
	private Label labelPrice;
	@FXML
	private Button btnRegisterTrip;
	@FXML
	private Label assistivTooltip;
	@FXML
	private ProgressIndicator progressIndicator;

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

	private boolean validateNumericField(String input) {
		return validate.regex(input, Regex.NUMERIC_DOUBLE);
	}

	private boolean validateNumericOrEmptyField(String input) {
		return validate.regex(input, Regex.NUMERIC_OR_EMPTY);
	}

	private boolean validateTime(String input) {
		return validate.regex(input, Regex.TIME);
	}

	private boolean validateZeroToFive(String input) {
		return validate.regex(input, Regex.ONE_NUMBER_FROM_ONE_TO_FIVE);
	}

	public boolean validateMandatoryFields() {
		if (validateNumericField(textDistance.getText()) && validateTime(textTimestamp.getText())
				&& validateZeroToFive(textPassengersQuantity.getText())
				&& validateNumericOrEmptyField(textLuggageQuantity.getText()) && dateTrip.getValue() != null
				&& comboFromMunicipality.getSelectionModel().getSelectedItem() != null
				&& comboToMunicipality.getSelectionModel().getSelectedItem() != null
				&& validate.stringRequired(textAddressFrom.getText())
				&& validate.stringRequired(textAddressTo.getText())) {
			return true;
		}
		return false;
	}

	public boolean validateComboField() {
		if (comboCar.getSelectionModel().getSelectedItem() != null && labelPrice.getText().equalsIgnoreCase("pris")
				&& labelPrice.getText().length() == 0) {
			return false;
		}
		return true;
	}

	public void btnRegisterTrip(ActionEvent event) {
		if (!errorsList.hasErrors() && validateMandatoryFields()) {
			selectedFromMunicipality = comboFromMunicipality.getSelectionModel().getSelectedItem();
			selectedToMunicipality = comboToMunicipality.getSelectionModel().getSelectedItem();
			flexDrive.setUserId(ftController.getUserIdByUsername(textUser.getText()));
			flexDrive.setUsername(user.getUserName());
			flexDrive.setFrom(selectedFromMunicipality);
			flexDrive.setTo(selectedToMunicipality);
			createLocalDateTime();
			flexDrive.setBookedFor(dateTime);
			flexDrive.setBookedAt(LocalDateTime.now());
			flexDrive.setAddressFrom(textAddressFrom.getText());
			flexDrive.setAddressTo(textAddressTo.getText());
			flexDrive.setDistance(Double.parseDouble(textDistance.getText()));
			flexDrive.setPassengers(Integer.parseInt(textPassengersQuantity.getText()));
			if (!textLuggageQuantity.getText().isEmpty() && textLuggageQuantity.getText() != "") {
				flexDrive.setLuggage(Integer.parseInt(textLuggageQuantity.getText()));
			} else {
				flexDrive.setLuggage(0);
			}

			if (!textPramQuantity.getText().isEmpty() && textPramQuantity.getText() != "") {
				flexDrive.setLuggage(Integer.parseInt(textPramQuantity.getText()));
			} else {
				flexDrive.setPram(0);
			}
			if (!checkChildCarSeat1.isSelected() || !checkChildCarSeat2.isSelected())
				flexDrive.setChildCarSeat(0);
			if (checkChildCarSeat1.isSelected())
				flexDrive.setChildCarSeat(1);
			if (checkChildCarSeat2.isSelected())
				flexDrive.setChildCarSeat(2);
			if (!checkAssistive1.isSelected() || !checkAssistive2.isSelected())
				flexDrive.setAssistive(0);
			if (checkAssistive1.isSelected())
				flexDrive.setAssistive(1);
			if (checkAssistive2.isSelected())
				flexDrive.setAssistive(2);
			flexDrive.setComment(textComment.getText());
			flexDrive.setPrice(new Price(Double.parseDouble(labelPrice.getText())));
			flexDrive.setCar(comboCar.getSelectionModel().getSelectedItem());
			flexDrive.setApproved(true);
			ftController.registerFlexDrive(flexDrive);
		} 
	}

	public void updatePrice() {
		selectedFromMunicipality = comboFromMunicipality.getSelectionModel().getSelectedItem();
		selectedToMunicipality = comboToMunicipality.getSelectionModel().getSelectedItem();
		flexDrive.setFrom(selectedFromMunicipality);
		flexDrive.setTo(selectedToMunicipality);
		flexDrive.setBookedFor(createLocalDateTime());
		flexDrive.setDistance(Double.parseDouble(textDistance.getText()));
		flexDrive.setPassengers(Integer.parseInt(textPassengersQuantity.getText()));
		int luggage = textLuggageQuantity.getText().equalsIgnoreCase("") ? 0
				: Integer.parseInt(textLuggageQuantity.getText());
		flexDrive.setLuggage(luggage);
		if (checkAssistive1.isSelected())
			flexDrive.setAssistive(1);
		if (checkAssistive2.isSelected())
			flexDrive.setAssistive(2);
		
		JavaFXPriceTask priceTask = new JavaFXPriceTask(flexDrive, ftController);
		labelPrice.textProperty().bind(priceTask.messageProperty());
		priceTask.start();
	}

	public LocalDateTime createLocalDateTime() {
		LocalDate date = dateTrip.getValue();
		String[] timeArray = textTimestamp.getText().split(":");
		LocalTime time = null;
		try {
			time = LocalTime.of(Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]));
		} catch (NumberFormatException exc) {
		}
		return dateTime = LocalDateTime.of(date, time);
	}

	public void disableRegisterButton() {
		if (comboCar.getSelectionModel().getSelectedItem() == null) {
			btnRegisterTrip.setDisable(true);
		}
	}

	public void disableDatesBeforeToday() {
		dayCellFactory = e -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				if (item.isBefore(LocalDate.now())) {
					setStyle("-fx-background-color: #ffc0cb; -fx-text-fill: gray;");
					setDisable(true);
				}
				if (item.isAfter(LocalDate.now().plusDays(14))) {
					setStyle("-fx-background-color: #ffc0cb; -fx-text-fill: gray;");
					setDisable(true);
				}
			}
		};
	}

	@Override
	public void init() {
		CloseEventHandler.closeStage(stage, () -> ftController.unRegistrateObserver(this));

		comboFromMunicipality.setItems(FXCollections.observableArrayList(ftController.getKommuner()));
		comboToMunicipality.setItems(FXCollections.observableArrayList(ftController.getKommuner()));
		comboCar.setItems(FXCollections.observableArrayList(ftController.fetchCarsAvailable()));

		disableDatesBeforeToday();
		dateTrip.setEditable(false);

		dateTrip.setDayCellFactory(dayCellFactory);

		textComment.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
			}
		});

		textAddressFrom.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue) {
					if (validate.stringRequired(textAddressFrom.getText())) {
						errorsList.removeError(textAddressFrom);
						textAddressFrom.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textAddressFrom);
						textAddressFrom.setStyle("-fx-border-color: red");
					}
				}
				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		textAddressTo.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue) {
					if (validate.stringRequired(textAddressTo.getText())) {
						errorsList.removeError(textAddressTo);
						textAddressTo.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textAddressFrom);
						textAddressTo.setStyle("-fx-border-color: red");
					}
				}
				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		textUser.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue) {
					if (ftController.usernameExist(textUser.getText()) == true) {
						errorsList.removeError(textUser);
						textUser.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textUser);
						textUser.setStyle("-fx-border-color: red");
					}
				}
				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		comboCar.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue && validateMandatoryFields()) {
					if (validateComboField()) {
						btnRegisterTrip.setDisable(false);
					} else {
						btnRegisterTrip.setDisable(true);
					}
				}
			}
		});

		comboFromMunicipality.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		comboToMunicipality.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		dateTrip.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		textDistance.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue) {
					if (validateNumericField(textDistance.getText())) {
						errorsList.removeError(textDistance);
						textDistance.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textDistance);
						textDistance.setStyle("-fx-border-color: red");
					}
					if (!errorsList.hasErrors() && validateMandatoryFields()) {
						updatePrice();
					}
				}
			}
		});

		textTimestamp.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue) {
					if (validateTime(textTimestamp.getText())) {
						errorsList.removeError(textTimestamp);
						textTimestamp.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textTimestamp);
						textTimestamp.setStyle("-fx-border-color: red");
					}
					if (!errorsList.hasErrors() && validateMandatoryFields()) {
						updatePrice();
					}
				}
			}
		});

		textPassengersQuantity.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue) {
					if (validateZeroToFive(textPassengersQuantity.getText())) {
						errorsList.removeError(textPassengersQuantity);
						textPassengersQuantity.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textPassengersQuantity);
						textPassengersQuantity.setStyle("-fx-border-color: red");
					}
					if (!errorsList.hasErrors() && validateMandatoryFields()) {
						updatePrice();
					}
				}
			}
		});

		textLuggageQuantity.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue) {
					if (validateNumericOrEmptyField(textLuggageQuantity.getText())) {
						errorsList.removeError(textLuggageQuantity);
						textLuggageQuantity.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textLuggageQuantity);
						textLuggageQuantity.setStyle("-fx-border-color: red");
					}
					if (!errorsList.hasErrors() && validateMandatoryFields()) {
						updatePrice();
					}
				}
			}
		});

		textPramQuantity.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (oldValue) {
					if (validateNumericOrEmptyField(textPramQuantity.getText())) {
						errorsList.removeError(textPramQuantity);
						textPramQuantity.setStyle("-fx-border-color: gray");
					} else {
						errorsList.addError(textPramQuantity);
						textPramQuantity.setStyle("-fx-border-color: red");
					}
					if (!errorsList.hasErrors() && validateMandatoryFields()) {
						updatePrice();
					}
				}
			}
		});

		checkChildCarSeat1.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (newValue) {
					if (checkChildCarSeat1.isSelected() == true)
						checkChildCarSeat2.setSelected(false);
					checkChildCarSeat2.setDisable(true);
					checkAssistive2.setDisable(true);
					if (checkChildCarSeat1.isSelected() == true && checkAssistive1.isSelected() == true) {
						checkChildCarSeat2.setDisable(true);
						checkAssistive2.setDisable(true);
					}
				} else if (checkChildCarSeat1.isSelected() == false && checkAssistive1.isSelected() == false) {
					checkChildCarSeat2.setDisable(false);
					checkAssistive2.setDisable(false);
				}
				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		checkChildCarSeat2.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (newValue) {
					if (checkChildCarSeat2.isSelected() == true)
						checkChildCarSeat1.setSelected(false);
					checkChildCarSeat1.setDisable(true);
					checkAssistive1.setDisable(true);
					checkAssistive2.setDisable(true);
				} else {
					checkChildCarSeat1.setDisable(false);
					checkAssistive1.setDisable(false);
					checkAssistive2.setDisable(false);
				}
				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		checkAssistive1.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (newValue) {
					if (checkChildCarSeat1.isSelected() == true && checkAssistive1.isSelected() == true) {
						checkAssistive2.setSelected(false);
						checkChildCarSeat2.setDisable(true);
						checkAssistive2.setDisable(true);
					}
					if (checkAssistive1.isSelected() == true)
						checkChildCarSeat2.setSelected(false);
					checkChildCarSeat2.setDisable(true);
					checkAssistive2.setDisable(true);
				} else if (checkChildCarSeat1.isSelected() == false && checkAssistive1.isSelected() == false) {
					checkChildCarSeat2.setDisable(false);
					checkAssistive2.setDisable(false);
				}

				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		checkAssistive2.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				disableRegisterButton();
				if (newValue) {
					if (checkAssistive2.isSelected() == true)
						checkAssistive1.setSelected(false);
					checkChildCarSeat1.setDisable(true);
					checkChildCarSeat2.setDisable(true);
					checkAssistive1.setDisable(true);
				} else {
					checkAssistive1.setDisable(false);
					checkChildCarSeat1.setDisable(false);
					checkChildCarSeat2.setDisable(false);
				}

				if (!errorsList.hasErrors() && validateMandatoryFields()) {
					updatePrice();
				}
			}
		});

		btnRegisterTrip.setDisable(true);

		labelPrice.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equalsIgnoreCase("pris") && !newValue.isEmpty()) {
					progressIndicator.setVisible(false);
				} else {
					progressIndicator.setVisible(true);
					btnRegisterTrip.setDisable(true);
				}
			}
		});
	}

	public void assistivTooltip(MouseEvent event) {
		FXTooltip tooltip = new FXTooltip("Hjælpemidler kan fx være rollator eller kørestol");
		assistivTooltip.setTooltip(tooltip);
	}

	@Override
	public void update(Observable observable, Object state) {
		if (state == State.FLEXDRIVE_REGISTERED) {
			flexDrive = null;
			new AlertBox("Din registrering blev registreret korrekt", AlertType.INFORMATION);
			stage.close();
		}
		if (state instanceof ErrorsList) {
			new AlertBox("Felter blev ikke korrekt udfyldt eller mangles", AlertType.WARNING);
		}
	}

}
