package ua.com.juja.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;

import static org.junit.Assert.*;

public class UnreachableTest {
    private DBCommandManager dbManager;
    private CommandProcessable command ;
    private String singleCommand;
    private String[] commandLine;

    @Before
    public void setUp() throws Exception {
        command = new Unreachable ();
        dbManager = new DBCommandManager ();
        singleCommand = "un";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println ("Start of UnreachableTest");
    }



    @After
    public void tearDown() throws Exception {
        command = null;
        dbManager = null;
        System.out.println ("End of UnreachableTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess ( singleCommand );
        assertTrue ( canProcess );
    }

    @Test
    public void process() {
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }
}