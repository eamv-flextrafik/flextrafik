package view.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CreateIcon {

	private String iconPath = System.getProperty("user.dir")
			+ File.separator
			+ "src"
			+ File.separator
			+ "flexdrive.png";
	private Stage window;

	public CreateIcon(Stage window) {
		this.window = window;
	}

	public void iconCreater() {
		Image icon = null;
		try {
			icon = new Image(new FileInputStream(new File(iconPath)));
		} catch (FileNotFoundException exc) {
			exc.printStackTrace();
		}
		window.getIcons().add(icon);
	}

}
