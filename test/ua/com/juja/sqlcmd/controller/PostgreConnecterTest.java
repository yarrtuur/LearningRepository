package ua.com.juja.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.types_enums_except.CmdLineState;

import static org.junit.Assert.*;

public class PostgreConnecterTest {
    private PostgreConnecter command;
    private DBCommandManager dbManager;
    private String singleCommand;
    private String[] commandLine;

    @Before
    public void setUp() throws Exception {
        command = new PostgreConnecter ();
        dbManager = new DBCommandManager ();
        singleCommand = "connect | postgres | 1 | postgres | 127.0.0.1 | 5432";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");

        System.out.println ("Start of PostgreConnecterTest");
    }

    @After
    public void tearDown() throws Exception {

        dbManager = null;
        command = null;
        System.out.println ("End of PostgreConnecterTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess ( commandLine[0] );
        assertTrue ( canProcess );
    }

    @Test
    public void process() {
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }

    @Test
    public void setSocketDataWhole() {
        assertEquals ( "jdbc:postgresql://127.0.0.1:5432/postgres", command.setSocketData ( commandLine ) );
    }

    @Test
    public void setSocketData() {
        singleCommand = "connect | postgres | 1 | postgres ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( "jdbc:postgresql://127.0.0.1:5432/postgres", command.setSocketData ( commandLine ) );
    }
}