package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class DBTableViewer extends DBCommand {

    private boolean isDetails = false;
    private List<String> tblList = null;
    private int stmtResult ;
    private ResultSet stmtResultSet;
    private StringBuilder columnString ;
    private PreparedStatement preparedStatement;

    public DBTableViewer(String[] command) {
        this.chkCmdData(command);
        System.out.println(this.startSqlAction(this.makeSqlLine()));
    }

    @Override
    public DBFeedBack startSqlAction(String sql){
        if (connection == null ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try {
            System.out.println("Selecting tables from a schema in given database...");
            int countOfTables = getTablesList(sql);
            if (countOfTables > 0) {
                if (isDetails) {
                    printTableDetails();
                } else {
                    printTableList();
                }
                stmtResult = 0;
                return DBFeedBack.OK;
            }else{
                System.out.println("There are no tables in public scheme.");
                stmtResult = -1;
                return DBFeedBack.REFUSE;
            }
        }catch(SQLException ex){
            System.out.println("Select data from table interrupted in given database...");
            ex.printStackTrace();
            stmtResult = -1;
            return DBFeedBack.REFUSE;
        }
    }

    private void printTableList() {
        columnString = new StringBuilder();
        for (String step : tblList) {
            columnString.append(" ").append(step).append(",");
        }
        System.out.println(String.format("Table: %s ",
                columnString.replace(columnString.length() - 1, columnString.length(), " ").toString()));
    }

    private void printTableDetails() throws SQLException {
        for (String step : tblList) {
            columnString = new StringBuilder();
            preparedStatement = connection.prepareStatement(makeSqlLine(step));
            stmtResultSet = preparedStatement.executeQuery();
            while (stmtResultSet.next()) {
                columnString.append(" ").append(stmtResultSet.getString("column_name")).append(",");
            }
            System.out.println(String.format("Table: %s , Columns: %s", step,
                    columnString.replace(columnString.length() - 1, columnString.length(), " ").toString()));
        }
    }

    private int getTablesList(String sql) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        stmtResultSet = preparedStatement.executeQuery();
        tblList =  new LinkedList<>();
        while (stmtResultSet.next()) {
            tblList.add(stmtResultSet.getString("table_name"));
        }
        return tblList.size();
    }

    @Override
    public String makeSqlLine() {
            return"SELECT t.table_name FROM information_schema.tables t " +
                    "WHERE t.table_schema = 'public'";
    }

    private String makeSqlLine(String tblName){
        StringBuilder sb = new StringBuilder();
        return sb.append(String.format("SELECT c.column_name FROM information_schema.columns c " +
                "WHERE c.table_schema = 'public' AND c.table_name = \'%s\' ", tblName ) ).toString();
    }

    @Override
    public void chkCmdData(String[] command) {
        try{
            if(command.length > 1 && command[1].equals("fields")){
                isDetails = true;
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Command string format is wrong. Try again.");
        }
    }

    @Override
    public ActionResult getActionResult() {
        return ( stmtResult == 0 ) ?  ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

}
