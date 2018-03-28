package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBTblDroper extends DBCommand {
    private int stmtResult ;
    private String tblName ;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("drop");
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        chkCmdData(commandLine);
        System.out.println(this.startSqlAction(this.makeSqlLine()));
        return CmdLineState.WAIT;
    }

    @Override
    public DBFeedBack startSqlAction(String sql){
        if (connection == null ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try (
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ){
            System.out.println("Droping table from given database...");
            stmtResult = preparedStatement.executeUpdate();
            System.out.println("table deleted successfully");
            return DBFeedBack.OK;
        }catch(SQLException ex){
            System.out.println("Drop table is lost in given database...");
            ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }
    };

    @Override
    public String makeSqlLine() {
        return String.format("DROP TABLE %s",tblName);
    }

    @Override
    public void chkCmdData(String[] command) {
        try{
            tblName = command[1];
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Command string format is wrong. Try again.");
        }
    }

    @Override
    public ActionResult getActionResult() {
        return ( stmtResult == 0 ) ?  ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }
}
