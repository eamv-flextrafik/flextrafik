package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Car;
import exceptions.PersistenceFailureException;
import util.CleanUp;

public class CarMapperImpl implements CarMapper {

	public final static String FETCH_CARS_AVAILABLE = "SELECT id, registryPlate FROM cars"
			+ " WHERE id NOT IN(SELECT carId FROM assignedCars)";
	public final static String ASSIGN_CAR = "INSERT INTO assignedcars (bookedFlexDrivesId, carId) VALUES (?,?)";

	@Override
	public List<Car> fetchCarsAvailable(DataAccess dataAccess) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Car> carsAvailable = new ArrayList<>();
		try {
			statement = dataAccess.getConnection().prepareStatement(FETCH_CARS_AVAILABLE);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Car car = new Car(resultSet.getInt("id"), resultSet.getString("registryPlate"));
				carsAvailable.add(car);
			}
			return carsAvailable;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public void assignCar(DataAccess dataAccess, int flexDriveId, int carId) throws PersistenceFailureException {
		PreparedStatement statement = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(ASSIGN_CAR);
			statement.setInt(1, flexDriveId);
			statement.setInt(2, carId);
			statement.execute();
		} catch (SQLException exc) {
			exc.printStackTrace();
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(statement);
		}
	}

}
