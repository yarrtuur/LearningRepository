package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.PrepareResult;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TableViewer implements CommandProcess, PrepareCmdLine {
	private DBCommandManager dbManager;
	private View view;
	private String tableName;
	private boolean isDetails;
	private boolean isOne;
	private ResultSet resultSet;

	public TableViewer(DBCommandManager dbManager, View view) {
		this.dbManager = dbManager;
		this.view = view;
	}

	@Override
	public boolean canProcess(String singleCommand) {
		return (singleCommand.equals("tables") && dbManager.getConnection().isConnected());
	}

	@Override
	public CmdLineState process(String[] commandLine) {
		if (prepareCmdData(commandLine).equals(PrepareResult.PREPARE_RESULT_OK)) {
			try {
				if (isDetails) {
					if (isOne) {
						resultSet = dbManager.toView(tableName);
						printOneTableDetails(resultSet);
					} else {
						resultSet = dbManager.toView();
						printSomeTablesDetails(resultSet);
					}
				} else {
					resultSet = dbManager.toView();
					printTablesList(resultSet);
				}

			} catch (SQLException ex) {
				view.write("Select data about table interrupted.");
				view.write(ex.getCause().toString());
			}
		}
		return CmdLineState.WAIT;
	}

	private void printSomeTablesDetails(ResultSet resultSet) throws SQLException {
		List<String> tblList = new LinkedList<>();
		while (resultSet.next()) {
			tblList.add(resultSet.getString("table_name"));
		}
		dbManager.closePrepareStatement();
		for (String step : tblList) {
			tableName = step;
			resultSet = dbManager.toView(tableName);
			printOneTableDetails(resultSet);
		}
	}

	private void printTablesList(ResultSet resultSet) throws SQLException {
		List<String> tblList = new LinkedList<>();
		while (resultSet.next()) {
			tblList.add(resultSet.getString("table_name"));
		}
		dbManager.closePrepareStatement();
		StringBuilder tableString = new StringBuilder();
		for (String step : tblList) {
			tableString.append(" ").append(step).append(",");
		}
		view.write(String.format("Tables: %s ",
				tableString.replace(tableString.length() - 1, tableString.length(), " ").toString()));
	}

	private void printOneTableDetails(ResultSet resultSet) throws SQLException {
		StringBuilder columnString = new StringBuilder();
		if (resultSet != null) {
			while (resultSet.next()) {
				columnString.append(" ").append(resultSet.getString("column_name")).append(":")
						.append(resultSet.getString("data_type")).append(",");
			}
		}
		dbManager.closePrepareStatement();
		String columnView = columnString.replace(columnString.length() - 1, columnString.length(), " ").toString();
		view.write(String.format("Table: %s , Columns: %s", tableName, columnView));
	}

	@Override
	public PrepareResult prepareCmdData(String[] commandLine) {
		if (commandLine.length > 1 && commandLine[1].equals("fields")) {
			isDetails = true;
			isOne = false;
		} else if (commandLine.length > 1 && !commandLine[1].equals("fields")) {
			isDetails = true;
			isOne = true;
			this.tableName = commandLine[1];
		} else if (commandLine.length == 1) {
			isDetails = false;
			isOne = false;
		} else {
			return PrepareResult.PREPARE_RESULT_OK;
		}

		return PrepareResult.PREPARE_RESULT_OK;
	}

}
