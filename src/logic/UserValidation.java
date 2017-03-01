package logic;

import domain.User;
import util.ErrorsList;
import util.Validation;

public class UserValidation {

	private Validation validation;
	private ErrorsList<ValidationErrors> errors;

	public UserValidation() {
		validation = new Validation();
		errors = new ErrorsList<ValidationErrors>();
	}

	public void validate(User user) {
		if (!validation.stringRequired(user.getUserName())) {
			errors.addError(ValidationErrors.USERNAME_REQUIRED_ERROR);
		}
		if (!validation.stringRequired(user.getPassword())) {
			errors.addError(ValidationErrors.PASSWORD_REQUIRED_ERROR);
		}
		if (validation.lowerThan(user.getPhoneNumber(), 8)) {
			errors.addError(ValidationErrors.PHONE_NUMBER_LENGTH_ERROR);
		}
		if (validation.lowerThan(user.getCprNumber().length(), 10)
				|| validation.greaterThan(user.getCprNumber().length(), 10)) {
			errors.addError(ValidationErrors.CPR_NUMBER_LENGTH_ERROR);
		}
	}

	public boolean validationFailed() {
		return errors.hasErrors();
	}

	public ErrorsList<ValidationErrors> getErrors() {
		return errors;
	}

}
