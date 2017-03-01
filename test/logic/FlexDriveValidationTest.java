package logic;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

public class FlexDriveValidationTest {

	@Test
	public void testLowestAllowedDate() {
		LocalDateTime lowestAllowedDate = LocalDateTime.now().plusSeconds(1);
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validateBookedFor(lowestAllowedDate));
	}

	@Test
	public void testHighestAllowedDate() {
		LocalDateTime highestAllowedDate = LocalDateTime.now().plusDays(14);
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validateBookedFor(highestAllowedDate));
	}

	@Test
	public void testDateInAllowedRange() {
		LocalDateTime dateInAllowedRange = LocalDateTime.now().plusDays(5);
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validateBookedFor(dateInAllowedRange));
	}

	@Test
	public void testPastDate() {
		LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(false, validation.validateBookedFor(pastDate));
	}

	@Test
	public void testInvalidFutureDate() {
		LocalDateTime invalidFutureDate = LocalDateTime.now().minusDays(15);
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(false, validation.validateBookedFor(invalidFutureDate));
	}

	@Test
	public void testLowestPassengersQty() {
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validatePassengers(1));
	}

	@Test
	public void testHighestPassengersQty() {
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validatePassengers(5));
	}

	@Test
	public void testValidPassengersQty() {
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validatePassengers(2));
		assertEquals(true, validation.validatePassengers(3));
		assertEquals(true, validation.validatePassengers(4));
	}

	@Test
	public void testMinChildCarSeatAndAssistiveQty() {
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validateChildCarSeatAndAssistiveQty(0, 0));
	}

	@Test
	public void testMaxChildCarSeatAndAssistiveQty() {
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validateChildCarSeatAndAssistiveQty(1, 1));
	}

	@Test
	public void testInvalidChildCarSeatAndAssistiveQty() {
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(false, validation.validateChildCarSeatAndAssistiveQty(2, 1));
		assertEquals(false, validation.validateChildCarSeatAndAssistiveQty(1, 2));
		assertEquals(false, validation.validateChildCarSeatAndAssistiveQty(2, 2));
	}

	@Test
	public void testInvalidAddress() {
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(false, validation.validateAddress("", null));
	}

	@Test
	public void testValidAddress() {
		FlexDriveValidation validation = new FlexDriveValidation();
		assertEquals(true, validation.validateAddress("a", null));
	}

	@Test
	public void testFailedValidation() {
		FlexDriveValidation validation = new FlexDriveValidation();
		validation.validateAddress("", null);
		assertEquals(true, validation.validationFailed());
	}

	@Test
	public void testPassedValidation() {
		FlexDriveValidation validation = new FlexDriveValidation();
		validation.validateAddress("asdasd", null);
		assertEquals(false, validation.validationFailed());
	}
	
}
