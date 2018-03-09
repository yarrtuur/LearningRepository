package maven_sql.sqlCmd.modules;

import maven_sql.sqlCmd.jdbcOperations.*;
import maven_sql.sqlCmd.types_enums.EnumCmdsList;
import maven_sql.sqlCmd.types_enums.CmdLineState;

public class CmdLineParser {

    private CmdLineState cmdState;

    public CmdLineParser() {
        setCmdState(CmdLineState.WAIT);
    }

    public void takeUpCmdLine(String args) {
        String[] command = args.toLowerCase().split("\\|");
        DBCommand dbCommand = null;
        System.out.println(command[0]);
        switch ( command[0] )
        {
            case "exit" :setCmdState(CmdLineState.EXIT);  break;
            case "help" :setCmdState(CmdLineState.WAIT);
                System.out.println("There is a list of available commands: ");
                for (EnumCmdsList enumCmdsList : EnumCmdsList.values())
                    System.out.println(enumCmdsList + ": " + enumCmdsList.getDescription());
                break;
            case "сonnect" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBPostgreConnecter(command);
                System.out.println(dbCommand.getActionResult());
                break;
            case "tables" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBDataSelecter(command);
                break;
            case "clear" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBTblCleaner(command);
                break;
            case "drop" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBTblDroper(command);
                break;
            case "create" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBTblCreater(command);
                break;
            case "find" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBDataFinder(command);
                break;
            case "insert" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBDataInserter(command);
                break;
            case "update" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBUpdater(command);
                break;
            case "delete" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBDataDeleter(command);
                break;

            default: setCmdState(CmdLineState.WAIT);
                System.out.println("Not available command. Please type `help` to list all commands ");
                break;
        }
    }
    public CmdLineState getCMDState() {
        return cmdState;
    }
    private void setCmdState(CmdLineState cmdState) {
        this.cmdState = cmdState;
    }
}