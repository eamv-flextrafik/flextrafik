package util;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CleanUp {

	public static void cleanUpSql(ResultSet resultSet, PreparedStatement statement) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException exc) {
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException exc) {
			}
		}
	}

	public static void cleanUpSql(PreparedStatement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException exc) {
			}
		}
	}

	public static void cleanUpWriter(FileWriter fileWriter) {
		if (fileWriter != null) {
			try {
				fileWriter.close();
			} catch (IOException exc) {
			}
		}
	}

}
