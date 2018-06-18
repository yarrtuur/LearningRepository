package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionProperties;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.MakeDBConnectLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;


public class PostgreConnect implements CommandProcess, MakeDBConnectLine {
	private DBCommandManager dbManager;
	private View view;
	private String login, passwd;

	public PostgreConnect(DBCommandManager dbManager, View view) {
		this.dbManager = dbManager;
		this.view = view;
	}

	@Override
	public boolean canProcess(String singleCommand) {
		return singleCommand.startsWith("connect");
	}

	@Override
	public CmdLineState process(String[] commandLine) {
		FeedBack resultCode;
		String connectLine;

		if (!registerJDBCDriver()) return CmdLineState.WRONG;
		connectLine = setSocketProperties();
		try {
			resultCode = dbManager.toConnect(connectLine, this.login, this.passwd);
		} catch (SQLException ex) {
			view.write(ex.getMessage());
			return CmdLineState.WRONG;
		}
		if (resultCode.equals(FeedBack.OK)) {
			view.write("You made it, take control your database now!");
		}else{
			view.write("There is no connect!");
		}

		return CmdLineState.WAIT;
	}

	private boolean registerJDBCDriver() {
		view.write("-------- PostgreSQL JDBC Connection Testing ------------");
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			view.write("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			return true;
		}
		view.write("PostgreSQL JDBC Driver Registered!");
		return false;
	}

	@Override
	public String setSocketProperties() {
		ConnectionProperties connectionProperties = new ConnectionProperties();;
		this.login = connectionProperties.getConnDbLogin();
		this.passwd = connectionProperties.getConnDbPasswd();
		return String.format("%s/%s", connectionProperties.getConnDbSocket(), connectionProperties.getConnDbName());
	}

}
