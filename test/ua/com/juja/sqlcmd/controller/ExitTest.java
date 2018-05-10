package ua.com.juja.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.types_enums_except.CmdLineState;

import static org.junit.Assert.*;

public class ExitTest {
    private CommandProcessable command;
    private DBCommandManager dbManager;
    private String[] commandLine;

    @Before
    public void setUp() throws Exception {
        command = new Exit ();
        dbManager = new DBCommandManager ();
        String singleCommand = "exit";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println ("Start of ExitTest");
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        dbManager = null;
        System.out.println ("End of ExitTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess ( commandLine[0] );
        assertTrue ( canProcess );
    }

    @Test
    public void canNotProcess() {
        boolean canProcess = command.canProcess ( "exiter" );
        assertFalse ( canProcess );
    }

    @Test
    public void process() {
        assertEquals ( CmdLineState.EXIT, command.process ( dbManager, commandLine ) );
    }
}