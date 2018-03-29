package maven_sql.sqlCmd.jdbcOperations;


import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.*;

public class DBTblCreater extends DBCommand {
    private JdbcDbBridge jdbcDbBridge;
    private int stmtResult;
    private List<String> listColumn;
    private String tblName;
    private PreparedStatement preparedStatement;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("create");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge) {
        this.jdbcDbBridge = jdbcDbBridge;
        this.stmtResult = -1;
        this.listColumn = new LinkedList<>();
        this.tblName = null;

        chkCmdData(commandLine);
        System.out.println(this.startSqlAction(this.makeSqlLine()));
        return CmdLineState.WAIT;
    }

    @Override
    public void chkCmdData(String[] command) {
        try{
            tblName = command[1];
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Command string format is wrong. Try again.");
        }
        try{
            listColumn.addAll(Arrays.asList(command).subList(2, command.length));
        }catch(IndexOutOfBoundsException ex){
            System.out.println("There is no column to create table. Try again.");
        }
    }

    @Override
    public DBFeedBack startSqlAction(String sql) {
        if ( !jdbcDbBridge.isConnected() ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try {
            DBFeedBack chkTableAvailable = searchTableByName();
            if(chkTableAvailable.equals(DBFeedBack.REFUSE)){
                System.out.println(String.format("Table %s already exists.",tblName));
                return chkTableAvailable;
            }
            else {
                return createTableWithParams(sql);
            }
        }catch(SQLException ex){
            System.out.println("Create table is interrupted in given database...");
            ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }
    }

    private DBFeedBack createTableWithParams(String sql) throws SQLException {
        System.out.println("Creating table in given database...");
        preparedStatement = jdbcDbBridge.getConnection().prepareStatement(sql);
        stmtResult = preparedStatement.executeUpdate();
        System.out.println("CREATE TABLE Query returned successfully");
        preparedStatement.close();
        return DBFeedBack.OK;
    }

    private DBFeedBack searchTableByName() throws SQLException{
        String sqlstr = String.format("SELECT 1 FROM information_schema.tables WHERE table_name =  \'%s\'",tblName);
        preparedStatement =  jdbcDbBridge.getConnection().prepareStatement(sqlstr);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            preparedStatement.close();
            return DBFeedBack.REFUSE;
        }else {
            return DBFeedBack.OK;
        }
    }

    @Override
    public String makeSqlLine(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tblName)
                .append(" ( rid serial CONSTRAINT id_").append(tblName).append("_pk PRIMARY KEY ");
        for (String aListColumnData : listColumn) {
            sb.append(",").append(aListColumnData).append(" varchar(200) ");
        }
        sb.append(");");
        return  sb.toString();
    }

    @Override
    public ActionResult getActionResult() {
        return ( stmtResult == 0 ) ?  ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

}
