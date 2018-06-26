package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionProperties;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.MakeDBConnectLine;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;


public class PostgreConnect implements CommandProcess, MakeDBConnectLine {
    private DBCommandManager dbManager;
    private View view;
    private String login, passwd;

    public PostgreConnect(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.startsWith("connect");
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        String connectLine;
        if (!registerJDBCDriver()) return CmdLineState.WAIT;
        try {
            connectLine = setSocketProperties();
            dbManager.toConnect(connectLine, login, passwd);
            view.write("You made it, take control your database now!");
        } catch (SQLException | NullPointerException | ExitException ex) {
            view.write(ex.getMessage());
        }
        return CmdLineState.WAIT;
    }

    private boolean registerJDBCDriver() {
        view.write("-------- PostgreSQL JDBC Connection Testing ------------");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            view.write("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            view.write(ex.getMessage());
            return false;
        }
        view.write("PostgreSQL JDBC Driver Registered!");
        return true;
    }

    @Override
    public String setSocketProperties() throws ExitException {
        ConnectionProperties connectionProperties = new ConnectionProperties();
        login = connectionProperties.getConnDbLogin();
        passwd = connectionProperties.getConnDbPasswd();
        return String.format("%s/%s", connectionProperties.getConnDbSocket(), connectionProperties.getConnDbName());
    }

}
