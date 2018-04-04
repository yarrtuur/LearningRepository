package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.controller.JdbcDbBridge;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBPostgreConnecter extends DBCommand {

    private String login, passwd, dbSid, ipAddr, connPort;
    private JdbcDbBridge jdbcDbBridge;
    private View view;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals ( "connect" );
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view) {
        this.jdbcDbBridge = jdbcDbBridge;
        this.login = null;
        this.passwd = null;
        this.dbSid = null;
        this.ipAddr = null;
        this.connPort = null;
        this.view = view;
        chkCmdData ( commandLine );
        System.out.println ( this.startSqlAction ( "Starting connect..." ) );
        return CmdLineState.WAIT;
    }

    @Override
    public ActionResult getActionResult() {
        return jdbcDbBridge.isConnected () ? ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

    private DBFeedBack sqlAction() {

        view.write ( "-------- PostgreSQL JDBC Connection Testing ------------" );

        try {
            Class.forName ( "org.postgresql.Driver" );
        } catch (ClassNotFoundException ex) {
            view.write ( "Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!" );
            return DBFeedBack.REFUSE;
        }

        view.write ( "PostgreSQL JDBC Driver Registered!" );

        try {
            jdbcDbBridge.setConnection ( DriverManager.getConnection (
                    this.makeSqlLine (), login, passwd ) );
        } catch (SQLException e) {
            view.write ( "Connection Failed! Check output console" );
            return DBFeedBack.REFUSE;
        }

        if (jdbcDbBridge.isConnected ()) {
            view.write ( "You made it, take control your database now!" );
            return DBFeedBack.OK;
        } else {
            view.write ( "Failed to make connection!" );
        }
        return DBFeedBack.REFUSE;
    }

    @Override
    public void chkCmdData(String[] commandLine) {
        try {
            this.login = commandLine[1];
            this.passwd = commandLine[2];
            this.dbSid = commandLine[3];
        } catch (IndexOutOfBoundsException ex) {
            view.write ( "Command string format is wrong. Try again." );
        }
        if (commandLine.length > 4) {
            this.ipAddr = commandLine[4];
        } else {
            this.ipAddr = "127.0.0.1";
        }
        if (commandLine.length > 5) {
            this.connPort = commandLine[5];
        } else {
            connPort = "5432";
        }
    }

    @Override
    public String makeSqlLine() {
        return String.format ( "jdbc:postgresql://%s:%s/%s", ipAddr, connPort, dbSid );
    }

    @Override
    public DBFeedBack startSqlAction(String sql) {
        System.out.println ( sql );
        return sqlAction ();
    }
}
