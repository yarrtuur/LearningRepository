package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class DataDeleter implements CommandProcess, PrepareCmdLine, PrepareCommandData {
	private DBCommandManager dbManager;
	private View view;
	private DataContainer dataContainer;


	public DataDeleter(DBCommandManager dbManager, View view) {
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
			dbManager.toDelete(dataContainer);
			view.write("Delete data operation successfull");
		} catch (SQLException | ExitException ex) {
			view.write(ex.getMessage());
		}
		return CmdLineState.WAIT;
	}

	@Override
	public void prepareCmdData(String[] commandLine) throws ExitException {
		dataContainer.setTableName(getTableName(commandLine));
		dataContainer.setDataSet(getFieldsParams(commandLine));
	}

}
