package view.util;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXFileChooser {

	private Stage stage;

	public String setPath() {
		FileChooser fileChooserSave = new FileChooser();

		fileChooserSave.setTitle("Save File");
		fileChooserSave.setInitialDirectory(new File(System.getProperty("user.home")));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV", "*.csv");

		fileChooserSave.getExtensionFilters().add(extFilter);

		File file = fileChooserSave.showSaveDialog(stage);
		return file.getAbsolutePath();
	}

}