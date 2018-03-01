package maven_sql.sqlCmd;

import maven_sql.sqlCmd.viewer.CmdDialogue;

public class Main {
    public static void main(String[] args) {
        CmdDialogue dialogue =  CmdDialogue.getInstance();
        dialogue.cmdDialogueHolder();
    }
}
