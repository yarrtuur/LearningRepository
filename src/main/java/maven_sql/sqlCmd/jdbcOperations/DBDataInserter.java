package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBDataInserter extends DBCommand {
    private Map<String ,String> mapColumnsData = new HashMap<>();
    private int stmtResult ;
    private String tblName ;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("insert");
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
            )
        {
            System.out.println("Inserting data to table in given database...");
            stmtResult = preparedStatement.executeUpdate();
            System.out.println("insert data into table is done ");
            return DBFeedBack.OK;
        }catch(SQLException ex){
            System.out.println("insert data into  table is interrupted in given database...");
            ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }

    };

    @Override
    public ActionResult getActionResult() {
        return ( stmtResult == 0 ) ?  ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

    @Override
    public String makeSqlLine() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tblName).append(" ( ");
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for(Map.Entry<String,String> step : mapColumnsData.entrySet()){
            columns.append(step.getKey()).append(",");
            values.append("\'").append(step.getValue()).append("\',");
        }

        columns.replace(columns.length()-1,columns.length(),")");
        values.replace(values.length()-1,values.length(),")");
        sb.append(columns).append(" values ( ").append(values).append(";");
        return sb.toString();
    }

    @Override
    public void chkCmdData(String[] command) {
        try{
            tblName = command[1];
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Command string format is wrong. Try again.");
        }
        try{
            for (int i = 2; i < command.length; i = i + 2) {
                mapColumnsData.put(command[i], command[i+1]);
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("There is no column to insert. Try again.");
        }
    }
}
