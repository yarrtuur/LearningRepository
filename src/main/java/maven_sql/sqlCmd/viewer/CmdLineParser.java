package maven_sql.sqlCmd.viewer;

import maven_sql.sqlCmd.jdbcOperations.*;
import maven_sql.sqlCmd.types_enums.EnumCmdsList;
import maven_sql.sqlCmd.types_enums.CmdLineState;

public class CmdLineParser {

    private CmdLineState cmdState;

    public CmdLineParser() {
        setCmdState(CmdLineState.WAIT);
    }

    public void takeUpCmdLine(String args) {

        String[] command = args.replaceAll("\\s","").toLowerCase().split("\\|");
        DBCommand dbCommand = null;
        System.out.println(command[0]);
        switch ( command[0] )
        {
        /*OK*/
            case "exit" :setCmdState(CmdLineState.EXIT);
                System.out.println("Bye...");
                new DBExit();
                break;
        /*OK*/
            case "help" :setCmdState(CmdLineState.WAIT);
                System.out.println("There is a list of available commands: ");
                for (EnumCmdsList enumCmdsList : EnumCmdsList.values())
                    System.out.println(enumCmdsList + ": " + enumCmdsList.getDescription());
                break;
        /*OK*/
            case "connect" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBPostgreConnecter(command);
                break;
        /*OK*/
            case "tables" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBTableViewer(command);
                break;
        /*OK*/
            case "clear" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBTableCleaner(command);
                break;
        /*OK*/
            case "drop" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBTblDroper(command);
                break;
        /*OK*/
            case "create" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBTblCreater(command);
                break;
        /*OK*/
            case "find" :
                setCmdState(CmdLineState.WAIT);
                dbCommand = new DBDataFinder(command);
                break;
        /*OK*/
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
        System.out.println(dbCommand != null ? dbCommand.getActionResult() : null);
        dbCommand = null;
    }
    public CmdLineState getCMDState() {
        return cmdState;
    }
    private void setCmdState(CmdLineState cmdState) {
        this.cmdState = cmdState;
    }
}
