package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ConnectFeedBack;

public interface DBConnectable {

    ConnectFeedBack connectToDB();
}
