package maven_sql.sqlCmd.controller;

import maven_sql.sqlCmd.types_enums.CmdLineState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdDialogue {
    private static CmdDialogue _instance = null;

    private CmdDialogue() {    }

    public static synchronized CmdDialogue getInstance() {
        if (_instance == null)
            _instance = new CmdDialogue();
        return _instance;
    }

    public void cmdDialogueHolder() {
        System.out.println("Hello!");
        System.out.println("Please, set the connect string by format: connect | login | password | database ");
        System.out.println("or type `help` for list available commands. ");

        MainController mainController = new MainController();
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            while (mainController.getCMDState().equals(CmdLineState.WAIT)) {
                mainController.takeUpCmdLine(reader.readLine());
            }
        } catch ( IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("See you!");
    }
}
