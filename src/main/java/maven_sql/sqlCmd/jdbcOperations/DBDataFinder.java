package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.jdbcOperations.DBCommand;
import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBDataFinder extends DBCommand {
    private String tblName ;
    private boolean isColumns = false;
    private ResultSet stmtResultSet;
    private int stmtResult ;
    Map<String,String> columnMap;

    public DBDataFinder(String[] command) {
        this.chkCmdData(command);
        System.out.println(this.startSqlAction(this.makeSqlLine()));
    }

    @Override
    public void chkCmdData(String[] command) {
       // find | tableName OR  find | tableName | column | value
        try{
            tblName = command[1];
            if(command.length > 2 ){
                isColumns = true;
                columnMap = new HashMap<>();
                for (int i = 2; i < command.length; i = i + 2) {
                    columnMap.put(command[i],command[i+1]);
                }
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Command string format is wrong. Try again.");
        }

    }

    @Override
    public String makeSqlLine() {
        StringBuilder sb = new StringBuilder();
         sb.append(String.format("SELECT * from public.%s ", tblName));
         if(isColumns){
             for (Map.Entry<String, String> step : columnMap.entrySet() ) {
                 sb.append(String.format(" %s = \'%s\' AND", step.getKey(), step.getValue()));
             }
             sb.replace(sb.length()-3,sb.length()," ");
         }
         return sb.toString();
    }

    @Override
    public ActionResult getActionResult() {
        return null;
    }

    @Override
    public DBFeedBack startSqlAction(String sql){
        if (connection == null ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try {
            System.out.println("Selecting tables from a schema in given database...");
            preparedStatement = connection.prepareStatement(sql);
            stmtResultSet = preparedStatement.executeQuery();


            stmtResult = 0;
            return DBFeedBack.OK;
        }catch(SQLException ex){
            System.out.println("Select data from table interrupted in given database...");
            ex.printStackTrace();
            stmtResult = -1;
            return DBFeedBack.REFUSE;
        }
    }

}
