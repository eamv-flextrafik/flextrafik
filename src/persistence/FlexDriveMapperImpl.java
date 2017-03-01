package persistence;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domain.Car;
import domain.FlexDrive;
import domain.Price;
import exceptions.PersistenceFailureException;
import util.CleanUp;
import util.Month;

public class FlexDriveMapperImpl implements FlexDriveMapper {

	private final static String INSERT = "INSERT INTO flexDrives("
			+ " userId, addressFrom, addressTo, fromMunicipality, toMunicipality, price, bookedFor, bookedAt,"
			+ " passengers, luggage, pram, childCarSeat, assistive, comment, approved)"
			+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String FETCH_CSV_TRIPS_INFO_BY_DATE = "SELECT fd.*, u.username, u.name AS userFullName, c.id AS carId, c.registryplate FROM flexdrives fd"
			+ " INNER JOIN users u ON u.id = fd.userId"
			+ " LEFT JOIN assignedcars ac ON fd.id = ac.bookedFlexDrivesId"
			+ " LEFT JOIN cars c ON ac.carid = c.id"
			+ " WHERE year(bookedFor) = ?"
			+ " AND month(bookedFor) = ?"
			+ " ORDER BY fd.bookedFor ASC";
	private final static String FETCH_ALL_FLEX_DRIVES = "SELECT fd.*, u.username, c.id AS carId, c.registryplate FROM flexdrives fd"
			+ " INNER JOIN users u ON u.id = fd.userId"
			+ " LEFT JOIN assignedcars ac ON fd.id = ac.bookedFlexDrivesId"
			+ " LEFT JOIN cars c ON ac.carid = c.id"
			+ " ORDER BY fd.bookedFor ASC";
	private final static String FETCH_INCURRED_FLEX_DRIVES_BY_USER = "SELECT fd.*, u.username, c.id AS carId, c.registryplate FROM flexdrives fd"
			+ " INNER JOIN users u ON u.id = fd.userId"
			+ " LEFT JOIN assignedcars ac ON fd.id = ac.bookedFlexDrivesId"
			+ " LEFT JOIN cars c ON ac.carid = c.id"
			+ " WHERE u.username = ? AND fd.approved = true AND DATE(fd.bookedFor) < DATE(NOW())"
			+ " ORDER BY fd.bookedFor ASC";
	public final static String FETCH_INCURRED_FLEX_DRIVES = "SELECT fd.*, u.username, c.id AS carId, c.registryplate FROM flexdrives fd"
			+ " INNER JOIN users u ON u.id = fd.userId"
			+ " LEFT JOIN assignedcars ac ON fd.id = ac.bookedFlexDrivesId"
			+ " LEFT JOIN cars c ON ac.carid = c.id"
			+ " WHERE fd.approved = true AND DATE(fd.bookedFor) < DATE(NOW())"
			+ " ORDER BY fd.bookedFor ASC";
	private final static String FETCH_INCURRED_FLEX_DRIVES_BY_FROM_TO = "SELECT fd.*, u.username, c.id AS carId, c.registryplate FROM flexdrives fd"
			+ " INNER JOIN users u ON u.id = fd.userId"
			+ " LEFT JOIN assignedcars ac ON fd.id = ac.bookedFlexDrivesId"
			+ " LEFT JOIN cars c ON ac.carid = c.id"
			+ " WHERE date(bookedFor) BETWEEN ? AND ? AND fd.approved = true AND DATE(fd.bookedFor) < DATE(NOW())"
			+ " ORDER BY fd.bookedFor ASC";
	private final static String FETCH_INCURRED_FLEX_DRIVES_BY_FROM_TO_AND_USER = "SELECT fd.*, u.username, c.id AS carId, c.registryplate FROM flexdrives fd"
			+ " INNER JOIN users u ON u.id = fd.userId"
			+ " LEFT JOIN assignedcars ac ON fd.id = ac.bookedFlexDrivesId"
			+ " LEFT JOIN cars c ON ac.carid = c.id"
			+ " WHERE bookedFor BETWEEN ? AND ? AND u.username = ? AND fd.approved = true AND DATE(fd.bookedFor) < DATE(NOW())"
			+ " ORDER BY fd.bookedFor ASC";
	private final static String FETCH_FLEX_DRIVE_BY_ID = "SELECT * FROM flexdrives WHERE id = ?";
	private final static String FETCH_EXISTING_YEARS = "SELECT distinct(YEAR(bookedFor)) AS year FROM flexdrives";
	private final static String FETCH_EXISTING_MONTHS = "SELECT distinct (MONTH(bookedFor)) AS month FROM flexdrives WHERE YEAR(bookedFor) = ?";
	private final static String FETCH_USER_HISTORY = "SELECT fd.*, u.username, u.name AS userFullName FROM flexdrives fd"
			+ " INNER JOIN users u ON u.id = fd.userId" + " WHERE userID = ?" + " ORDER BY fd.bookedFor ASC";
	private final static String APPROVE_FLEX_DRIVE = "UPDATE flexdrives SET approved = ? WHERE id = ?";

	private int lastInsertId;

	@Override
	public void insert(DataAccess dataAccess, FlexDrive flexDrive) throws PersistenceFailureException {
		PreparedStatement statement = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, flexDrive.getUserId());
			statement.setString(2, flexDrive.getAddressFrom());
			statement.setString(3, flexDrive.getAddressTo());
			statement.setString(4, flexDrive.getFrom());
			statement.setString(5, flexDrive.getTo());
			statement.setBigDecimal(6, new BigDecimal(flexDrive.getPrice().getValue()));
			statement.setTimestamp(7, Timestamp.valueOf(flexDrive.getBookedFor()));
			statement.setTimestamp(8, new Timestamp(new Date().getTime()));
			statement.setInt(9, flexDrive.getPassengers());
			statement.setInt(10, flexDrive.getLuggage());
			statement.setInt(11, flexDrive.getPram());
			statement.setInt(12, flexDrive.getChildCarSeat());
			statement.setInt(13, flexDrive.getAssistive());
			statement.setString(14, flexDrive.getComment());
			statement.setBoolean(15, flexDrive.isApproved());
			statement.executeUpdate();
			
			ResultSet keys = statement.getGeneratedKeys();
			keys.next();
			
			lastInsertId = keys.getInt(1);
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(statement);
		}
	}

	@Override
	public int getLastInsertId() {
		return lastInsertId;
	}

	@Override
	public List<FlexDrive> fetchFlexDrivesCsvInfoByDate(DataAccess dataAccess, int year, int month)
			throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_CSV_TRIPS_INFO_BY_DATE);
			statement.setInt(1, year);
			statement.setInt(2, month);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				FlexDrive flexDrive = new FlexDrive();
				flexDrive.setUserId(resultSet.getInt("userId"));
				flexDrive.setUsername(resultSet.getString("username"));
				flexDrive.setUserFullName(resultSet.getString("userFullName"));
				flexDrive.setAddressFrom(resultSet.getString("addressFrom"));
				flexDrive.setAddressTo(resultSet.getString("addressTo"));
				flexDrive.setFrom(resultSet.getString("fromMunicipality"));
				flexDrive.setTo(resultSet.getString("toMunicipality"));
				flexDrive.setPrice(new Price(resultSet.getDouble("price")));
				flexDrive.setBookedFor(resultSet.getTimestamp("BookedFor").toLocalDateTime());
				flexDrive.setBookedAt(resultSet.getTimestamp("BookedAt").toLocalDateTime());
				flexDrive.setPassengers(resultSet.getInt("passengers"));
				flexDrive.setLuggage(resultSet.getInt("luggage"));
				flexDrive.setPram(resultSet.getInt("pram"));
				flexDrive.setChildCarSeat(resultSet.getInt("ChildCarSeat"));
				flexDrive.setAssistive(resultSet.getInt("assistive"));
				flexDrive.setComment(resultSet.getString("comment"));

				Car car = new Car();
				car.setId(resultSet.getInt("carId"));
				car.setRegistryPlate(resultSet.getString("registryplate"));

				flexDrive.setCar(car);
				flexDriveList.add(flexDrive);
			}
			return flexDriveList;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public List<FlexDrive> fetchAllFlexDrives(DataAccess dataAccess) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_ALL_FLEX_DRIVES);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				FlexDrive flexDrive = new FlexDrive();
				flexDrive.setId(resultSet.getInt("id"));
				flexDrive.setUserId(resultSet.getInt("userId"));
				flexDrive.setUsername(resultSet.getString("username"));
				flexDrive.setAddressFrom(resultSet.getString("addressFrom"));
				flexDrive.setAddressTo(resultSet.getString("addressTo"));
				flexDrive.setFrom(resultSet.getString("fromMunicipality"));
				flexDrive.setTo(resultSet.getString("toMunicipality"));
				flexDrive.setPrice(new Price(resultSet.getDouble("price")));
				flexDrive.setBookedFor(resultSet.getTimestamp("BookedFor").toLocalDateTime());
				flexDrive.setBookedAt(resultSet.getTimestamp("BookedAt").toLocalDateTime());
				flexDrive.setPassengers(resultSet.getInt("passengers"));
				flexDrive.setLuggage(resultSet.getInt("luggage"));
				flexDrive.setPram(resultSet.getInt("pram"));
				flexDrive.setChildCarSeat(resultSet.getInt("ChildCarSeat"));
				flexDrive.setAssistive(resultSet.getInt("assistive"));
				flexDrive.setComment(resultSet.getString("comment"));
				flexDrive.setApproved(resultSet.getBoolean("approved"));
				
				Car car = new Car();
				car.setId(resultSet.getInt("carId"));
				car.setRegistryPlate(resultSet.getString("registryplate"));

				flexDrive.setCar(car);
				flexDriveList.add(flexDrive);
			}
			return flexDriveList;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public List<FlexDrive> fetchIncurredFlexDrives(DataAccess dataAccess) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_INCURRED_FLEX_DRIVES);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				FlexDrive flexDrive = new FlexDrive();
				flexDrive.setUserId(resultSet.getInt("userId"));
				flexDrive.setAddressFrom(resultSet.getString("addressFrom"));
				flexDrive.setAddressTo(resultSet.getString("addressTo"));
				flexDrive.setFrom(resultSet.getString("fromMunicipality"));
				flexDrive.setTo(resultSet.getString("toMunicipality"));
				flexDrive.setPrice(new Price(resultSet.getDouble("price")));
				flexDrive.setBookedFor(resultSet.getTimestamp("BookedFor").toLocalDateTime());
				flexDrive.setBookedAt(resultSet.getTimestamp("BookedAt").toLocalDateTime());
				flexDrive.setPassengers(resultSet.getInt("passengers"));
				flexDrive.setLuggage(resultSet.getInt("luggage"));
				flexDrive.setPram(resultSet.getInt("pram"));
				flexDrive.setChildCarSeat(resultSet.getInt("ChildCarSeat"));
				flexDrive.setAssistive(resultSet.getInt("assistive"));
				flexDrive.setComment(resultSet.getString("comment"));
				flexDrive.setApproved(resultSet.getBoolean("approved"));
				
				Car car = new Car();
				car.setId(resultSet.getInt("carId"));
				car.setRegistryPlate(resultSet.getString("registryplate"));

				flexDrive.setCar(car);
				flexDriveList.add(flexDrive);
			}
			return flexDriveList;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}
	
	@Override
	public List<FlexDrive> fetchIncurredFlexDrivesByUser(DataAccess dataAccess, String user) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_INCURRED_FLEX_DRIVES_BY_USER);
			statement.setString(1, user);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				FlexDrive flexDrive = new FlexDrive();
				flexDrive.setUserId(resultSet.getInt("userId"));
				flexDrive.setAddressFrom(resultSet.getString("addressFrom"));
				flexDrive.setAddressTo(resultSet.getString("addressTo"));
				flexDrive.setFrom(resultSet.getString("fromMunicipality"));
				flexDrive.setTo(resultSet.getString("toMunicipality"));
				flexDrive.setPrice(new Price(resultSet.getDouble("price")));
				flexDrive.setBookedFor(resultSet.getTimestamp("BookedFor").toLocalDateTime());
				flexDrive.setBookedAt(resultSet.getTimestamp("BookedAt").toLocalDateTime());
				flexDrive.setPassengers(resultSet.getInt("passengers"));
				flexDrive.setLuggage(resultSet.getInt("luggage"));
				flexDrive.setPram(resultSet.getInt("pram"));
				flexDrive.setChildCarSeat(resultSet.getInt("ChildCarSeat"));
				flexDrive.setAssistive(resultSet.getInt("assistive"));
				flexDrive.setComment(resultSet.getString("comment"));
				flexDrive.setApproved(resultSet.getBoolean("approved"));
				
				Car car = new Car();
				car.setId(resultSet.getInt("carId"));
				car.setRegistryPlate(resultSet.getString("registryplate"));

				flexDrive.setCar(car);
				flexDriveList.add(flexDrive);
			}
			return flexDriveList;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public List<FlexDrive> fetchIncurredFlexDrivesByFromTo(DataAccess dataAccess, LocalDate from, LocalDate to)	throws PersistenceFailureException {
		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_INCURRED_FLEX_DRIVES_BY_FROM_TO);
			statement.setDate(1, java.sql.Date.valueOf(from));
			statement.setDate(2, java.sql.Date.valueOf(to));
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				FlexDrive flexDrive = new FlexDrive();
				flexDrive.setUserId(resultSet.getInt("userId"));
				flexDrive.setUsername(resultSet.getString("username"));
				flexDrive.setAddressFrom(resultSet.getString("addressFrom"));
				flexDrive.setAddressTo(resultSet.getString("addressTo"));
				flexDrive.setFrom(resultSet.getString("fromMunicipality"));
				flexDrive.setTo(resultSet.getString("toMunicipality"));
				flexDrive.setPrice(new Price(resultSet.getDouble("price")));
				flexDrive.setBookedFor(resultSet.getTimestamp("BookedFor").toLocalDateTime());
				flexDrive.setBookedAt(resultSet.getTimestamp("BookedAt").toLocalDateTime());
				flexDrive.setPassengers(resultSet.getInt("passengers"));
				flexDrive.setLuggage(resultSet.getInt("luggage"));
				flexDrive.setPram(resultSet.getInt("pram"));
				flexDrive.setChildCarSeat(resultSet.getInt("ChildCarSeat"));
				flexDrive.setAssistive(resultSet.getInt("assistive"));
				flexDrive.setComment(resultSet.getString("comment"));
				flexDrive.setApproved(resultSet.getBoolean("approved"));
				
				Car car = new Car();
				car.setId(resultSet.getInt("carId"));
				car.setRegistryPlate(resultSet.getString("registryplate"));

				flexDrive.setCar(car);
				flexDriveList.add(flexDrive);
			}
			return flexDriveList;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public List<FlexDrive> fetchIncurredFlexDrivesByFromToAndUser(DataAccess dataAccess, LocalDate from, LocalDate to, String user) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_INCURRED_FLEX_DRIVES_BY_FROM_TO_AND_USER);
			statement.setDate(1, java.sql.Date.valueOf(from));
			statement.setDate(2, java.sql.Date.valueOf(to));
			statement.setString(3, user);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				FlexDrive flexDrive = new FlexDrive();
				flexDrive.setUserId(resultSet.getInt("userId"));
				flexDrive.setAddressFrom(resultSet.getString("addressFrom"));
				flexDrive.setAddressTo(resultSet.getString("addressTo"));
				flexDrive.setFrom(resultSet.getString("fromMunicipality"));
				flexDrive.setTo(resultSet.getString("toMunicipality"));
				flexDrive.setPrice(new Price(resultSet.getDouble("price")));
				flexDrive.setBookedFor(resultSet.getTimestamp("BookedFor").toLocalDateTime());
				flexDrive.setBookedAt(resultSet.getTimestamp("BookedAt").toLocalDateTime());
				flexDrive.setPassengers(resultSet.getInt("passengers"));
				flexDrive.setLuggage(resultSet.getInt("luggage"));
				flexDrive.setPram(resultSet.getInt("pram"));
				flexDrive.setChildCarSeat(resultSet.getInt("ChildCarSeat"));
				flexDrive.setAssistive(resultSet.getInt("assistive"));
				flexDrive.setComment(resultSet.getString("comment"));
				flexDrive.setApproved(resultSet.getBoolean("approved"));
				
				Car car = new Car();
				car.setId(resultSet.getInt("carId"));
				car.setRegistryPlate(resultSet.getString("registryplate"));

				flexDrive.setCar(car);
				flexDriveList.add(flexDrive);
			}
			return flexDriveList;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public FlexDrive getById(DataAccess dataAccess, int id) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		FlexDrive flexDrive = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_FLEX_DRIVE_BY_ID);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				flexDrive = new FlexDrive();
				flexDrive.setId(resultSet.getInt("id"));
			}
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
		return flexDrive;
	}

	@Override
	public List<String> fetchExistingYears(DataAccess dataAccess) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> years = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_EXISTING_YEARS);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				years.add(String.valueOf(resultSet.getInt("year")));
			}
			return years;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public List<String> fetchExistingMonths(DataAccess dataAccess, int year) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> months = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_EXISTING_MONTHS);
			statement.setInt(1, year);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				months.add(Month.of(resultSet.getInt("month")));
			}
			return months;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public List<FlexDrive> fetchUserHistory(DataAccess dataAccess, int userId) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<FlexDrive> flexDriveList = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_USER_HISTORY);
			statement.setInt(1, userId);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				FlexDrive flexDrive = new FlexDrive();
				flexDrive.setFrom(resultSet.getString("fromMunicipality"));
				flexDrive.setTo(resultSet.getString("toMunicipality"));
				flexDrive.setPrice(new Price(resultSet.getDouble("price")));
				flexDrive.setBookedFor(resultSet.getTimestamp("BookedFor").toLocalDateTime());
				flexDrive.setApproved(resultSet.getBoolean("approved"));
				flexDriveList.add(flexDrive);
			}
			return flexDriveList;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public void approveFlexDrive(DataAccess dataAccess, int flexDriveId) throws PersistenceFailureException {
		PreparedStatement statement = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(APPROVE_FLEX_DRIVE);
			statement.setBoolean(1, true);
			statement.setInt(2, flexDriveId);
			statement.executeUpdate();
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(statement);
		}
	}

}
