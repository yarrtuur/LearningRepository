package ua.com.juja.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.types_enums_except.CmdLineState;

import static org.junit.Assert.*;

public class TableCreaterTest {
    private CommandProcessable command ;
    private DBCommandManager dbManager;
    private String singleCommand;
    private String[] commandLine;

    @Before
    public void setUp() throws Exception {
        command = new TableCreater ();
        dbManager = new DBCommandManager ();
        dbManager.toConnect ( "jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "1" );
        singleCommand = "create | clone | fld | integer ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println ("Start of TableCreaterTest");
    }

    @After
    public void tearDown() throws Exception {
        dbManager.toExit ();
        dbManager = null;
        command = null;
        System.out.println ("End of TableCreaterTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess ( "create" );
        assertTrue ( canProcess );
    }

    @Test
    public void process() {
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }

    @Test
    public void processWrongTable() {
        singleCommand = "create |  ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }

    @Test
    public void processWrongCountArguments() {
        singleCommand = "create | clone | fld ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }
}