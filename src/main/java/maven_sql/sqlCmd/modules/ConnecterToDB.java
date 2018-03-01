package maven_sql.sqlCmd.modules;

import maven_sql.sqlCmd.types_enums.ConnectDataChkStates;
import maven_sql.sqlCmd.types_enums.ConnectFeedBack;
import maven_sql.sqlCmd.types_enums.DatabaseType;
import maven_sql.sqlCmd.types_enums.PasswdType;
import maven_sql.sqlCmd.types_enums.UserType;

public class ConnecterToDB {
    private UserType login;
    private PasswdType passwd;
    private DatabaseType sid;

    public ConnecterToDB(UserType login, PasswdType passwd, DatabaseType sid){
        this.setLogin(login);
        this.setPasswd(passwd);
        this.setSid(sid);
    }
    private void setLogin(UserType login){
        this.login = login;
    }
    private void setPasswd(PasswdType passwd){
        this.passwd = passwd;
    }
    private void setSid(DatabaseType sid){
        this.sid = sid;
    }
    public ConnectFeedBack connectToDB(){
        /*TO DO*/
        this.chkConnectData();

        return ConnectFeedBack.REFUSE;
    }
    public ConnectDataChkStates chkConnectData(){
        /*TO DO*/
        return ConnectDataChkStates.WRONG_DATA;
    }
}
