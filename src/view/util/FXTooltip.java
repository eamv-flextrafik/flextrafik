package view.util;

import javafx.scene.control.Tooltip;

public class FXTooltip extends Tooltip {

	public FXTooltip(String text) {
		super();
		this.setStyle("-fx-background-color: #f9f9f9; -fx-text-fill: #000");
		this.setText(text);
	}

}
