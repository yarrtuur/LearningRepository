package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBPostgreConnecter extends DBCommand {
    private String[] command;

    public DBPostgreConnecter(String[] command){
        //сonnect|postgres|1|postgres
        this.setLogin(command);
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
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return DBFeedBack.REFUSE;
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        try {
            connection = DriverManager.getConnection(
                    this.makeSqlLine(command) , command[1],  command[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
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
    private void setLogin(String[] command){
        this.command = command;
    }

    @Override
    DBFeedBack sqlAction(String sql) {
        System.out.println(sql);
        return sqlAction();
    }

    @Override
    String makeSqlLine(String[] command){
        return "jdbc:postgresql://127.0.0.1:5432/" + command[3];
    }
}
