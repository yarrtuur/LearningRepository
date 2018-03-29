package maven_sql.sqlCmd;

import maven_sql.sqlCmd.controller.MainController;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainControllerTest {

    @Test
    public void testTakeUpCmdLine() {
        MainController clp = new MainController();
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command connect");
        clp.takeUpCmdLine("connect|postgres|1|postgres");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command help");
        clp.takeUpCmdLine("help");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command create");
        clp.takeUpCmdLine("create|test|colonka");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command drop");
        clp.takeUpCmdLine("drop|test");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing again command create");
        clp.takeUpCmdLine("create|test|colonka");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing again command create");
        clp.takeUpCmdLine("create|test|colonka");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command tables");
        clp.takeUpCmdLine("tables");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command tables|fields");
        clp.takeUpCmdLine("tables|fields");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command insert");
        clp.takeUpCmdLine("insert | test| colonka|500");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command find|tablename");
        clp.takeUpCmdLine("find|test");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command find| tablename|column|value");
        clp.takeUpCmdLine("find|test|colonka|500");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command clear| tablename|");
        clp.takeUpCmdLine("clear|test");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing again command find|tablename");
        clp.takeUpCmdLine("find|test");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
      //  clp.takeUpCmdLine("update");
      //  assertEquals(CmdLineState.WAIT, clp.getCMDState());
      //  clp.takeUpCmdLine("delete");
      //  assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing wrong command ");
        clp.takeUpCmdLine("helpje");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing again command drop");
        clp.takeUpCmdLine("drop|test");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        System.out.println("---------------------------");
        System.out.println("testing command exit");
        clp.takeUpCmdLine("exit");
        assertEquals(CmdLineState.EXIT, clp.getCMDState());
    }
}