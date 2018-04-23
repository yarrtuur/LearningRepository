package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.CmdLineState;

public class PostgreConnecter implements CommandProcessable {

    private String login, passwd;
    private DBCommandManager dbManager ;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("connect");
    }

    @Override
    public CmdLineState process( DBCommandManager dbManager, String[] commandLine ) {
        this.dbManager = dbManager;
        dbManager.getView().write("Starting connect...");
        dbManager.toConnect( setSocketData(commandLine), this.login, this.passwd );
        return CmdLineState.WAIT;
    }

    public String setSocketData(String[] commandLine) throws RuntimeException {
        String dbSid = "", ipAddr = "", connPort = "";
        try {
            if(commandLine.length > 3) {
                this.login = commandLine[1];
                this.passwd = commandLine[2];
                dbSid = commandLine[3];
            }
        } catch (RuntimeException ex) {
            dbManager.getView ().write ( "Command string format is wrong. Try again." );
            throw new RuntimeException ( ex.getCause () );
        }
        if (commandLine.length > 4) {
            ipAddr = commandLine[4];
        } else {
            ipAddr = "127.0.0.1";
        }
        if (commandLine.length > 5) {
            connPort = commandLine[5];
        } else {
            connPort = "5432";
        }
        return String.format("jdbc:postgresql://%s:%s/%s", ipAddr, connPort, dbSid);

    }

}
