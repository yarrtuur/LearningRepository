package maven_sql.sqlCmd.controller.jdbcOperations;

import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.viewer.View;

import java.sql.SQLException;

public class DBExit extends DBCommand{

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("exit");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view) {
        try {
            if (jdbcDbBridge.isConnected()) {
                jdbcDbBridge.getConnection().close();
                jdbcDbBridge.setConnection(null);
                view.write ("You are disconnected now. Bye...");
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return CmdLineState.EXIT;
    }
}
