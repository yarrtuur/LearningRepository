package maven_sql.sqlCmd.types_enums;

public enum EnumCmdsList {
    exit ("exit"),
    help ("help"),
    connect ("connect | username | password | database"),
    tables ("tables OR tables | fields "),
    clear ("clear | tableName "),
    drop ("drop | tableName "),
    create ("create | tableName | column1 | column2 | ... | columnN"),
    find ("find | tableName OR  find | tableName | column | value"),
    insert ("insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN"),
    update ("update | tableName | column1 | value1 | column2 | value2"),
    delete ("delete | tableName | column | value");

    private String description;

    private EnumCmdsList(String description) {
        this.description = description;
    }

    public String getDescription() {return description;}
}
