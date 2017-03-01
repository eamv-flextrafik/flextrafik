package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.PersistenceFailureException;

public class DataAccessImpl implements DataAccess {

	private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/fts";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";
	private Connection connection = null;

	public DataAccessImpl() {
		try {
			this.connection = DriverManager.getConnection(CONNECTION_URL, DB_USER, DB_PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException("Connection is not available.", e);
		}
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public void commit() throws PersistenceFailureException {
		try {
			this.connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException("Connection is not available.", e);
		}
	}

	@Override
	public void rollback() {
		try {
			this.connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Connection is not available.", e);
		}
	}

	@Override
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			throw new RuntimeException("Connection is not available.", e);
		}
	}

	@Override
	public void startTransaction() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// This should never happen
		}
	}

}
