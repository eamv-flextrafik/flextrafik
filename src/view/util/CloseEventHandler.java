package view.util;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CloseEventHandler {

	public static void closeStage(Stage stage, Runnable run) {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				run.run();
				stage.close();
			}
		});
	}

	public static void closeProgram(Stage stage) {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				Platform.exit();
				System.exit(0);
			}
		});
	}

}
