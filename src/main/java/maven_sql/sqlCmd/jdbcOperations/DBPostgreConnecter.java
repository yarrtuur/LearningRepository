package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.ConnectFeedBack;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBPostgreConnecter extends DBCommand implements DBConnectable{
    private String login, passwd, sid;

    public DBPostgreConnecter(String[] args){
        //сonnect|postgres|1|postgres
        this.setLogin(args[1]);
        this.setPasswd(args[2]);
        this.setSid(args[3]);
        this.connectToDB();
    }

    @Override
    public ActionResult getActionResult() {
        return connectToDB().equals(ConnectFeedBack.OK) ? ActionResult.RESULT_OK : ActionResult.RESULT_WRONG;
    }
    @Override
    public ConnectFeedBack connectToDB(){

        System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return ConnectFeedBack.REFUSE;
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        try {
            connection = DriverManager.getConnection(
                    this.makeSqlLine() , login,  passwd);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return ConnectFeedBack.REFUSE;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            return ConnectFeedBack.OK;
        } else {
            System.out.println("Failed to make connection!");
        }
        return ConnectFeedBack.REFUSE;
    }
    private void setLogin(String login){
        this.login = login;
    }
    private void setPasswd(String passwd){
        this.passwd = passwd;
    }
    private void setSid(String sid){
        this.sid = sid;
    }
    @Override
    String makeSqlLine(){
        return "jdbc:postgresql://127.0.0.1:5432/" + sid;
    }
}
