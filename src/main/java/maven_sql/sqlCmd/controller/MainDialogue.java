package maven_sql.sqlCmd.controller;

import maven_sql.sqlCmd.controller.jdbcOperations.*;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.viewer.Console;
import maven_sql.sqlCmd.viewer.View;

import java.util.LinkedList;
import java.util.List;

public class MainDialogue {
    private static MainDialogue _instance = null;
    private View view = new Console();
    private JdbcDbBridge jdbcDbBridge;
    private List<DBCommand> commands ;
    private CmdLineState cmdState;

    private MainDialogue() {    }

    public static synchronized MainDialogue getInstance() {
        if (_instance == null)
            _instance = new MainDialogue ();
        return _instance;
    }

    private void initBridge(){
        this.jdbcDbBridge = new JdbcDbBridge();
        commands = new LinkedList<> ();
        commands.add(new DBPostgreConnecter ());
        commands.add(new DBExit ());
        commands.add(new DBDataFinder ());
        commands.add(new DBTableViewer ());
        commands.add(new DBTableCleaner ());
        commands.add(new DBTblCreater());
        commands.add(new DBTblDroper());
        commands.add(new DBDataInserter());
        commands.add(new DBHelp());
        /*...*/
        commands.add(new Unreachable());
        setCmdState(CmdLineState.WAIT);
    }

    public CmdLineState getCMDState() {
        return cmdState;
    }

    private void setCmdState(CmdLineState cmdState) {
        this.cmdState = cmdState;
    }

    public void takeUpCmdLine(String args) {
        String[] commandLine = args.replaceAll("\\s","")
                .toLowerCase().split("\\|");
        for (DBCommand dbCommand : commands){
            if( dbCommand.canProcess(commandLine[0])){
                setCmdState(dbCommand.process(commandLine, jdbcDbBridge, view));
                break;
            }
        }
    }

    public void mainDialogueHolder() {
        initBridge ();

        view.write ( "Hello, user!" );
        view.write ( "Please, set the connect string by format: " );
        view.write ( "connect | login | password | database " );
        view.write ( " or type `help` for list available commands. " );

        while (this.getCMDState ().equals ( CmdLineState.WAIT )) {
            this.takeUpCmdLine ( view.read () );
        }
        System.out.println ( "See you!" );
    }

}
