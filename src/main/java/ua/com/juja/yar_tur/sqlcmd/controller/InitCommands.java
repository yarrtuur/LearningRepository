package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.controller.commands.*;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.util.LinkedList;
import java.util.List;

class InitCommands {
	private View view;
	private DBCommandManager dbManager;

	InitCommands(View view, DBCommandManager dbManager) {
		this.view = view;
		this.dbManager = dbManager;
	}

	List<CommandProcess> initCommandList() {
		List<CommandProcess> commands = new LinkedList<>();
		commands.add(new Exit(dbManager, view));
		commands.add(new Help(view));
		commands.add(new PostgreConnect(dbManager, view));
		commands.add(new TableCreate(dbManager, view));
		commands.add(new TableView(dbManager, view));
		commands.add(new DataInsert(dbManager, view));
		commands.add(new DataFind(dbManager, view));
		commands.add(new TableClean(dbManager, view));
		commands.add(new TableDrop(dbManager, view));
		commands.add(new DataDelete(dbManager, view));
		commands.add(new DataUpdate(dbManager, view));
		commands.add(new ChkConnection(dbManager, view));
		commands.add(new Unreachable(view));

		return commands;
	}
}
