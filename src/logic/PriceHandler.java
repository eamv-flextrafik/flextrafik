package logic;

import domain.FlexDrive;
import domain.Price;

public interface PriceHandler {

	public void calculatePrice(FlexDrive flexDrive);
	public boolean isDone();
	public Price getPrice();
	
}
