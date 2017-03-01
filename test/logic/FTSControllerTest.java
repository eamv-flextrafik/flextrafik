package logic;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import domain.Car;
import domain.FlexDrive;
import domain.Price;
import domain.User;

public class FTSControllerTest {

	@Test
	public void registrateFlexDrive() {
		FlexDrive flexDrive = new FlexDrive();
		Car car = new Car();
		car.setRegistryPlate("EZ29856");
		car.setId(3);
		flexDrive.setAddressFrom("123");
		flexDrive.setAddressTo("213");
		flexDrive.setApproved(true);
		flexDrive.setBookedAt(LocalDateTime.now());
		flexDrive.setBookedFor(LocalDateTime.now());
		flexDrive.setCar(car);
		flexDrive.setDistance(1);
		flexDrive.setFrom("Herning");
		flexDrive.setTo("Herning");
		flexDrive.setPrice(new Price(2));
		flexDrive.setUserId(1);

		FTController ftController = new FTControllerImpl();
		ftController.registerFlexDrive(flexDrive);
	}

	@Test
	public void bookFlexDrive() {
		FlexDrive flexDrive = new FlexDrive();
		flexDrive.setUserId(1);
		flexDrive.setFrom("Skjern");
		flexDrive.setTo("Herning");
		flexDrive.setAddressFrom("HernigVej");
		flexDrive.setAddressTo("SkjernVEj");
		flexDrive.setBookedAt(LocalDateTime.now());
		flexDrive.setBookedFor(LocalDateTime.now());
		flexDrive.setDistance(10);
		flexDrive.setLuggage(1);
		flexDrive.setPassengers(1);
		flexDrive.setPram(1);
		flexDrive.setComment("Test");
		flexDrive.setChildCarSeat(1);
		flexDrive.setAssistive(0);
		flexDrive.setPrice(new Price(10.2));

		FTController ftsController = new FTControllerImpl();
		ftsController.bookFlexDrive(flexDrive);

		FlexDrive newFlexDrive = ftsController.getById(7);

		assertEquals(7, newFlexDrive.getId());
	}

	@Test
	public void fetchFlexDrivesCsvInfoByDate() {
		List<FlexDrive> flexDriveList = new ArrayList<>();
		FTController ftsController = new FTControllerImpl();
		flexDriveList = ftsController.fetchFlexDrivesCsvInfoByDate(2016, 5);

		assertEquals(10, flexDriveList.size());
	}

	@Test
	public void fetchAllFlexDrives() {
		List<FlexDrive> flexDriveList = new ArrayList<>();
		FTController ftsController = new FTControllerImpl();
		flexDriveList = ftsController.fetchAllFlexDrives();

		assertEquals(10, flexDriveList.size());
	}

	@Test
	public void exportToCsv() {
		FTController ftsController = new FTControllerImpl();
		ftsController.exportToCsv(2016, 5, "C:\\test\\test.csv");
	}

	@Test
	public void getKommuner() {
		FTController ftsController = new FTControllerImpl();
		String[] kommuner;
		kommuner = ftsController.getKommuner();

		assertEquals(19, kommuner.length);
	}

	@Test
	public void createUser() {
		User.instance().setRoleId(2);
		User.instance().setUserName("Zeltres5");
		User.instance().setPassword("Awesome password");
		User.instance().setName("Dennis");
		User.instance().setPhoneNumber(24648053);
		User.instance().setCprNumber("2309933311");

		FTController ftsController = new FTControllerImpl();
		ftsController.createUser(User.instance());

		assertEquals(5, User.instance().getUserId());
	}

	@Test
	public void updateUser() {
		User.instance().setUserId(6);
		User.instance().setPassword("1234");
		User.instance().setName("AwesomeName");
		User.instance().setPhoneNumber(24648053);

		FTController ftsController = new FTControllerImpl();
		ftsController.updateUser(User.instance());

		assertEquals(6, User.instance().getUserId());
		assertEquals("AwesomeName", User.instance().getUserName());
	}

	@Test
	public void fetchExistingYears() {
		List<String> years = new ArrayList<>();
		FTController ftsController = new FTControllerImpl();
		years = ftsController.fetchExistingYears();

		assertEquals(1, years.size());
	}

	@Test
	public void fetchExistingMonths() {
		List<String> months = new ArrayList<>();
		FTController ftsController = new FTControllerImpl();
		months = ftsController.fetchExistingMonths(2016);

		assertEquals(1, months.size());
	}

	@Test
	public void usernameExist() {
		boolean exists = false;
		FTController ftsController = new FTControllerImpl();
		exists = ftsController.usernameExist("Martin1234");
		System.out.println(exists);

		assertEquals(true, exists);
	}

	@Test
	public void getIdByUsername() {
		int userId = 0;
		FTController ftsController = new FTControllerImpl();
		userId = ftsController.getUserIdByUsername("Martin1234");
		assertEquals(1, userId);
	}

}
