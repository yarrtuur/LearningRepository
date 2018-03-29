package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.PreparedStatement;
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
    private ResultSet stmtResultSet;
    private ResultSetMetaData stmtResultSetMeta;
    private int stmtResult ;
    private Map<String,String> columnMap;
    private JdbcDbBridge jdbcDbBridge;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("find");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge) {
        this.jdbcDbBridge = jdbcDbBridge;
        this.tblName = null;
        this.isColumns = false;
        this.stmtResultSet = null;
        this.stmtResultSetMeta = null;
        this.stmtResult = -1;
        chkCmdData(commandLine);
        System.out.println(this.startSqlAction(this.makeSqlLine()));
        return CmdLineState.WAIT;
    }

    @Override
    public void chkCmdData(String[] command) {
        try{
            tblName = command[1];
            if( command.length > 2 ){
                isColumns = true;
                columnMap = new HashMap<>();
                for (int i = 2; i < command.length; i = i + 2) {
                    columnMap.put(command[i], command[i + 1]);
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
             sb.append(" WHERE ");
             for (Map.Entry<String, String> step : columnMap.entrySet()) {
                 sb.append(String.format(" %s = \'%s\' AND", step.getKey(), step.getValue()));
             }
             sb.replace(sb.length() - 3, sb.length(), " ");
         }
         return sb.toString();
    }

    @Override
    public ActionResult getActionResult() {
        return ( stmtResult == 0 ) ?  ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

    @Override
    public DBFeedBack startSqlAction(String sql){
        if (!jdbcDbBridge.isConnected()){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try(
            PreparedStatement preparedStatement = jdbcDbBridge.getConnection().prepareStatement(sql)
                )
            {
            System.out.println("Selecting data from table in given database...");
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
