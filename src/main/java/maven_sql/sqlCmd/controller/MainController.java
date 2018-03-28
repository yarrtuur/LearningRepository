package maven_sql.sqlCmd.controller;

import maven_sql.sqlCmd.jdbcOperations.*;
import maven_sql.sqlCmd.types_enums.CmdLineState;

import java.util.LinkedList;
import java.util.List;

public class MainController {
    private CmdLineState cmdState;
    private List<DBCommand> commands = new LinkedList<>();

    public MainController() {
        commands.add(new DBPostgreConnecter());
        commands.add(new DBExit());
        commands.add(new DBDataFinder());
        commands.add(new DBTableViewer());
        commands.add(new DBTableCleaner());
        commands.add(new DBTblCreater());
        commands.add(new DBTblDroper());
        /*...*/
        commands.add(new Unreachable());
        setCmdState(CmdLineState.WAIT);
    }

    public void takeUpCmdLine(String args) {
        String[] commandLine = args.replaceAll("\\s","").toLowerCase().split("\\|");
        for (DBCommand dbCommand : commands){
            if( dbCommand.canProcess(commandLine[0])){
                cmdState = dbCommand.process(commandLine);
                break;
            }
        }
    }
    public CmdLineState getCMDState() {
        return cmdState;
    }
    private void setCmdState(CmdLineState cmdState) {
        this.cmdState = cmdState;
    }
}
