package util;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ValidationTest {

	private Validation validation;

	@Before
	public void setUp() {
		validation = new Validation();
	}

	@After
	public void tearDown() {
		validation = null;
	}

	@Test
	public void testRegex() {
		assertEquals(false, validation.regex("ase45", Regex.NUMERIC));
		assertEquals(true, validation.regex("45", Regex.NUMERIC));

		assertEquals(false, validation.regex("1230", Regex.TIME));
		assertEquals(true, validation.regex("12:30", Regex.TIME));
		assertEquals(false, validation.regex("24:59", Regex.TIME));
		assertEquals(false, validation.regex("01:60", Regex.TIME));

		assertEquals(false, validation.regex("6", Regex.ONE_NUMBER_FROM_ONE_TO_FIVE));
		assertEquals(true, validation.regex("4", Regex.ONE_NUMBER_FROM_ONE_TO_FIVE));
	}

	@Test
	public void testStringRequired() {
		assertEquals(true, validation.stringRequired("123"));
		assertEquals(false, validation.stringRequired(""));
	}

	@Test
	public void testGreaterThan() {
		assertEquals(true, validation.greaterThan("123".length(), 2));
		assertEquals(false, validation.greaterThan("123".length(), 4));
	}

	@Test
	public void testLowerThan() {
		assertEquals(true, validation.lowerThan("123".length(), 4));
		assertEquals(false, validation.greaterThan("123".length(), 3));
	}

	@Test
	public void testEqualsTo() {
		assertEquals(true, validation.equalsTo("123".length(), 3));
		assertEquals(false, validation.greaterThan("123".length(), 4));
	}
	
}
