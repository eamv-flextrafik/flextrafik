package logic;

public class PriceHandlerFactory {

	public PriceHandler createPriceHandler() {
		return new PriceHandlerImpl();
	}

}
