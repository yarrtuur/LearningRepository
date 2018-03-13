package maven_sql.sqlCmd.jdbcOperations;


import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBTblCreater extends DBCommand {
    private StringBuilder sb = new StringBuilder();
    private boolean  stmtResult ;
    private String tblName;
    private List<String> listColumnData = new ArrayList<>();
    //create ("create | tableName | column1 | column2 | ... | columnN")

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
            for (int i = 2; i < command.length ; i = i + 2 ) {
                listColumnData.add(command[i]);
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("There is no column to create table. Try again.");
        }
    }

    @Override
    public String makeSqlLine(){

        sb.append("CREATE TABLE IF NOT EXISTS ").append(tblName)
                .append(" ( rid serial CONSTRAINT id_table_pk PRIMARY KEY, ");
        for (int i = 2; i < listColumnData.size(); i++ ) {
            if ( i != 2){
                sb.append(",");
            }
            sb.append(listColumnData.get(i)).append(" varchar(200) ");
        }
        sb.append(");");

        return  sb.toString();
    }

    @Override
    public DBFeedBack sqlAction(String sql) {
        if (connection == null ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try {
            System.out.println("Creating table in given database...");
            stmt = connection.createStatement();
            stmtResult = stmt.execute(sql);
                System.out.println("Created table in given database...");
                return DBFeedBack.OK;
        }catch(SQLException ex){
            System.out.println("Create table is lost in given database...");
            return DBFeedBack.REFUSE;
        }

    }
    
    @Override
    public ActionResult getActionResult() {
        return ( stmtResult ) ?  ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

}
