package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class DataDelete implements CommandProcess, PrepareCmdLine, PrepareCommandData {
	private DBCommandManager dbManager;
	private View view;
	private DataContainer dataContainer;

	public DataDelete(DBCommandManager dbManager, View view) {
		this.dbManager = dbManager;
		this.view = view;
		dataContainer = new DataContainer();
	}

	@Override
	public boolean canProcess(String singleCommand) {
		return (singleCommand.startsWith("delete") && dbManager.getConnection().isConnected());
	}

	@Override
	public CmdLineState process(String[] commandLine) {
		try {
			prepareCmdData(commandLine);
            int deleteCount = dbManager.toDelete(dataContainer);
            view.write(String.format("Delete data operation successfull for %d rows", deleteCount));
		} catch (SQLException | ExitException ex) {
			view.write(ex.getMessage());
		}
		return CmdLineState.WAIT;
	}

	@Override
	public void prepareCmdData(String[] commandLine) throws ExitException {
		dataContainer.setTableName(getTableName(commandLine));
		dataContainer.setDataSetWhere(getFieldsParams(commandLine));
	}

}
