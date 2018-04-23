package ua.com.juja.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;

import static org.junit.Assert.*;

public class HelpTest {
    private CommandProcessable command;
    private DBCommandManager dbManager;
    private String singleCommand;
    private String[] commandLine;

    @Before
    public void beforeTest(){
        dbManager = new DBCommandManager ();
        command = new Help ();
        singleCommand = "help";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println ("Start of HelpTest");
    }

    @After
    public void afterTest(){
        dbManager = null;
        command = null;
        System.out.println ("End of HelpTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess ( commandLine[0] );
        assertTrue ( canProcess );
    }

    @Test
    public void canNotProcess() {
        boolean canProcess = command.canProcess ( "helper" );
        assertFalse ( canProcess );
    }

    @Test
    public void process() {
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }
}