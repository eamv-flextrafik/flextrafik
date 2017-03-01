package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.FTController;
import view.util.CreateIcon;

public class WindowCreator {

	public void create(String fxmlFilePath, Stage stage, FTController ftController) {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFilePath));

		try {
			CreateIcon createIcon = new CreateIcon(stage);
			createIcon.iconCreater();
			AnchorPane mainWindow = (AnchorPane) loader.load();
			FlexTrafikController flexTrafikController = (FlexTrafikController) loader.getController();
			flexTrafikController.setLogicController(ftController);
			flexTrafikController.setStage(stage);
			flexTrafikController.init();
			stage.setTitle("Flextur");
			Scene scene = new Scene(mainWindow);
			stage.setScene(scene);
			stage.show();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}
