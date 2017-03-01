package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.FTController;
import logic.State;
import util.Observable;
import util.Observer;
import util.Validation;
import view.WindowCreator;

public class LoginController implements Initializable, FlexTrafikController, Observer {

	private FTController ftController;
	private Validation validation;
	private Stage loginStage;

	@FXML
	private TextField textUsername;
	@FXML
	private TextField textPassword;
	@FXML
	private Button btnCreateProfile;
	@FXML
	private Button btnLogin;
	@FXML
	private Label labelError;
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
		this.loginStage = stage;
	}

	@Override
	public void init() {
		validation = new Validation();
	}

	public void btnLogin(ActionEvent event) {
		if (validation.stringRequired(textUsername.getText()) && validation.stringRequired(textPassword.getText())) {
			ftController.login(textUsername.getText(), textPassword.getText());
		}
	}

	public void btnCreateProfile(ActionEvent event) {
		WindowCreator windowCreator = new WindowCreator();
		Stage stage = new Stage();
		windowCreator.create("/view/fxml/CreateProfileWindow.fxml", stage, ftController);
	}

	public void menuItemExit(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	@Override
	public void update(Observable observable, Object state) {
		if (state instanceof State) {
			WindowCreator windowCreator = new WindowCreator();
			Stage stage = new Stage();
			switch ((State) state) {
			case BACKEND_USER:
				windowCreator.create("/view/fxml/EmployeeWindow.fxml", stage, ftController);
				loginStage.close();
				break;
			case FRONTEND_USER:
				windowCreator.create("/view/fxml/UserWindow.fxml", stage, ftController);
				loginStage.close();
				break;
			case INVALID_USER:
				labelError.setText("Forkert brugernavn eller password");
				break;
			case DATABASE_ERROR:
				labelError.setText("MySQL database er ikke tilgængelig.");
				break;
			default:
				break;
			}
		}
	}

}
