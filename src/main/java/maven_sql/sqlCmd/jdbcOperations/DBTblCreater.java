package maven_sql.sqlCmd.jdbcOperations;


import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTblCreater extends DBCommand {
    StringBuilder sb = new StringBuilder();
    boolean stmtResult = false;

    public DBTblCreater(String[] command) {
        System.out.println(sqlAction(makeSqlLine(command)));
    }
    @Override
    String makeSqlLine(String[] command){

        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(command[1]);
        sb.append(" ( ");
        sb.append("rid serial CONSTRAINT id_table_pk PRIMARY KEY, ");
        for (int i = 2; i < command.length; i++ ) {
            if ( i != 2){
                sb.append(",");
            }
            sb.append(command[i]);
            sb.append(" varchar(200) ");
        }
        sb.append(");");

        return  sb.toString();
    }
    @Override
    DBFeedBack sqlAction(String sql) {
        if (connection == null ){
            System.out.println("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try {
            System.out.println("Creating table in given database...");
            stmt = connection.createStatement();
            stmtResult = stmt.execute(sql);
            if(stmtResult == true) {
                System.out.println("Created table in given database...");
                return DBFeedBack.OK;
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }
        return DBFeedBack.REFUSE;
    }
    @Override
    public ActionResult getActionResult() {
        return (stmtResult == true) ? ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

}
