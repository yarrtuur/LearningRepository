package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnreachableTest {
    private View view;
    private CommandProcess command;
    private String singleCommand;
    private String[] commandLine;

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Start UnreachableTest");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("Finish UnreachableTest");
    }

    @Before
    public void setUp() {
        view = new Console();
        command = new Unreachable(view);
        singleCommand = "un";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println("Start of UnreachableTest");
    }

    @After
    public void tearDown() {
        command = null;
        System.out.println("End of UnreachableTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess(singleCommand);
        assertTrue(canProcess);
    }

    @Test
    public void process() {
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }
}