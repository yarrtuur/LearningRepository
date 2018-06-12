package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HelpTest {

	private String singleCommand;
	private String[] commandLine;
	private View view;
	private CommandProcess command;

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before HelpTest.class");
	}

	@AfterClass
	public  static void afterClass() {
		System.out.println("After HelpTest.class");
	}

	@Before
	public void setUp() throws Exception{
		singleCommand = "help";
		commandLine = new String[]{"help"};
		view = new Console();
		command = new Help(view);
	}

	@After
	public void afterTest() {
		singleCommand = null;
		view = null;
	}

	@Test
	public void canProcessTest(){
		assertTrue(command.canProcess(singleCommand));
	}

	@Test
	public void processTest(){
		assertEquals(CmdLineState.WAIT,command.process(commandLine));
	}

}