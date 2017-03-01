package view;

import javafx.stage.Stage;
import logic.FTController;

public interface FlexTrafikController {
	
	public void setLogicController(FTController ftController);
	
	public void setStage(Stage stage);
	
	public void init();

}
