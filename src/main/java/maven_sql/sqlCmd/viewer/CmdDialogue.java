package maven_sql.sqlCmd.viewer;

import maven_sql.sqlCmd.modules.CmdLineParser;
import maven_sql.sqlCmd.types_enums.CmdLineState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdDialogue {
    private static CmdDialogue _instance = null;
    private CmdDialogue() {
    }
    public static synchronized CmdDialogue getInstance() {
        if (_instance == null)
            _instance = new CmdDialogue();
        return _instance;
    }

    public void cmdDialogueHolder() {
        System.out.println("Hello!");
        System.out.println("Please, set the connect string by format: login | password | database ");
        System.out.println("or type `help` for list available commands. ");

        CmdLineParser clp = new CmdLineParser();
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            /*while getCMDState returns WAIT we are waiting for a new command*/
            while (clp.getCMDState().equals(CmdLineState.WAIT)) {
                clp.takeUpCmdLine(reader.readLine());
            }
        } catch ( IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("See you!");
    }
}
