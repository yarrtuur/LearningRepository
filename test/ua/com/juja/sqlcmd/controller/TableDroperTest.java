package ua.com.juja.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;

import static org.junit.Assert.*;

public class TableDroperTest {
    private TableDroper command ;
    private DBCommandManager dbManager;
    private DataSet dataSet;
    private String singleCommand;
    private String[] commandLine;

    @Before
    public void setUp() throws Exception {
        command = new TableDroper ();
        dbManager = new DBCommandManager ();
        dbManager.toConnect ( "jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "1" );
        dataSet = new DataSet ();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate ( "clone", dataSet );
        singleCommand = "drop | clone ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println ("Start of TableDroperTest");
    }

    @After
    public void tearDown() throws Exception {
        dbManager.toExit ();
        dbManager = null;
        command = null;
        System.out.println ("End of TableDroperTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess ( "drop" );
        assertTrue ( canProcess );
    }

    @Test
    public void process() {
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }
    @Test
    public void processWrongTableName() {
        singleCommand = "drop |  ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }

}