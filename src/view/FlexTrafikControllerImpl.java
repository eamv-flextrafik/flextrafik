package view;

import logic.FTController;

public abstract class FlexTrafikControllerImpl implements FlexTrafikController {

	FTController ftController;
	
	@Override
	public void setLogicController(FTController ftController) {
		this.ftController = ftController;
	}

}
