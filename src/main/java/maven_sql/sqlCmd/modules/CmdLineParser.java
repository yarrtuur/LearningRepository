package maven_sql.sqlCmd.modules;

import maven_sql.sqlCmd.types_enums.EnumCmdsList;
import maven_sql.sqlCmd.types_enums.CmdLineState;

import java.util.Arrays;

public class CmdLineParser {

    private CmdLineState cmdState;

    public CmdLineParser() {
        setCmdState(CmdLineState.WAIT);
    }
    private void setCmdState(CmdLineState cmdState) {
        this.cmdState = cmdState;
    }
    public CmdLineState getCMDState() {
        return cmdState;
    }

    public void takeUpCmdLine(String args) {
        String[] command = args.toLowerCase().split("\\|");

        switch ( command[0] )
        {
            case "exit" :setCmdState(CmdLineState.EXIT);  break;
            case "help" :setCmdState(CmdLineState.WAIT);
                System.out.println("There is a list of available commands: ");
                for (EnumCmdsList enumCmdsList : EnumCmdsList.values())
                    System.out.println(enumCmdsList + ": " + enumCmdsList.getDescription());
                break;
            case "сonnect" :setCmdState(CmdLineState.WAIT);
                break;
            case "tables" :setCmdState(CmdLineState.WAIT);
                break;
            case "clear" :setCmdState(CmdLineState.WAIT);
                break;
            case "drop" :setCmdState(CmdLineState.WAIT);
                break;
            case "create" :setCmdState(CmdLineState.WAIT);
                break;
            case "find" :setCmdState(CmdLineState.WAIT);
                break;
            case "insert" :setCmdState(CmdLineState.WAIT);
                break;
            case "update" :setCmdState(CmdLineState.WAIT);
                break;
            case "delete" :setCmdState(CmdLineState.WAIT);
                break;

            default: setCmdState(CmdLineState.WAIT);
                System.out.println("Not available command. Please type `help` to list all commands ");
                break;
        }
    }
}
