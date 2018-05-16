package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class PostgreConnecter implements CommandProcess {
    private DBCommandManager dbManager;
    private View view;
    private String login, passwd;

    public PostgreConnecter(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("connect");
    }

    @Override
    public CmdLineState process( String[] commandLine) {
        FeedBack resultCode;
        view.write("Starting connect...");
        view.write("-------- PostgreSQL JDBC Connection Testing ------------");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            view.write("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            return CmdLineState.WAIT;
        }

        view.write("PostgreSQL JDBC Driver Registered!");

        view.write(String.format("%d", commandLine.length));
        if (commandLine.length >= 4) {
            try {
                resultCode = dbManager.toConnect(setSocketData(commandLine), this.login, this.passwd);
            } catch (SQLException ex) {
                view.write("Connection Failed! Check output console");
                view.write(ex.getCause().toString());
                return CmdLineState.WAIT;
            }
            if( resultCode.equals(FeedBack.OK)) {
                view.write("You made it, take control your database now!");
            } else {
                view.write("Failed to make connection!");
            }
            return CmdLineState.WAIT;
        }



        return CmdLineState.WAIT;
    }

    private String setSocketData(String[] commandLine) {
        String dbSid = "", ipAddr, connPort;

        if (commandLine[1] != null) this.login = commandLine[1];
        if (commandLine[2] != null) this.passwd = commandLine[2];
        if (commandLine[3] != null) dbSid = commandLine[3];

        if (commandLine.length >= 5) {
            ipAddr = commandLine[4];
        } else {
            ipAddr = "127.0.0.1";
        }
        if (commandLine.length >= 6) {
            connPort = commandLine[5];
        } else {
            connPort = "5432";
        }
        return String.format("jdbc:postgresql://%s:%s/%s", ipAddr, connPort, dbSid);
    }
}
