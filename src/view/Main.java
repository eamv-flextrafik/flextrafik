package view;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.FTController;
import logic.FTControllerImpl;
import view.WindowCreator;

public class Main extends Application {

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Flextursystem");
		WindowCreator windowCreator = new WindowCreator();
		FTController ftsController = new FTControllerImpl();
		windowCreator.create("/view/fxml/LoginWindow.fxml", stage, ftsController);
	}

}
