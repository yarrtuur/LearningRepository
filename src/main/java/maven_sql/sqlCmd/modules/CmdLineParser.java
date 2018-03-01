package maven_sql.sqlCmd.modules;

import maven_sql.sqlCmd.types_enums.CmdCommandsList;
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
        String[] command = args.split("\\|");

        switch (command[0])
        {
            case "exit" :setCmdState(CmdLineState.EXIT);  break;
            case "help" :setCmdState(CmdLineState.WAIT);
                System.out.println("There is a list of available commands: "
                        + Arrays.toString(CmdCommandsList.values()));
                break;
        }
    }
}
