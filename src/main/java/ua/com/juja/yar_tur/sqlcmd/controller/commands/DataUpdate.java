package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainerUpdate;
import ua.com.juja.yar_tur.sqlcmd.utils.DataSet;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataUpdate implements CommandProcess, PrepareCmdLine, PrepareCommandData {
	private DBCommandManager dbManager;
	private View view;
	private DataSet dataSetSet;
	private DataSet dataSetWhere;
	private DataContainerUpdate dataContainerUpdate;

	public DataUpdate(DBCommandManager dbManager, View view) {
		this.dbManager = dbManager;
		this.view = view;
		dataContainerUpdate = new DataContainerUpdate();
	}

	@Override
	public boolean canProcess(String singleCommand) {
		return (singleCommand.startsWith("update") && dbManager.getConnection().isConnected());
	}

	@Override
	public CmdLineState process(String[] commandLine) {
		try {
			prepareCmdData(commandLine);
			dbManager.toUpdate(dataContainerUpdate);
			view.write("Update data successfull");
		} catch (SQLException | ExitException ex) {
			view.write(ex.getMessage());
		}
		return CmdLineState.WAIT;
	}

	@Override
	public void prepareCmdData(String[] commandLine) throws ExitException {
		dataContainerUpdate.setTableName(getTableName(commandLine));
		getFieldsUpdateParams(commandLine);
		dataContainerUpdate.setDataSetSet(dataSetSet);
		dataContainerUpdate.setDataSetWhere(dataSetWhere);
	}


	private void getFieldsUpdateParams(String[] commandLine) throws ExitException {
		if (commandLine.length % 2 != 0 && commandLine.length > 2) {
			throw new ExitException("String format is wrong. Must be even count of data. Try again.");
		} else {
			fillDataSets(commandLine);
		}
	}

	private void fillDataSets(String[] commandLine) throws ExitException {
		List<String> cmdList = new ArrayList<>(Arrays.asList(commandLine));
		int set = cmdList.indexOf("set");
		int where = cmdList.indexOf("where");

		String[] setCommandLine = Arrays.copyOfRange(commandLine, set + 1, where);
		dataSetSet = getFieldsParams(setCommandLine);

		String[] whereCommandLine = Arrays.copyOfRange(commandLine, where + 1, commandLine.length);
		dataSetWhere = getFieldsParams(whereCommandLine);
	}

	@Override
	public DataSet getFieldsParams(String[] commandLine) throws ExitException {
		DataSet dataSet;
		if (commandLine.length % 2 != 0 && commandLine.length >= 2) {
			throw new ExitException("String format is wrong. Try again.");
		} else {
			dataSet = new DataSet();
			for (int i = 0; i < commandLine.length; i += 2) {
				dataSet.add(commandLine[i], commandLine[i + 1]);
			}
		}
		return dataSet;
	}
}
