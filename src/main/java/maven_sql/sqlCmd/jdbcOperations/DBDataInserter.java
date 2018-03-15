package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBDataInserter extends DBCommand {
    private StringBuilder sb = new StringBuilder();
    private Map<String ,String> mapColData = new HashMap<>();
    private int stmtResult ;
    private String tblName ;

    public DBDataInserter(String[] command) {
        this.chkCmdData(command);
        System.out.println(this.sqlAction(this.makeSqlLine()));
    }

    @Override
    public DBFeedBack sqlAction(String sql){
        if (connection == null ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try {
            System.out.println("Inserting data to table in given database...");
            preparedStatement = connection.prepareStatement(sql);
            stmtResult = preparedStatement.executeUpdate();
            System.out.println("insert data into table is done ");
            return DBFeedBack.OK;

        }catch(SQLException ex){
            System.out.println("insert data into  table is lost in given database...");
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
        sb.append("INSERT INTO ").append(tblName).append(" ( ");
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for(Map.Entry<String,String> step : mapColData.entrySet()){
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
                mapColData.put(command[i], command[i+1]);
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("There is no column to insert. Try again.");
        }
    }
}
