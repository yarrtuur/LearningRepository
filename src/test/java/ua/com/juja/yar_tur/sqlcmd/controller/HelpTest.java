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

public class HelpTest {
    private CommandProcess command;
    private View view;
    private String[] commandLine;

    @Before
    public void beforeTest() {
        view = new Console();
        command = new Help(view);
        String singleCommand = "help";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println("Start of HelpTest");
    }

    @After
    public void afterTest() {
        command = null;
        System.out.println("End of HelpTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess(commandLine[0]);
        assertTrue(canProcess);
    }

    @Test
    public void canNotProcess() {
        boolean canProcess = command.canProcess("helper");
        assertFalse(canProcess);
    }

    @Test
    public void process() {
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }
}