package view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import domain.FlexDrive;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.FTController;
import logic.State;
import util.Observable;
import util.Observer;
import view.util.CloseEventHandler;
import view.WindowCreator;

public class EmployeeController implements Initializable, FlexTrafikController, Observer {

	private FTController ftController;
	private Stage stage;
	private List<FlexDrive> flexDrives = null;
	private Callback<DatePicker, DateCell> dayCellFactory;

	@FXML
	private Button btnShowOrderedTrips;
	@FXML
	private Button btnApproveTrip;
	@FXML
	private Button btnRegisterTrip;
	@FXML
	private Button btnExport;
	@FXML
	private Button btnSearch;
	@FXML
	private TextField textUser;
	@FXML
	private DatePicker dateFrom;
	@FXML
	private DatePicker dateTo;
	@FXML
	private TableView<FlexDrive> tableTrips;
	@FXML
	private TableColumn<FlexDrive, String> from;
	@FXML
	private TableColumn<FlexDrive, String> to;
	@FXML
	private TableColumn<FlexDrive, String> userId;
	@FXML
	private TableColumn<FlexDrive, String> approved;
	@FXML
	private TableColumn<FlexDrive, String> bookedFor;
	@FXML
	private TableColumn<FlexDrive, String> passengers;
	@FXML
	private TableColumn<FlexDrive, String> car;
	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem menuItemExit;
	@FXML
	private Button btnClear;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void setLogicController(FTController ftController) {
		this.ftController = ftController;
		this.ftController.registrateObserver(this);
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void init() {
		CloseEventHandler.closeProgram(stage);

		disableDatesAfterToday();
		dateFrom.setDayCellFactory(dayCellFactory);
		dateFrom.setEditable(false);
		dateTo.setDayCellFactory(dayCellFactory);
		dateTo.setEditable(false);

		tableTrips.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				FlexDrive flexDrive = tableTrips.getSelectionModel().getSelectedItem();
				if (newValue != null && flexDrive.isApproved() == false) {
					btnApproveTrip.setDisable(false);
				} else {
					btnApproveTrip.setDisable(true);
				}
			}
		});

		from.setCellValueFactory(new PropertyValueFactory<>("from"));
		to.setCellValueFactory(new PropertyValueFactory<>("to"));
		userId.setCellValueFactory(new PropertyValueFactory<>("userId"));

		Callback<TableColumn<FlexDrive, String>, TableCell<FlexDrive, String>> cellFactory = new Callback<TableColumn<FlexDrive, String>, TableCell<FlexDrive, String>>() {
			@Override
			public TableCell<FlexDrive, String> call(TableColumn<FlexDrive, String> column) {
				final TableCell<FlexDrive, String> cell = new TableCell<FlexDrive, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							FlexDrive flexDrive = (FlexDrive) this.getTableRow().getItem();
							if (flexDrive != null) {
								if (flexDrive.isApproved()) {
									setGraphic(new Label("Godkendt"));
								} else {
									setGraphic(new Label("Ikke godkendt"));
								}
							}
						}
					}
				};
				return cell;
			}
		};

		approved.setCellFactory(cellFactory);
		bookedFor.setCellValueFactory(new PropertyValueFactory<>("bookedFor"));
		passengers.setCellValueFactory(new PropertyValueFactory<>("passengers"));
		car.setCellValueFactory(new PropertyValueFactory<>("car"));

		dateFrom.showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					if (dateFrom.getValue() == null || dateTo.getValue() == null) {
						btnSearch.setDisable(true);
					}
					if (dateFrom.getValue() == null && dateTo.getValue() == null) {
						btnSearch.setDisable(false);
					}
					if (dateFrom.getValue() != null && dateTo.getValue() != null) {
						btnSearch.setDisable(false);
					}
				}
			}
		});

		dateTo.showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					if (dateTo.getValue() == null || dateFrom.getValue() == null) {
						btnSearch.setDisable(true);
					}
					if (dateTo.getValue() == null && dateFrom.getValue() == null) {
						btnSearch.setDisable(false);
					}
					if (dateTo.getValue() != null && dateFrom.getValue() != null) {
						btnSearch.setDisable(false);
					}
				}
			}
		});
	}

	public void disableDatesAfterToday() {
		dayCellFactory = e -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				if (item.isAfter(LocalDate.now())) {
					setStyle("-fx-background-color: #ffc0cb; -fx-text-fill: gray;");
					setDisable(true);
				}
			}
		};
	}

	public void btnShowOrderedTrips(ActionEvent event) {
		flexDrives = new ArrayList<>();
		flexDrives = ftController.fetchAllFlexDrives();
		tableTrips.setItems((FXCollections.observableArrayList(flexDrives)));
	}

	public void btnApproveTrip(ActionEvent event) {
		FlexDrive selectedFlexDrive = tableTrips.getSelectionModel().getSelectedItem();
		ftController.setCurrentFlexDrive(selectedFlexDrive);
		WindowCreator windowCreator = new WindowCreator();
		Stage stage = new Stage();
		windowCreator.create("/view/fxml/AssignCarWindow.fxml", stage, ftController);
	}

	public void btnExport(ActionEvent event) {
		WindowCreator windowCreator = new WindowCreator();
		Stage stage = new Stage();
		windowCreator.create("/view/fxml/ExportWindow.fxml", stage, ftController);
	}

	public void btnRegisterTrip(ActionEvent event) {
		WindowCreator windowCreator = new WindowCreator();
		Stage stage = new Stage();
		windowCreator.create("/view/fxml/RegisterFlexDriveWindow.fxml", stage, ftController);
	}

	public void btnSearch(ActionEvent event) {
		if (textUser.getText().isEmpty() && dateFrom.getValue() == null && dateTo.getValue() == null) {
			flexDrives = ftController.fetchIncurredFlexDrives();
		}

		if (!textUser.getText().isEmpty() && dateFrom.getValue() == null && dateTo.getValue() == null) {
			flexDrives = ftController.fetchIncurredFlexDrivesByUser(textUser.getText());
		}

		if (textUser.getText().isEmpty() && dateFrom.getValue() != null && dateTo.getValue() != null) {
			flexDrives = ftController.fetchIncurredFlexDrivesByFromTo(dateFrom.getValue(), dateTo.getValue());
		}

		if (!textUser.getText().isEmpty() && dateFrom.getValue() != null && dateTo.getValue() != null) {
			flexDrives = ftController.fetchIncurredFlexDrivesByFromToAndUser(dateFrom.getValue(), dateTo.getValue(),
					textUser.getText());
		}
		tableTrips.setItems((FXCollections.observableArrayList(flexDrives)));
	}

	public void menuItemExit(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	public void btnClear(ActionEvent event) {
		textUser.setText("");
		dateFrom.setValue(null);
		dateTo.setValue(null);
		btnSearch.setDisable(false);
	}

	@Override
	public void update(Observable observable, Object state) {
		if (state == State.FLEXDRIVE_APPROVED) {
			flexDrives = ftController.fetchAllFlexDrives();
			tableTrips.setItems((FXCollections.observableArrayList(flexDrives)));
		}
	}

}
