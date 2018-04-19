package ua.com.juja.sqlcmd.controller;

import org.junit.*;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;

import static org.junit.Assert.*;

public class DBCommandManagerTest {

    private DBCommandManager dbManager;
    String tableName = "clone", ipAddr, connPort, dbSid, dbSidLine, login, passwd;
    DataSet dataSet;

    @BeforeClass
    public static void  beforeClass(){
        System.out.println("Before DBCommandManagerTest class");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("After DBCommandManagerTest class");
    }

    @Before
    public void initTest(){
        dbManager = new DBCommandManager();
    }

    @After
    public void afterTest(){
        dbManager = null;
    }

    @Test
    public void toHelp() {
        assertEquals(DBFeedBack.OK, dbManager.toHelp());
    }

    @Test
    public void toExit() {
        assertEquals(DBFeedBack.OK, dbManager.toExit());
    }

    @Test
    public void toConnect() {
          ipAddr ="127.0.0.1";
          connPort = "5432";
          dbSid = "postgres";
          dbSidLine = String.format("jdbc:postgresql://%s:%s/%s", ipAddr, connPort, dbSid);
          login = "postgres";
          passwd = "1";
        System.out.println(dbSidLine);
        assertEquals(DBFeedBack.OK, dbManager.toConnect( dbSidLine, login, passwd ) );
    }

    @Test
    public void toConnectWithout() {
          ipAddr ="127.0.0.1";
          connPort = "5432";
          dbSid = "postgres";
          dbSidLine = String.format("jdbc:postgresql://%s:%s/%s", ipAddr, connPort, dbSid);
          login = "postgres";
          passwd = "ppp";
        System.out.println( String.format(" %s %s %s ", dbSidLine, login, passwd ) );
        assertEquals(DBFeedBack.REFUSE, dbManager.toConnect( dbSidLine, login, passwd ) );
    }

    @Test
    public void toUpdateWithoutConnect() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "flower" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toUpdate(tableName, dataSet) );
    }

    @Test
    public void toUpdateWithConnectWithoutTable() {
        ipAddr ="127.0.0.1";
        connPort = "5432";
        dbSid = "postgres";
        dbSidLine = String.format("jdbc:postgresql://%s:%s/%s", ipAddr, connPort, dbSid);
        login = "postgres";
        passwd = "1";
        dbManager.toConnect( dbSidLine, login, passwd );
        dataSet = new DataSet();
        dataSet.add ( "fld", "flower" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toUpdate(tableName, dataSet) );
    }

    @Test
    public void toDeleteWithoutConnect() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "flower" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toDelete(tableName, dataSet) );
    }

    @Test
    public void toDropWithoutConnect() {
        assertEquals(DBFeedBack.REFUSE, dbManager.toDrop(tableName) );
    }

    @Test
    public void toCleanWithoutConnect() {
        assertEquals(DBFeedBack.REFUSE, dbManager.toClean(tableName) );
    }

    @Test
    public void toFindWithoutConnect() {
        assertEquals(DBFeedBack.REFUSE, dbManager.toFind(tableName, false, null) );
    }

    @Test
    public void toViewWithoutConnect() {
        assertEquals(DBFeedBack.REFUSE, dbManager.toView(false,false, tableName) );
    }

    @Test
    public void toInsertWithoutConnect() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "flower" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toInsert(tableName,dataSet) );
    }

    @Test
    public void toCreate() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "flower" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toCreate(tableName,dataSet) );
    }
}