package view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import domain.FlexDrive;
import domain.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.FTController;
import logic.State;
import util.Observable;
import util.Observer;
import view.util.CloseEventHandler;
import view.WindowCreator;

public class UserController implements Initializable, FlexTrafikController, Observer {

	private FTController ftController;
	private Stage stage;
	private List<FlexDrive> userHistory;

	@FXML
	private Button btnBookFlexDrive;
	@FXML
	private Button btnChangeProfile;
	@FXML
	private TableView<FlexDrive> tableTrips;
	@FXML
	private TableColumn<FlexDrive, String> from;
	@FXML
	private TableColumn<FlexDrive, String> to;
	@FXML
	private TableColumn<FlexDrive, String> bookedFor;
	@FXML
	private TableColumn<FlexDrive, String> approved;
	@FXML
	private TableColumn<FlexDrive, String> price;
	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem menuItemExit;

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

		userHistory = ftController.fetchUserHistory(User.instance().getUserId());
		tableTrips.setItems((FXCollections.observableArrayList(userHistory)));
		from.setCellValueFactory(new PropertyValueFactory<>("from"));
		to.setCellValueFactory(new PropertyValueFactory<>("to"));

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
		price.setCellValueFactory(new PropertyValueFactory<>("Price"));
	}

	public void btnBookFlexDrive(ActionEvent event) {
		WindowCreator windowCreator = new WindowCreator();
		Stage stage = new Stage();
		windowCreator.create("/view/fxml/BookFlexDriveWindow.fxml", stage, ftController);
	}

	public void btnUpdateProfile(ActionEvent event) {
		WindowCreator windowCreator = new WindowCreator();
		Stage stage = new Stage();
		windowCreator.create("/view/fxml/UpdateProfileWindow.fxml", stage, ftController);
	}

	public void menuItemExit(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	@Override
	public void update(Observable observable, Object state) {
		if (state == State.FLEXDRIVE_BOOKED) {
			userHistory = ftController.fetchUserHistory(User.instance().getUserId());
			tableTrips.setItems((FXCollections.observableArrayList(userHistory)));
		}
	}

}
