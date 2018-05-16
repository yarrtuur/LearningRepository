package ua.com.juja.yar_tur.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import static org.junit.Assert.*;

public class ExitTest {
    private CommandProcess command;
    private DBCommandManager dbManager;
    private View view;
    private String[] commandLine;

    @Before
    public void setUp() {
        dbManager = new JDBCDatabaseManager();
        view = new Console();
        command = new Exit(dbManager, view);
        String singleCommand = "exit";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println("Start of ExitTest");
    }

    @After
    public void tearDown() {
        command = null;
        dbManager = null;
        System.out.println("End of ExitTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess(commandLine[0]);
        assertTrue(canProcess);
    }

    @Test
    public void canNotProcess() {
        boolean canProcess = command.canProcess("exiter");
        assertFalse(canProcess);
    }

    @Test
    public void process() {
        assertEquals(CmdLineState.EXIT, command.process(commandLine));
    }
}