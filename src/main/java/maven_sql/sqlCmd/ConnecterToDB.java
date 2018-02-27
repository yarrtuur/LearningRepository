package maven_sql.sqlCmd;

public class ConnecterToDB {
    private User login;
    private Passwd passwd;
    private Database sid;

    private void setLogin(User login){
        this.login = login;
    }
    private void setPasswd(Passwd passwd){
        this.passwd = passwd;
    }
    private void setSid(Database sid){
        this.sid = sid;
    }
    public ConnectStates connectToDB(){
        /*TO DO*/
        this.chkConnectData();

        return ConnectStates.REFUSE;
    }
    public ConnectData chkConnectData(){
        /*TO DO*/
        return ConnectData.WRONG_DATA;
    }
    public ConnecterToDB(User login, Passwd passwd, Database sid){
        this.setLogin(login);
        this.setPasswd(passwd);
        this.setSid(sid);
    }
}
