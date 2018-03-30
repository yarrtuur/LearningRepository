package maven_sql.sqlCmd.viewer;

public interface View {

    void write(String message);

    String read();
}