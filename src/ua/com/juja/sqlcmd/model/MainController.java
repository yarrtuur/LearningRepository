package ua.com.juja.sqlcmd.model;

import ua.com.juja.sqlcmd.controller.*;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.viewer.Console;
import ua.com.juja.sqlcmd.viewer.View;

import java.util.LinkedList;
import java.util.List;

public class MainController {

    private View view = new Console();
    private DBCommandManager dbManager = new DBCommandManager();
    private List<CommandProcessable> commands;
    private CmdLineState cmdState;

    public MainController() {
    }

    /**
     * void initDBCommandList() makes and holds list of commands which do some DB operations
     * */
    private void initDBCommandList() {
        commands = new LinkedList<>();
        commands.add(new Exit());
        commands.add(new Help ());
        commands.add(new PostgreConnecter());
        commands.add(new TableCreater());
        commands.add(new TableViewer());
        commands.add(new DataInserter());
        commands.add(new DataFinder());
        commands.add(new TableCleaner());
        commands.add(new TableDroper ());
        commands.add(new DataDeleter ());
        commands.add(new DBUpdater ());
        /*...*/
        commands.add(new Unreachable());

        setCmdState(CmdLineState.WAIT);
    }

    /**
     * takeUpCmd(String args) gets and parse line from cmd and check and order the database command
     * */
    private void takeUpCmd(String args) {
        if (args == null) {
            setCmdState(CmdLineState.EXIT);
        } else {
            String[] commandLine = args.replaceAll("\\s", "")
                    .toLowerCase().split("\\|");
            for ( CommandProcessable dbCommand : commands ) {
                if (dbCommand.canProcess(commandLine[0])) {
                    setCmdState(dbCommand.process( this.dbManager, commandLine ) );
                    break;
                }
            }
        }
    }

    /**
     *  getCMDState() gets state of cmd line
     * */
    private CmdLineState getCMDState() {
        return cmdState;
    }

    /**
     *  setCMDState(CmdLineState cmdState) sets state of cmd line
     * */
    private void setCmdState(CmdLineState cmdState) {
        this.cmdState = cmdState;
    }

    /**
     * mainDialogueHolder() hold a control of speaking with user by command line
     * */
    public void mainDialogueHolder() {
        initDBCommandList();
        view.write("Hello, user!");
        view.write("Please, type `help` for list available commands. ");
        try {
            while (this.getCMDState().equals(CmdLineState.WAIT)) {
                this.takeUpCmd(view.read());
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
