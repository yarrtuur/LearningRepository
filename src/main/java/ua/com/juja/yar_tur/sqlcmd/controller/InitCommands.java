package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.controller.commands.*;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.util.LinkedList;
import java.util.List;

class InitCommands {
	View view;
	DBCommandManager dbManager;

	public InitCommands(View view, DBCommandManager dbManager) {
		this.view = view;
		this.dbManager = dbManager;
	}

	public List<CommandProcess> initCommandList() {
		List<CommandProcess> commands = new LinkedList<>();
		commands.add(new Exit(dbManager, view));
		commands.add(new Help(view));
		commands.add(new PostgreConnect(dbManager, view));
		commands.add(new TableCreater(dbManager, view));
		commands.add(new TableViewer(dbManager, view));
		commands.add(new DataInserter(dbManager, view));
		commands.add(new DataFinder(dbManager, view));
		commands.add(new TableCleaner(dbManager, view));
		commands.add(new TableDroper(dbManager, view));
		commands.add(new DataDeleter(dbManager, view));
		commands.add(new DataUpdater(dbManager, view));
		commands.add(new ChkConnection(dbManager, view));
		commands.add(new Unreachable(view));

		return commands;
	}
}
