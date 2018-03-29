package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.CmdLineState;

import java.sql.SQLException;

public class DBExit extends DBCommand{

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("exit");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge) {
        try {
            if (jdbcDbBridge.isConnected()) {
                jdbcDbBridge.getConnection().close();
                jdbcDbBridge.setConnection(null);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return CmdLineState.EXIT;
    }
}
