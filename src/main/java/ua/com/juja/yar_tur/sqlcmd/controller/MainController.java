package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.controller.commands.*;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.Printable;
import ua.com.juja.yar_tur.sqlcmd.viewer.PrinterData;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.util.LinkedList;
import java.util.List;

public class MainController {
    private View view = new Console();
    private Printable printer = new PrinterData(view);
    private DBCommandManager dbManager = new JDBCDatabaseManager(printer);
    private List<CommandProcess> commands;
    private CmdLineState cmdState;

    /**
     * void initDBCommandList() makes and holds list of commands which do some DB operations
     */
    private void initDBCommandList() {
        commands = new LinkedList<>();
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
        commands.add(new Unreachable(view));

        setCmdState(CmdLineState.WAIT);
    }

    /**
     * takeUpCmd(String args) gets and parse line from cmd and check and order the database command
     */
    private void takeUpCmd(String args) {
        if (args == null) {
            setCmdState(CmdLineState.EXIT);
        } else {
            String[] commandLine = args.replaceAll("\\s", "")
                    .toLowerCase().split("\\|");
            for (CommandProcess dbCommand : commands) {
                if (dbCommand.canProcess(commandLine[0])) {
                    setCmdState(dbCommand.process(commandLine));
                    break;
                }
            }
        }
    }

    /**
     * getCMDState() gets state of cmd line
     */
    private CmdLineState getCMDState() {
        return cmdState;
    }

    /**
     * setCMDState(CmdLineState cmdState) sets state of cmd line
     */
    private void setCmdState(CmdLineState cmdState) {
        this.cmdState = cmdState;
    }

    /**
     * mainDialogueHolder() hold a control of speaking with user by command line
     */
    public void mainDialogueHolder() {
        initDBCommandList();
        view.write("Hello, user!");
        view.write("Please, type `help` for list available commands. ");
        while (this.getCMDState().equals(CmdLineState.WAIT)) {
            this.takeUpCmd(view.read());
        }
    }
}
