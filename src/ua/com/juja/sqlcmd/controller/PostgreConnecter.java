package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums_except.CmdLineState;

public class PostgreConnecter implements CommandProcessable {

    private String login, passwd;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("connect");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {
        dbManager.getView().write("Starting connect...");
        dbManager.getView().write(String.format("%d", commandLine.length));
        if (commandLine.length >= 4) {
            dbManager.toConnect(setSocketData(commandLine), this.login, this.passwd);
        }
        return CmdLineState.WAIT;
    }

    public String setSocketData(String[] commandLine) {
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
