package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class TableCleaner implements CommandProcess, PrepareCmdLine {
	private DBCommandManager dbManager;
	private View view;
	private String tableName;

	public TableCleaner(DBCommandManager dbManager, View view) {
		this.dbManager = dbManager;
		this.view = view;
	}

	@Override
	public boolean canProcess(String singleCommand) {
		return (singleCommand.startsWith("clear") && dbManager.getConnection().isConnected());
	}

	@Override
	public CmdLineState process(String[] commandLine) {
		try {
			prepareCmdData(commandLine);
			dbManager.toClean(tableName);
		} catch (SQLException | ExitException ex) {
			view.write(ex.getMessage());
		}
		return CmdLineState.WAIT;
	}

	@Override
	public void prepareCmdData(String[] commandLine) throws ExitException {
		if (commandLine.length > 1) {
			tableName = commandLine[1];
		} else {
			throw new ExitException("There isn`t tablename at string. Try again.");
		}
	}
}
