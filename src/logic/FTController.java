package logic;

import java.time.LocalDate;
import java.util.List;

import domain.Car;
import domain.FlexDrive;
import domain.Price;
import domain.User;
import util.Observable;

public interface FTController extends Observable {

	/**
	 * Insert FlexDrive to the database
	 * 
	 * @param flexDrive
	 */
	public void bookFlexDrive(FlexDrive flexDrive);

	/**
	 * Insert FlexDrive to the database
	 * 
	 * @param flexDrive
	 */
	public void registerFlexDrive(FlexDrive flexDrive);

	/**
	 * assign Car to the Id of FlexDrive
	 * assign the car
	 * 
	 * @param flexDriveId
	 * @param carId
	 */
	public void assignCar(int flexDriveId, int carId);

	/**
	 * Returning a list of FlexDrives by year and month
	 * 
	 * @param year
	 * @param month
	 * @return List of FlexDrives
	 */
	public List<FlexDrive> fetchFlexDrivesCsvInfoByDate(int year, int month);

	/**
	 * Returning all FlexDrives
	 * 
	 * @return List of FlexDrives
	 */
	public List<FlexDrive> fetchAllFlexDrives();

	/**
	 * Returning a list of incurred FlexDrives
	 * 
	 * @return List of FlexDrives
	 */
	public List<FlexDrive> fetchIncurredFlexDrives();

	/**
	 * Returning a list of incurred FlexDrives by User
	 * 
	 * @param user
	 * @return List of FlexDrives
	 */
	public List<FlexDrive> fetchIncurredFlexDrivesByUser(String user);

	/**
	 * Returning a list of incurred FlexDrives by Date
	 * 
	 * @param from
	 * @param to
	 * @return List of FlexDrives
	 */
	public List<FlexDrive> fetchIncurredFlexDrivesByFromTo(LocalDate from, LocalDate to);

	/**
	 * Returning a list of incurred FlexDrives by User and Date
	 * 
	 * @param from
	 * @param to
	 * @param user
	 * @return List of FlexDrives
	 */
	public List<FlexDrive> fetchIncurredFlexDrivesByFromToAndUser(LocalDate from, LocalDate to, String user);

	/**
	 * Returning a list of cars available
	 * 
	 * @return List of Car
	 */
	public List<Car> fetchCarsAvailable();

	/**
	 * Export to CSV by year and month
	 * 
	 * @param year
	 * @param month
	 * @param path
	 */
	public void exportToCsv(int year, int month, String path);

	/**
	 * Returning a String array of kommuner
	 * 
	 * @return String array of Kommuner
	 */
	public String[] getKommuner();

	/**
	 * ReturningFlexDrive object by the id
	 * 
	 * @param id
	 * @return FlexDrive
	 */
	public FlexDrive getById(int id);

	/**
	 * Insert User to the database
	 * 
	 * @param user
	 */
	public void createUser(User user);

	/**
	 * Changed User in the database
	 * 
	 * @param user
	 */
	public void updateUser(User user);

	/**
	 * Returning a list of strings with existing years
	 * 
	 * @return List of String
	 */
	public List<String> fetchExistingYears();

	/**
	 * Returning a list of strings with existing month by year
	 * 
	 * @param year
	 * @return List of String
	 */
	public List<String> fetchExistingMonths(int year);

	/**
	 * Checking if the username and password exist
	 * 
	 * @param username
	 * @param password
	 */
	public void login(String username, String password);

	/**
	 * Returning a int id from by username
	 * 
	 * @param username
	 * @return
	 */
	public int getUserIdByUsername(String username);

	/**
	 * Returning true or false by username
	 * 
	 * @param userName
	 * @return boolean
	 */
	public boolean usernameExist(String userName);

	/**
	 * Returning true or false by the cprNumber
	 * 
	 * @param userName
	 * @return boolean
	 */
	public boolean cprNumberExist(String string);

	/**
	 * Returning a list of FlexDrive by UserId
	 * 
	 * @param userId
	 * @return List of FlexDrives
	 */
	public List<FlexDrive> fetchUserHistory(int userId);

	/**
	 * Returning a FlexDrive by userId
	 * 
	 * @return FlexDrive
	 */
	public FlexDrive getCurrentFlexDrive();

	/**
	 * Sets the current FlexDrive 
	 * 
	 * @param currentFlexDrive
	 */
	public void setCurrentFlexDrive(FlexDrive currentFlexDrive);

	/**
	 * Insert approved FlexDrive by currentFlexDrive
	 * 
	 * @param flexDriveId
	 */
	public void approveFlexDrive(int flexDriveId);
	
	/**
	 * Calculate price by FlexDrive
	 * 
	 * @param flexDrive
	 */
	public void calculatePrice(FlexDrive flexDrive);
	
	/**
	 * Return true or false depends if the Price is calculated
	 * 
	 * @return
	 */
	public boolean isPriceCalculationDone();
	
	/**
	 * Return Price
	 * 
	 * @return
	 */
	public Price getPrice();
	
}
