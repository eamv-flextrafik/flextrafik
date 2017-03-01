package logic;

import java.time.LocalDateTime;

import domain.FlexDrive;
import util.ErrorsList;
import util.Validation;

public class FlexDriveValidation {

	private Validation validation;
	private ErrorsList<ValidationErrors> errors;

	public FlexDriveValidation() {
		validation = new Validation();
		errors = new ErrorsList<ValidationErrors>();
	}

	public boolean validateBookedFor(LocalDateTime dateTime) {
		if (dateTime.isBefore(LocalDateTime.now()) || dateTime.isAfter(LocalDateTime.now().plusDays(14))) {
			errors.addError(ValidationErrors.BOOKED_FOR_ERROR);
			return false;
		}
		return true;
	}

	public boolean validatePassengers(int passengers) {
		if (passengers > 5) {
			errors.addError(ValidationErrors.PASSENGERS_ERROR);
			return false;
		}
		return true;
	}

	public boolean validateAddress(String address, ValidationErrors error) {
		if (!validation.stringRequired(address)) {
			errors.addError(error);
			return false;
		}
		return true;
	}

	public boolean validateChildCarSeatAndAssistiveQty(int childCarSeatQty, int assistiveQty) {
		if ((assistiveQty + childCarSeatQty) > 2) {
			errors.addError(ValidationErrors.CHILD_CAR_SEAT_ASSISTIVE_QTY_ERROR);
			return false;
		}

		return true;
	}

	public void validate(FlexDrive flexDrive) {
		validateAddress(flexDrive.getAddressFrom(), ValidationErrors.ADDRESS_FROM_ERROR);
		validateAddress(flexDrive.getAddressTo(), ValidationErrors.ADDRESS_TO_ERROR);
		validateBookedFor(flexDrive.getBookedFor());
		validatePassengers(flexDrive.getPassengers());
		validateChildCarSeatAndAssistiveQty(flexDrive.getChildCarSeat(), flexDrive.getAssistive());
	}

	public boolean validationFailed() {
		return errors.hasErrors();
	}

	public ErrorsList<ValidationErrors> getErrors() {
		return errors;
	}
	
}
