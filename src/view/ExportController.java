package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import domain.FlexDrive;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.FTController;
import util.Month;
import view.util.FXFileChooser;

public class ExportController implements Initializable, FlexTrafikController{

	private FTController ftController;
	private String CSV_PATH = "";
	private List<String> year = new ArrayList<>();
	private List<String> month = new ArrayList<>();
	private List<FlexDrive> flexDrives = null;
	private int selectedYear;
	private int selectedMonth;

	@FXML
	private ComboBox<String> comboYear;
	@FXML
	private ComboBox<String> comboMonth;
	@FXML
	private TableView<FlexDrive> tableTrips;
	@FXML
	private TableColumn<FlexDrive, String> username;
	@FXML
	private TableColumn<FlexDrive, String> name;
	@FXML
	private TableColumn<FlexDrive, String> date;
	@FXML
	private TableColumn<FlexDrive, String> price;
	@FXML
	private Button btnExport;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@Override
	public void setLogicController(FTController ftController) {
		this.ftController = ftController;
	}

	@Override
	public void setStage(Stage stage) {
	}
	

	@Override
	public void init() {
		year = ftController.fetchExistingYears();
		comboYear.setItems(FXCollections.observableArrayList(year));

		username.setCellValueFactory(new PropertyValueFactory<>("username"));
		name.setCellValueFactory(new PropertyValueFactory<>("userFullName"));
		date.setCellValueFactory(new PropertyValueFactory<>("bookedFor"));
		price.setCellValueFactory(new PropertyValueFactory<>("price"));

		comboYear.getSelectionModel().selectedItemProperty().addListener(e -> {
			if (comboYear.getSelectionModel().getSelectedItem() != null) {
				month = ftController
						.fetchExistingMonths(Integer.parseInt(comboYear.getSelectionModel().getSelectedItem()));
				comboMonth.setItems(FXCollections.observableArrayList(month));
				if (comboMonth.getSelectionModel().getSelectedItem() == null) {
					btnExport.setDisable(true);
				}
			}
		});

		comboMonth.getSelectionModel().selectedItemProperty().addListener(e -> {
			if (comboYear.getSelectionModel().getSelectedItem() != null) {
				btnExport.setDisable(false);
				fetchFlexDrivesCsvInfoByDate();
			}
		});
	}

	public void btnExport(ActionEvent event) {
		try {
			FXFileChooser fxFileChooser = new FXFileChooser();
			CSV_PATH = fxFileChooser.setPath();
			export();
		} catch (Exception exc) {
			
		}
	}

	public void export() {
		selectedYear = Integer.parseInt(comboYear.getSelectionModel().getSelectedItem());
		selectedMonth = Month.toInt(comboMonth.getSelectionModel().getSelectedItem());
		if (comboYear.getSelectionModel().getSelectedItem() != null
				&& comboMonth.getSelectionModel().getSelectedItem() != null) {
			ftController.exportToCsv(selectedYear, selectedMonth, CSV_PATH);
		}
	}

	public void fetchFlexDrivesCsvInfoByDate() {
		selectedYear = Integer.parseInt(comboYear.getSelectionModel().getSelectedItem());
		selectedMonth = Month.toInt(comboMonth.getSelectionModel().getSelectedItem());
		flexDrives = ftController.fetchFlexDrivesCsvInfoByDate(selectedYear, selectedMonth);
		tableTrips.setItems((FXCollections.observableArrayList(flexDrives)));
	}

}
