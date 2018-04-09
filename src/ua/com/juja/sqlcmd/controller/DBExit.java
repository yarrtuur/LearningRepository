package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.model.JdbcBridge;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.SQLException;

public class DBExit  implements CommandProcessable {

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("exit");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcBridge jdbcBridge, View view) {
        try {
            if (jdbcBridge.isConnected()) {
                jdbcBridge.getConnection().close();
                jdbcBridge.setConnection(null);
            }
        } catch (SQLException ex) {
            /*NOP*/
        }
        view.write("You are disconnected now. Bye...");
        return CmdLineState.EXIT;
    }
}
