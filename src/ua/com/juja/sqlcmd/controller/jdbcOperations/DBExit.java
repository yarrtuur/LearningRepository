package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.controller.JdbcDbBridge;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.viewer.View;

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
