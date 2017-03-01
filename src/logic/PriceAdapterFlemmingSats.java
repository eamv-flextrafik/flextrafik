package logic;

import java.time.LocalDateTime;

import domain.FlexDrive;
import sats.Sats;
import sats.UnknownKommuneException;

class PriceAdapterFlemmingSats implements PriceAdapter {

	@Override
	public double getPricePerKilometer(FlexDrive flexDrive) {
		LocalDateTime date = flexDrive.getBookedFor();
		double pricePerKilometer = 0;
		try {
			pricePerKilometer = Sats.i().getSats(flexDrive.getFrom(), flexDrive.getTo(), 
					date.getYear(), date.getMonthValue(), date.getDayOfMonth());
		} catch (UnknownKommuneException exc) {
		}
		return pricePerKilometer;
	}

}
