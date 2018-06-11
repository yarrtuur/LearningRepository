package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionProperties;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.MakeDBConnectLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.io.FileNotFoundException;
import java.sql.SQLException;


public class PostgreConnect implements CommandProcess, MakeDBConnectLine {
	private DBCommandManager dbManager;
	private View view;
	private ConnectionProperties connectionProperties;
	private String login, passwd;

	public PostgreConnect(DBCommandManager dbManager, View view) {
		this.dbManager = dbManager;
		this.view = view;
	}

	@Override
	public boolean canProcess(String singleCommand) {
		return singleCommand.equals("connect");
	}

	@Override
	public CmdLineState process(String[] commandLine) {
		FeedBack resultCode;
		String connectLine;

		view.write("Starting connect...");
		view.write("-------- PostgreSQL JDBC Connection Testing ------------");
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			view.write("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			return CmdLineState.WAIT;
		}
		view.write("PostgreSQL JDBC Driver Registered!");

		if (commandLine.length == 1) {
			try {
				connectionProperties = new ConnectionProperties();
			} catch (FileNotFoundException e) {
				view.write(e.getMessage());
				return CmdLineState.WAIT;
			}
			connectLine = setSocketProperties();
		} else {
			view.write("Command line is not correct Please type 'help' command.");
			return CmdLineState.WAIT;
		}

		try {
			resultCode = dbManager.toConnect(connectLine, this.login, this.passwd);
		} catch (SQLException ex) {
			view.write("Connection Failed! Check output console");
			view.write(ex.getMessage());
			return CmdLineState.WAIT;
		}
		if (resultCode.equals(FeedBack.OK)) {
			view.write("You made it, take control your database now!");
		}

		return CmdLineState.WAIT;
	}

	@Override
	public String setSocketProperties() {
		this.login = connectionProperties.getConnDbLogin();
		this.passwd = connectionProperties.getConnDbPasswd();
		return String.format("%s/%s", connectionProperties.getConnDbSocket(), connectionProperties.getConnDbName());
	}

}
