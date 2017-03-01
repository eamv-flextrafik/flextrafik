package persistence;

import java.util.List;

import domain.Car;
import exceptions.PersistenceFailureException;

public interface CarMapper {

	public List<Car> fetchCarsAvailable(DataAccess dataAccess) throws PersistenceFailureException;

	public void assignCar(DataAccess dataAccess, int flexDriveId, int carId) throws PersistenceFailureException;

}
