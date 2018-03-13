package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBPostgreConnecter extends DBCommand {

    private String login, passwd, dbSid, ipAddr, connPort;

    public DBPostgreConnecter(String[] command){
        //сonnect|postgres|1|postgres|IP|port
        this.chkCmdData(command);
        System.out.println(this.sqlAction("Starting connect..."));
    }

    @Override
    public ActionResult getActionResult() {
        return connection != null ? ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

    public DBFeedBack sqlAction(){

        System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            //ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        try {
            connection = DriverManager.getConnection(
                    this.makeSqlLine() , login, passwd);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            //e.printStackTrace();
            return DBFeedBack.REFUSE;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            return DBFeedBack.OK;
        } else {
            System.out.println("Failed to make connection!");
        }
        return DBFeedBack.REFUSE;
    }
    @Override
    public void chkCmdData(String[] command){
        try {
            this.login = command[1];
            this.passwd = command[2];
            this.dbSid = command[3];
        }catch(IndexOutOfBoundsException ex){
            //ex.printStackTrace();
            System.out.println("Command string format is wrong. Try again.");
        }
            if( command.length > 4 ) {
                this.ipAddr = command[4];
            }else{
                this.ipAddr = "127.0.0.1";
            }
            if( command.length > 5 ) {
                this.connPort = command[5];
            }else{
                connPort = "5432";
            }
    }

    @Override
    public String makeSqlLine(){
        return String.format("jdbc:postgresql://%s:%s/%s",ipAddr, connPort, dbSid);
    }

    @Override
    public DBFeedBack sqlAction(String sql) {
        System.out.println(sql);
        return sqlAction();
    }
}
