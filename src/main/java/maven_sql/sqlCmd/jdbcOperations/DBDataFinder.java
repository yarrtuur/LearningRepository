package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DBDataFinder extends DBCommand {
    private String tblName ;
    private boolean isColumns = false;
    private boolean isWhere = false;
    private ResultSet stmtResultSet;
    private ResultSetMetaData stmtResultSetMeta;
    private int stmtResult ;
    private Map<String,String> columnMap;

    public DBDataFinder(String[] command) {
        this.chkCmdData(command);
        System.out.println(this.startSqlAction(this.makeSqlLine()));
    }

    @Override
    public void chkCmdData(String[] command) {
       // find | tableName OR  find | tableName | column | value
        try{
            tblName = command[1];
            if( command.length > 2 ){
                isColumns = true;
                columnMap = new HashMap<>();
                isWhere = command[2].toLowerCase().equals("where");        //for future realisation
                int i = (command[2].toLowerCase().equals("where")) ? 3 : 2;//for future realisation
                for (; i < command.length; i = i + 2) {
                    columnMap.put(command[i], command[i + 1]);
                }
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Command string format is wrong. Try again.");
        }
    }
    //connect|postgres|1|postgres
    @Override
    public String makeSqlLine() {
        StringBuilder sb = new StringBuilder();
         sb.append(String.format("SELECT * from public.%s ", tblName));
         if(isColumns){
            // if(isWhere) {   // has frozen for future realisation
                 sb.append(" WHERE ");
                 for (Map.Entry<String, String> step : columnMap.entrySet()) {
                     sb.append(String.format(" %s = \'%s\' AND", step.getKey(), step.getValue()));
                 }
                 sb.replace(sb.length() - 3, sb.length(), " ");
             //}
         }
         return sb.toString();
    }

    @Override
    public ActionResult getActionResult() {
        return ( stmtResult == 0 ) ?  ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

    @Override
    public DBFeedBack startSqlAction(String sql){
        if (connection == null ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try {
            System.out.println("Selecting data from table in given database...");
            preparedStatement = connection.prepareStatement(sql);
            stmtResultSet = preparedStatement.executeQuery();
            stmtResultSetMeta = stmtResultSet.getMetaData();
            printFoundData();
            stmtResult = 0;
            return DBFeedBack.OK;
        }catch(SQLException ex){
            System.out.println("Select data from table interrupted in given database...");
            ex.printStackTrace();
            stmtResult = -1;
            return DBFeedBack.REFUSE;
        }
    }

    private void printFoundData() throws SQLException {
        List<String> columnsList = new LinkedList<>();
        StringBuilder sb ;

        for (int i = 1; i <= stmtResultSetMeta.getColumnCount(); i++) {
            columnsList.add(stmtResultSetMeta.getColumnName(i));
        }
        sb = new StringBuilder();
        sb.append(" | ");
        for (String aColumnsList : columnsList) {
            sb.append(aColumnsList).append(" | ");
        }
        System.out.println(sb.toString());
        while( stmtResultSet.next() ){
        sb = new StringBuilder();
        sb.append(" | ");
            for (String aColumnsList : columnsList) {
                sb.append(stmtResultSet.getString(aColumnsList)).append(" | ");
            }
            System.out.println(sb.toString());
        }
    }

}
