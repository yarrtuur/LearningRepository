package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.PrepareResult;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class TableViewer implements CommandProcess, PrepareCmdLine {
	private DBCommandManager dbManager;
	private View view;
	private String tableName;
	private boolean isOne;

	public TableViewer(DBCommandManager dbManager, View view) {
		this.dbManager = dbManager;
		this.view = view;
	}

	@Override
	public boolean canProcess(String singleCommand) {
		return (singleCommand.startsWith("tables") && dbManager.getConnection().isConnected());
	}

	@Override
	public CmdLineState process(String[] commandLine) {//todo
		if (prepareCmdData(commandLine).equals(PrepareResult.PREPARE_RESULT_OK)) {
			try {
				dbManager.toView(tableName);
			} catch (SQLException ex) {
				view.write(ex.getMessage());
			}
		}
		return CmdLineState.WAIT;
	}

	@Override
	public PrepareResult prepareCmdData(String[] commandLine) {
		if (commandLine.length > 1) {
			isOne = true;
			this.tableName = commandLine[1];
		} else {
			isOne = false;
		}
		return PrepareResult.PREPARE_RESULT_OK;
	}

}
