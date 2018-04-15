package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;

import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.util.LinkedList;


public class DBTableViewer  implements CommandProcessable, Preparable {
    private DBCommandManager dbManager;
    private String tableName;
    private boolean isDetails;
    private boolean isOne;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("tables");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {

        this.dbManager = dbManager;

        if( prepareCmdData( commandLine ).equals( ActionResult.ACTION_RESULT_OK ) ) {
            dbManager.toView( isDetails ,isOne);
        }

        return CmdLineState.WAIT;
    }

    @Override
    public ActionResult prepareCmdData(String[] commandLine) {
        if (commandLine.length > 1 && commandLine[1].equals("fields")) {
            isDetails = true;
            isOne = false;
        } else if(commandLine.length > 1 && commandLine[1].equals("tablename")){
            isDetails = true;
            isOne = true;
        } else {
            dbManager.getView().write("Command string format is wrong. Try again.");
        }

        return ActionResult.ACTION_RESULT_OK;
    }

    @Override
    public DBFeedBack startSqlAction(String sql) {

        try {
            view.write("Selecting tables from a schema in given database...");
            int countOfTables = getTablesList(sql);
            if (countOfTables > 0) {
                if (isDetails) {
                    printTableDetails();
                } else {
                    printTableList();
                }
                stmtResult = 0;
                return DBFeedBack.OK;
            } else {
                view.write("There are no tables in public scheme.");
                stmtResult = -1;
                return DBFeedBack.REFUSE;
            }
        } catch (SQLException ex) {
            view.write("Select data from table interrupted in given database...");
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
        view.write(String.format("Table: %s ",
                columnString.replace(columnString.length() - 1, columnString.length(), " ").toString()));
    }

    private void printTableDetails() throws SQLException {
        for (String step : tblList) {
            columnString = new StringBuilder();
            PreparedStatement preparedStatement = jdbcBridge.getConnection().prepareStatement(makeSqlLine(step));
            stmtResultSet = preparedStatement.executeQuery();
            while (stmtResultSet.next()) {
                columnString.append(" ").append(stmtResultSet.getString("column_name")).append(",");
            }
            preparedStatement.close();
            view.write(String.format("Table: %s , Columns: %s", step,
                    columnString.replace(columnString.length() - 1, columnString.length(), " ").toString()));
        }
    }

    private int getTablesList(String sql) throws SQLException {
        PreparedStatement preparedStatement = jdbcBridge.getConnection().prepareStatement(sql);
        stmtResultSet = preparedStatement.executeQuery();
        tblList = new LinkedList<>();
        while (stmtResultSet.next()) {
            tblList.add(stmtResultSet.getString("table_name"));
        }
        preparedStatement.close();
        return tblList.size();
    }

    @Override
    public String makeSqlLine() {
        return "SELECT t.table_name FROM information_schema.tables t " +
                "WHERE t.table_schema = 'public'";
    }

    private String makeSqlLine(String tblName) {
        return String.format("SELECT c.column_name FROM information_schema.columns c " +
                "WHERE c.table_schema = 'public' AND c.table_name = \'%s\' ", tblName);
    }

    @Override
    public void chkCmdData(String[] command) {

    }

    @Override
    public ActionResult getActionResult() {
        return (stmtResult == 0) ? ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }


}
