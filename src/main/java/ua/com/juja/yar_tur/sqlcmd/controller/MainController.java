package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.util.List;

public class MainController {
    View view;
    DBCommandManager dbManager;
    private List<CommandProcess> commands;
    private CmdLineState cmdState;

    public MainController(DBCommandManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    public void mainDialogueHolder() {
        commands = new CreateCommands(view, dbManager).initCommandList();
        view.write("Hello, user!");
        view.write("Please, type `help` for list available commands. ");
        while (this.getCMDState().equals(CmdLineState.WAIT)) {
            this.readCmd(view.read());
        }
    }

    private void readCmd(String cmdLine) {
        String[] commandLine = cmdLine.replaceAll(" ", "").toLowerCase().split("\\|");
        for (CommandProcess dbCommand : commands) {
            if (dbCommand.canProcess(commandLine[0])) {
                setCmdState(dbCommand.process(commandLine));
                break;
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
