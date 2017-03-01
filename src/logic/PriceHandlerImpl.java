package logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import domain.FlexDrive;
import domain.Price;

class PriceHandlerImpl implements PriceHandler {

	private FutureTask<Price> task;

	@Override
	public void calculatePrice(FlexDrive flexDrive) {
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		task = new FutureTask<Price>(() -> calculatePriceLambda(flexDrive));
		threadPool.submit(task);
		threadPool.shutdown();
	}

	private Price calculatePriceLambda(FlexDrive flexDrive) {
		PriceAdapter priceAdapter = new PriceAdapterFactory().createPriceAdapter();
		int numberOfPassengers, luggage, assistive = 0;
		double pricePerKilometer = 0, distance = 0, tripPrice, totalPrice;
		pricePerKilometer = priceAdapter.getPricePerKilometer(flexDrive);
		distance = flexDrive.getDistance();

		tripPrice = distance * pricePerKilometer;
		totalPrice = tripPrice;

		numberOfPassengers = flexDrive.getPassengers();
		if (numberOfPassengers > 1) {
			totalPrice += calculateHalf(tripPrice, numberOfPassengers);
		}

		luggage = flexDrive.getLuggage();
		if (luggage > 1) {
			totalPrice += calculateHalf(tripPrice, luggage);
		}

		assistive = flexDrive.getAssistive();
		if (assistive > 1) {
			totalPrice += calculateHalf(tripPrice, assistive);
		}
		return new Price(totalPrice);
	}

	private double calculateHalf(double price, int qty) {
		return (price * (qty - 1)) / 2;
	}

	public boolean isDone() {
		return task.isDone();
	}

	public Price getPrice() {
		Price price = null;
		try {
			price = task.get();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return price;
	}

}
