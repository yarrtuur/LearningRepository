package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HelpMockTest {

	View view = mock(Console.class);
	CommandProcess command = new Help(view);

	@Test
	public void test(){
		when(command.process(anyVararg())).thenReturn(CmdLineState.WAIT);
		//verify(command).process(new String[]{"help"});
	}

}