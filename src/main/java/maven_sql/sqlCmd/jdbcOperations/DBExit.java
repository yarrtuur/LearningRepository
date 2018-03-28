package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.CmdLineState;

import java.sql.SQLException;

public class DBExit extends DBCommand{

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("exit");
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        try {
            if (connection != null) {
                connection.close();
            }
        }catch(SQLException ex){
            /*NOP*/
        }
        return CmdLineState.EXIT;
    }
}
