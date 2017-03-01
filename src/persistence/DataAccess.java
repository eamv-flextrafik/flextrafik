package persistence;

import java.sql.Connection;

import exceptions.PersistenceFailureException;

public interface DataAccess {

	public Connection getConnection();

	public void commit() throws PersistenceFailureException;

	public void rollback();

	public void close();

	public void startTransaction();

}