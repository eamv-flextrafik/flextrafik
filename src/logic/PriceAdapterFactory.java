package logic;

class PriceAdapterFactory {

	public PriceAdapter createPriceAdapter() {
		return new PriceAdapterFlemmingSats();
	}

}
