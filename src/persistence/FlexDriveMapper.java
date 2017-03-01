package persistence;

import java.time.LocalDate;
import java.util.List;

import domain.FlexDrive;
import exceptions.PersistenceFailureException;

public interface FlexDriveMapper {

	public void insert(DataAccess dataAccess, FlexDrive flexDrive) throws PersistenceFailureException;
	
	public int getLastInsertId();

	public List<FlexDrive> fetchFlexDrivesCsvInfoByDate(DataAccess dataAccess, int year, int month) throws PersistenceFailureException;

	public List<FlexDrive> fetchAllFlexDrives(DataAccess dataAccess) throws PersistenceFailureException;
	
	public List<FlexDrive> fetchIncurredFlexDrives(DataAccess dataAccess) throws PersistenceFailureException;
	
	public List<FlexDrive> fetchIncurredFlexDrivesByUser(DataAccess dataAccess, String user) throws PersistenceFailureException;
	
	public List<FlexDrive> fetchIncurredFlexDrivesByFromTo(DataAccess dataAccess, LocalDate from, LocalDate to) throws PersistenceFailureException;
	
	public List<FlexDrive> fetchIncurredFlexDrivesByFromToAndUser(DataAccess dataAccess, LocalDate from, LocalDate to, String user) throws PersistenceFailureException;

	public FlexDrive getById(DataAccess dataAccess, int id) throws PersistenceFailureException;
	
	public List<String> fetchExistingYears(DataAccess dataAccess) throws PersistenceFailureException;
	
	public List<String> fetchExistingMonths(DataAccess dataAccess, int year) throws PersistenceFailureException;
	
	public List<FlexDrive> fetchUserHistory(DataAccess dataAccess, int userId) throws PersistenceFailureException;
	
	public void approveFlexDrive(DataAccess dataAccess, int flexDriveId) throws PersistenceFailureException;
	
}
