package ua.com.juja.sqlcmd.types_enums;

public enum EnumCmdsList {
    exit("exit"),
    help("help"),
    connect("connect | username | password | database_sid | <IP-addr> | <port> "),
    create("create | tableName | column1 | column2 | ... | columnN"),
    tables("tables OR tables | fields  OR tables | tableName "),
    insert("insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN"),
    find("find | tableName OR  find | tableName | column | value"),
    clear("clear | tableName "),
    drop("drop | tableName ")//,
    //update ("update | tableName | column1 | value1 | column2 | value2"),
    //delete ("delete | tableName | column | value")
    ;

    private String description;

    EnumCmdsList(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
