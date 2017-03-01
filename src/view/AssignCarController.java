package view;

import java.net.URL;
import java.util.ResourceBundle;

import domain.Car;
import domain.FlexDrive;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import logic.FTController;
import logic.State;
import util.Observable;
import util.Observer;
import view.util.AlertBox;
import view.util.CloseEventHandler;

public class AssignCarController implements Initializable, FlexTrafikController, Observer {

	private FTController ftController;
	private Stage stage;

	@FXML
	private ComboBox<Car> comboCar;
	@FXML
	private Button btnApproveTrip;

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
		CloseEventHandler.closeStage(stage, () -> ftController.unRegistrateObserver(this));
		comboCar.setItems(FXCollections.observableArrayList(ftController.fetchCarsAvailable()));
		FlexDrive flexDrive = ftController.getCurrentFlexDrive();
		btnApproveTrip.setOnAction(e -> ApproveTrip(flexDrive));
	}

	public void ApproveTrip(FlexDrive flexDrive) {
		ftController.assignCar(flexDrive.getId(), comboCar.getSelectionModel().getSelectedItem().getId());
		ftController.approveFlexDrive(flexDrive.getId());
	}

	@Override
	public void update(Observable observable, Object state) {
		if (state == State.FLEXDRIVE_APPROVED) {
			new AlertBox("Bestillingen er nu godkendt", AlertType.INFORMATION);
			stage.close();
		}
	}

}
