package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.util.List;

public class MainController {
    private View view;
    private DBCommandManager dbManager;
    private List<CommandProcess> commands;
    private CmdLineState cmdState;

    public MainController(DBCommandManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
        this.commands = new InitCommands(view, dbManager).initCommandList();
    }

    public void mainDialogueHolder() {//todo
		String incomStr;
		setCmdState(CmdLineState.WAIT);
		view.write("Hello, user!");
		view.write("Please, type `help` for list available commands. ");
		try{
			while (getCMDState().equals(CmdLineState.WAIT)){
				incomStr = view.read();
				readCmd(incomStr);
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
    }

    private void readCmd(String cmdLine) {
		if (cmdLine == null) {
			setCmdState(CmdLineState.EXIT);
		} else {
    		for (CommandProcess dbCommand : commands) {
    			if (dbCommand.canProcess(cmdLine)) {
    				String[] commandLine = cmdLine.replaceAll("\\s", "").toLowerCase().split("\\|");
    				setCmdState(dbCommand.process(commandLine));
    				break;
    			}
    		}
		}
    }

    private CmdLineState getCMDState() {
        return cmdState;
    }

    private void setCmdState(CmdLineState cmdState) {
        this.cmdState = cmdState;
    }

}
