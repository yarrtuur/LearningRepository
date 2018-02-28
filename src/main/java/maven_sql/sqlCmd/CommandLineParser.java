package maven_sql.sqlCmd;

public class CommandLineParser {

    private CommandLineState cmdState;

    private void setCmdState(CommandLineState cmdState) {
        this.cmdState = cmdState;
    }
    CommandLineState getCMDState()    {
        return cmdState;
    }
    CommandLineParser() {
        setCmdState(CommandLineState.WAIT);
    }

    void takeUpCmdLine(String args) {

        String[] command = args.split("\\|");

        if( command[0].equals("exit")){
            setCmdState(CommandLineState.EXIT);
        }
        if( command[0].equals("help") ){
            setCmdState(CommandLineState.WAIT);
            System.out.println(String.format("There is a list of available commands: %s , %s "
                    ,CmdCommandsList.exit
                    ,CmdCommandsList.help
                    ));
        }
    }

}
