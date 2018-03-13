package maven_sql.sqlCmd.jdbcOperations;

import java.sql.SQLException;

public class DBExit extends DBCommand{
    public DBExit() {
        try {
            if (connection != null) {
                connection.close();
            }
        }catch(SQLException ex){
            /*NOP*/
        }
    }
}
