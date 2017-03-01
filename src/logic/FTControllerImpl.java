package logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import domain.Car;
import domain.FlexDrive;
import domain.Price;
import domain.User;
import persistence.CarMapper;
import persistence.CarMapperImpl;
import persistence.DataAccess;
import persistence.DataAccessImpl;
import persistence.FlexDriveMapper;
import persistence.FlexDriveMapperImpl;
import persistence.UserMapper;
import persistence.UserMapperImpl;
import sats.Sats;

public class FTControllerImpl implements FTController {

	private FlexDriveMapper flexDriveMapper;
	private UserMapper userMapper;
	private CarMapper carMapper;
	private FlexDrive currentFlexDrive;
	private PriceHandler priceHandler;

	public FTControllerImpl() {
		flexDriveMapper = new FlexDriveMapperImpl();
		userMapper = new UserMapperImpl();
		carMapper = new CarMapperImpl();
		priceHandler = new PriceHandlerFactory().createPriceHandler();
	}
	
	@Override
	public void bookFlexDrive(FlexDrive flexDrive) {
		FlexDriveValidation validation = new FlexDriveValidation();
		validation.validate(flexDrive);
		if (validation.validationFailed()) {
			notifyObservers(this, validation.getErrors());
		} else {
			DataAccess dataAccess = null;
			try {
				dataAccess = new DataAccessImpl();
				dataAccess.startTransaction();
				flexDriveMapper.insert(dataAccess, flexDrive);
				dataAccess.commit();
				notifyObservers(this, State.FLEXDRIVE_BOOKED);
			} catch (Exception exc) {
				dataAccess.rollback();
			} finally {
				dataAccess.close();
			}
		}
	}

	@Override
	public void registerFlexDrive(FlexDrive flexDrive) {
		FlexDriveValidation validation = new FlexDriveValidation();
		validation.validate(flexDrive);
		if (validation.validationFailed()) {
			notifyObservers(this, validation.getErrors());
		} else {
			DataAccess dataAccess = null;
			try {
				dataAccess = new DataAccessImpl();
				dataAccess.startTransaction();
				flexDriveMapper.insert(dataAccess, flexDrive);
				carMapper.assignCar(dataAccess, flexDriveMapper.getLastInsertId(), flexDrive.getCar().getId());
				dataAccess.commit();
				notifyObservers(this, State.FLEXDRIVE_REGISTERED);
			} catch (Exception exc) {
				dataAccess.rollback();
			} finally {
				dataAccess.close();
			}
		}
	}

	@Override
	public List<FlexDrive> fetchFlexDrivesCsvInfoByDate(int year, int month) {
		DataAccess dataAccess = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			flexDriveList = flexDriveMapper.fetchFlexDrivesCsvInfoByDate(dataAccess, year, month);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return flexDriveList;
	}

	@Override
	public List<FlexDrive> fetchAllFlexDrives() {
		DataAccess dataAccess = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			flexDriveList = flexDriveMapper.fetchAllFlexDrives(dataAccess);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return flexDriveList;
	}


	@Override
	public List<FlexDrive> fetchIncurredFlexDrives() {
		DataAccess dataAccess = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			flexDriveList = flexDriveMapper.fetchIncurredFlexDrives(dataAccess);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return flexDriveList;
	}
	
	@Override
	public List<FlexDrive> fetchIncurredFlexDrivesByUser(String user) {
		DataAccess dataAccess = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			flexDriveList = flexDriveMapper.fetchIncurredFlexDrivesByUser(dataAccess, user);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return flexDriveList;
	}

	@Override
	public List<FlexDrive> fetchIncurredFlexDrivesByFromTo(LocalDate from, LocalDate to) {
		DataAccess dataAccess = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			flexDriveList = flexDriveMapper.fetchIncurredFlexDrivesByFromTo(dataAccess, from, to);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return flexDriveList;
	}

	@Override
	public List<FlexDrive> fetchIncurredFlexDrivesByFromToAndUser(LocalDate from, LocalDate to, String user) {
		DataAccess dataAccess = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			flexDriveList = flexDriveMapper.fetchIncurredFlexDrivesByFromToAndUser(dataAccess, from, to, user);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return flexDriveList;
	}

	@Override
	public List<Car> fetchCarsAvailable() {
		DataAccess dataAccess = null;
		List<Car> carsAvailable = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			carsAvailable = carMapper.fetchCarsAvailable(dataAccess);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return carsAvailable;
	}

	@Override
	public void exportToCsv(int year, int month, String path) {
		ExportToCsv exportToCsv = new ExportToCsv();
		exportToCsv.exportToCsv(year, month, path);
	}

	@Override
	public String[] getKommuner() {
		return Sats.i().getKommuner();
	}

	@Override
	public FlexDrive getById(int id) {
		DataAccess dataAccess = null;
		FlexDrive flexDrive = null;
		try {
			dataAccess = new DataAccessImpl();
			flexDrive = flexDriveMapper.getById(dataAccess, id);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return flexDrive;
	}

	@Override
	public void createUser(User user) {
		DataAccess dataAccess = null;
		UserValidation userValidation = new UserValidation();
		userValidation.validate(user);
		if (userValidation.validationFailed()) {
			notifyObservers(this, userValidation.getErrors());
		} else {
			try {
				dataAccess = new DataAccessImpl();
				dataAccess.startTransaction();
				userMapper.insert(dataAccess, user);
				dataAccess.commit();
				notifyObservers(this, State.USER_CREATED);
			} catch (Exception exc) {
				dataAccess.rollback();
			} finally {
				dataAccess.close();
			}
		}
	}

	@Override
	public void updateUser(User user) {
		DataAccess dataAccess = null;
		UserValidation userValidation = new UserValidation();
		userValidation.validate(user);
		if (userValidation.validationFailed()) {
			notifyObservers(this, userValidation.getErrors());
		} else {
			try {
				dataAccess = new DataAccessImpl();
				dataAccess.startTransaction();
				userMapper.update(dataAccess, user);
				dataAccess.commit();
				notifyObservers(this, State.USER_UPDATED);
			} catch (Exception exc) {
				dataAccess.rollback();
			} finally {
				dataAccess.close();
			}
		}
	}

	@Override
	public List<String> fetchExistingYears() {
		DataAccess dataAccess = null;
		List<String> years = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			years = flexDriveMapper.fetchExistingYears(dataAccess);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return years;
	}

	@Override
	public List<String> fetchExistingMonths(int year) {
		DataAccess dataAccess = null;
		List<String> months = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			months = flexDriveMapper.fetchExistingMonths(dataAccess, year);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return months;
	}

	@Override
	public void login(String username, String password) {
		DataAccess dataAccess = null;
		try {
			dataAccess = new DataAccessImpl();
			if (userMapper.userExists(dataAccess, username, password)) {
				User user = userMapper.getByUsername(dataAccess, username);
				if (user.isAdmin()) {
					notifyObservers(this, State.BACKEND_USER);
				} else {
					notifyObservers(this, State.FRONTEND_USER);
				}
			} else
				notifyObservers(this, State.INVALID_USER);
		} catch (Exception exc) {
			notifyObservers(this, State.DATABASE_ERROR);
		} finally {
			if (dataAccess != null) {
				dataAccess.close();
			}
		}
	}

	@Override
	public int getUserIdByUsername(String username) {
		DataAccess dataAccess = null;
		int userId = 0;
		try {
			dataAccess = new DataAccessImpl();
			userId = userMapper.getIdByUsername(dataAccess, username);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return userId;
	}

	@Override
	public boolean usernameExist(String username) {
		DataAccess dataAccess = null;
		boolean exist = false;
		try {
			dataAccess = new DataAccessImpl();
			exist = userMapper.userNameExist(dataAccess, username);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return exist;
	}

	@Override
	public boolean cprNumberExist(String cprNumber) {
		DataAccess dataAccess = null;
		boolean exist = false;
		try {
			dataAccess = new DataAccessImpl();
			exist = userMapper.cprNumberExist(dataAccess, cprNumber);
		} catch (Exception exc) {
			dataAccess.rollback();
		} finally {
			dataAccess.close();
		}
		return exist;
	}

	@Override
	public List<FlexDrive> fetchUserHistory(int userId) {
		DataAccess dataAccess = null;
		List<FlexDrive> userHistory = new ArrayList<>();
		try {
			dataAccess = new DataAccessImpl();
			userHistory = flexDriveMapper.fetchUserHistory(dataAccess, userId);
		} catch (Exception exc) {

		} finally {
			dataAccess.close();
		}
		return userHistory;
	}

	@Override
	public void assignCar(int flexDriveId, int carId) {
		DataAccess dataAccess = null;
		try {
			dataAccess = new DataAccessImpl();
			dataAccess.startTransaction();
			carMapper.assignCar(dataAccess, flexDriveId, carId);
			dataAccess.commit();
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			dataAccess.close();
		}
	}

	@Override
	public FlexDrive getCurrentFlexDrive() {
		return currentFlexDrive;
	}

	@Override
	public void setCurrentFlexDrive(FlexDrive currentFlexDrive) {
		this.currentFlexDrive = currentFlexDrive;
	}

	@Override
	public void approveFlexDrive(int flexDriveId) {
		DataAccess dataAccess = null;
		try {
			dataAccess = new DataAccessImpl();
			dataAccess.startTransaction();
			flexDriveMapper.approveFlexDrive(dataAccess, flexDriveId);
			dataAccess.commit();
			notifyObservers(this, State.FLEXDRIVE_APPROVED);
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			dataAccess.close();
		}
	}

	@Override
	public void calculatePrice(FlexDrive flexDrive) {
		this.priceHandler.calculatePrice(flexDrive);
	}

	@Override
	public boolean isPriceCalculationDone() {
		return this.priceHandler.isDone();
	}

	@Override
	public Price getPrice() {
		return this.priceHandler.getPrice();
	}
}
