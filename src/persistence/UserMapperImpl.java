package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.User;
import exceptions.PersistenceFailureException;
import util.CleanUp;

public class UserMapperImpl implements UserMapper {

	private final static String CREATE_USER = "INSERT INTO users"
			+ "(roleId, username, password, name, phonenumber, CPRnumber)"
			+ " VALUES (?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_USER = "UPDATE users SET password = ?, name = ?,"
			+ " phonenumber = ? WHERE id = ?";
	private final static String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
	private final static String GET_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
	private final static String LOGIN = "SELECT id FROM users where username = ? AND password = ?";
	private final static String USERNAME_EXIST = "SELECT * FROM users WHERE username = ?";
	private final static String GET_USER_ID_BY_USERNAME = "SELECT id FROM users WHERE username = ?";
	private final static String CPRNUMBER_EXIST = "SELECT * FROM users WHERE cprNumber = ?";

	@Override
	public void insert(DataAccess dataAccess, User user) throws PersistenceFailureException {
		PreparedStatement statement = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(CREATE_USER);
			statement.setInt(1, user.getRoleId());
			statement.setString(2, user.getUserName());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getName());
			statement.setInt(5, user.getPhoneNumber());
			statement.setString(6, user.getCprNumber());
			statement.execute();
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");

		} finally {
			CleanUp.cleanUpSql(statement);
		}

	}

	@Override
	public void update(DataAccess dataAccess, User user) throws PersistenceFailureException {
		PreparedStatement statement = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(UPDATE_USER);
			statement.setString(1, user.getPassword());
			statement.setString(2, user.getName());
			statement.setInt(3, user.getPhoneNumber());
			statement.setInt(4, user.getUserId());
			statement.execute();
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(statement);
		}
	}

	@Override
	public User getById(DataAccess dataAccess, int userId) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(GET_USER_BY_ID);
			statement.setInt(1, userId);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = User.instance();
				user.setUserId(resultSet.getInt("id"));
				user.setUserName(resultSet.getString("userName"));
			}
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
		return user;
	}

	@Override
	public User getByUsername(DataAccess dataAccess, String username) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(GET_USER_BY_USERNAME);
			statement.setString(1, username);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = User.instance();
				user.setUserId(resultSet.getInt("id"));
				user.setRoleId(resultSet.getInt("roleId"));
				user.setUserName(resultSet.getString("username"));
				user.setName(resultSet.getString("name"));
				user.setPhoneNumber(resultSet.getInt("phonenumber"));
				user.setPassword(resultSet.getString("password"));
				user.setCprNumber(resultSet.getString("cprnumber"));
			}
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
		return user;
	}

	@Override
	public boolean userExists(DataAccess dataAccess, String username, String password)
			throws PersistenceFailureException {
		PreparedStatement statement = null;
		dataAccess.getConnection();
		ResultSet resultSet = null;
		boolean exists = false;
		try {
			statement = dataAccess.getConnection().prepareStatement(LOGIN);
			statement.setString(1, username);
			statement.setString(2, password);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				exists = true;
			} else {
				exists = false;
			}
		} catch (SQLException e) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
		return exists;
	}

	@Override
	public boolean userNameExist(DataAccess dataAccess, String userName) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(USERNAME_EXIST);
			statement.setString(1, userName);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			return false;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

	@Override
	public int getIdByUsername(DataAccess dataAccess, String username) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int userId = 0;
		try {
			statement = dataAccess.getConnection().prepareStatement(GET_USER_ID_BY_USERNAME);
			statement.setString(1, username);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				userId = resultSet.getInt("id");
			}
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
		return userId;
	}

	public boolean cprNumberExist(DataAccess dataAccess, String cprNumber) throws PersistenceFailureException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dataAccess.getConnection().prepareStatement(CPRNUMBER_EXIST);
			statement.setString(1, cprNumber);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			return false;
		} catch (SQLException exc) {
			throw new PersistenceFailureException("Query has failed!");
		} finally {
			CleanUp.cleanUpSql(resultSet, statement);
		}
	}

}
