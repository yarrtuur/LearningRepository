package ua.com.juja.yar_tur.sqlcmd.viewer;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Printable {

	void printTablesList(ResultSet resultSet) throws SQLException;

	void printOneTableDetails(ResultSet resultSet, String tableName) throws SQLException;

	void printFoundData(ResultSet resultSet) throws SQLException;
}
