package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBTableCleaner extends DBCommand {
    private int stmtResult ;
    private String tblName ;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("clear");
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
            System.out.println("Deleting data from table in given database...");
            stmtResult = preparedStatement.executeUpdate();
            System.out.println("data deleted successfully");
            return DBFeedBack.OK;
        }catch(SQLException ex){
            System.out.println("Create table is lost in given database...");
            ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }
    }

    @Override
    public String makeSqlLine() {
        return String.format("DELETE FROM %s",tblName);
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
