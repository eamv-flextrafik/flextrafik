package persistence;

import domain.User;
import exceptions.PersistenceFailureException;

public interface UserMapper {

	public void insert(DataAccess dataAccess, User user) throws PersistenceFailureException;

	public void update(DataAccess dataAccess, User user) throws PersistenceFailureException;

	public User getById(DataAccess dataAccess, int userId) throws PersistenceFailureException;

	public User getByUsername(DataAccess dataAccess, String username) throws PersistenceFailureException;

	public boolean userExists(DataAccess dataAccess, String username, String password) throws PersistenceFailureException;

	public boolean userNameExist(DataAccess dataAccess, String userName) throws PersistenceFailureException;

	public int getIdByUsername(DataAccess dataAccess, String username) throws PersistenceFailureException;
	
	public boolean cprNumberExist(DataAccess dataAccess, String cprNumber) throws PersistenceFailureException;
	
}
