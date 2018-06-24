package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class UnreachableTest {
	private String singleCommand;
	private String[] commandLine;
	private View view;
	private CommandProcess command;

	@BeforeClass
	public static void setUpClass() {
		System.out.println("Before UnreachableTest.class");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("After UnreachableTest.class");
	}

	@Before
	public void setUp() {
		singleCommand = "single";
		view = new Console();
		command = new Unreachable(view);
		commandLine = new String[]{"single"};
	}

	@After
	public void tearDown() {
		singleCommand = null;
		command = null;
		commandLine = null;
	}

	@Test
	public void canProcess() {
		assertTrue(command.canProcess(singleCommand));
	}

	@Test
	public void process() {
		assertEquals(CmdLineState.WAIT,command.process(commandLine));
	}
}