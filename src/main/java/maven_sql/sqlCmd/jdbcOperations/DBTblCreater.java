package maven_sql.sqlCmd.jdbcOperations;


import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.*;

public class DBTblCreater extends DBCommand {
    private StringBuilder sb = new StringBuilder();
    private int stmtResult ;
    private List<String> listColumn = new LinkedList<>();
    private String tblName ;

    public DBTblCreater(String[] command) {
        this.chkCmdData(command);
            System.out.println(this.sqlAction(this.makeSqlLine()));
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
    public DBFeedBack sqlAction(String sql) {
        if (connection == null ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try {
            System.out.println("Creating table in given database...");
            String sqlstr = String.format("SELECT 1 FROM information_schema.tables WHERE table_name =  \'%s\'",tblName);

            preparedStatement =  connection.prepareStatement(sqlstr);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(String.format("Table %s already exists.",tblName));
                return DBFeedBack.REFUSE;
            }else {
                preparedStatement = connection.prepareStatement(sql);
                stmtResult = preparedStatement.executeUpdate();
                System.out.println("CREATE TABLE Query returned successfully");
                return DBFeedBack.OK;
            }
        }catch(SQLException ex){
            System.out.println("Create table is lost in given database...");
            ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }
    }

    @Override
    public String makeSqlLine(){

        sb.append("CREATE TABLE IF NOT EXISTS ").append(tblName)
                .append(" ( rid serial CONSTRAINT id_table_pk PRIMARY KEY ");
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
