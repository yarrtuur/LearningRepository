package ua.com.juja.sqlcmd.controller;

import org.junit.*;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;

import static org.junit.Assert.*;

public class DBCommandManagerTest {

    private DBCommandManager dbManager;
    String tableName = "clone", ipAddr, connPort, dbSid, dbSidLine, login, passwd;
    DataSet dataSet, dataSetSet, dataSetWhere;

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
        ipAddr ="127.0.0.1";
        connPort = "5432";
        dbSid = "postgres";
        dbSidLine = String.format("jdbc:postgresql://%s:%s/%s", ipAddr, connPort, dbSid);
        login = "postgres";
        passwd = "1";
        dbManager = new DBCommandManager();
        dbManager.toConnect(dbSidLine, login, passwd );
    }

    @After
    public void afterTest(){
        dbManager = null;
    }

    // help has done
    @Test
    public void toHelp() {
        assertEquals(DBFeedBack.OK, dbManager.toHelp());
    }

    // exit has done
    @Test
    public void toExit() {
        assertEquals(DBFeedBack.OK, dbManager.toExit());
    }

    //connect has done
    @Test
    public void toConnect() {
        assertEquals(DBFeedBack.OK, dbManager.toConnect( dbSidLine, login, passwd ) );
    }

    @Test
    public void toConnectWithout() {
        passwd = "ppp";
        System.out.println( String.format(" %s %s %s ", dbSidLine, login, passwd ) );
        assertEquals(DBFeedBack.REFUSE, dbManager.toConnect( dbSidLine, login, passwd ) );
    }

    //update has done
    @Test
    public void toUpdateWithoutConnect() {
        dbManager.toExit();
        dataSetSet = new DataSet();
        dataSetSet.add ( "fld", "1" );
        dataSetWhere = new DataSet();
        dataSetWhere.add ( "fld", "1" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toUpdate(tableName, this.dataSetSet, dataSetWhere) );
    }

    @Test
    public void toUpdateWithConnectWithoutTable() {
        dataSetSet = new DataSet();
        dataSetSet.add ( "fld", "1" );
        dataSetWhere = new DataSet();
        dataSetWhere.add ( "fld", "1" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toUpdate(tableName, this.dataSetSet, dataSetWhere) );
    }

    @Test
    public void toUpdate() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet );
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        dbManager.toInsert ( tableName, dataSet );
        dataSetSet = new DataSet();
        dataSetSet.add ( "fld", "2" );
        dataSetWhere = new DataSet();
        dataSetWhere.add ( "fld", "1" );
        assertEquals(DBFeedBack.OK, dbManager.toUpdate(tableName, this.dataSetSet, dataSetWhere) );
        dbManager.toDrop(tableName);
    }

    // delete has done
    @Test
    public void toDeleteWithoutConnect() {
        dbManager.toExit();
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toDelete(tableName, dataSet) );
    }

   @Test
    public void toDeleteWithConnectWithoutTable() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "flower" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toDelete(tableName, dataSet) );
    }

    @Test
    public void toDelete(){
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet );
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        dbManager.toInsert ( tableName, dataSet );
        assertEquals(DBFeedBack.OK, dbManager.toDelete (tableName, dataSet) );
        dbManager.toDrop ( tableName );
    }

    //drop has done
    @Test
    public void toDropWithoutConnect() {
        dbManager.toExit();
        assertEquals(DBFeedBack.REFUSE, dbManager.toDrop(tableName) );
    }

    @Test
    public void toDropWithConnectWithoutTable() {
        assertEquals(DBFeedBack.REFUSE, dbManager.toDrop(tableName) );
    }

    @Test
    public void toDropWithConnect() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet);
        assertEquals(DBFeedBack.OK, dbManager.toDrop(tableName) );
    }

    // clean has done
    @Test
    public void toCleanWithoutConnect() {
        dbManager.toExit();
        assertEquals(DBFeedBack.REFUSE, dbManager.toClean(tableName) );
    }

    @Test
    public void toCleanWithConnectWithoutTable() {
        dbManager.toDrop ( tableName );
        assertEquals(DBFeedBack.REFUSE, dbManager.toClean(tableName) );
    }

    @Test
    public void toClean() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet );
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        dbManager.toInsert ( tableName, dataSet );
        assertEquals(DBFeedBack.OK, dbManager.toClean(tableName) );
        dbManager.toDrop ( tableName );
    }

    // find has done
    @Test
    public void toFindWithoutConnect() {
        dbManager.toExit();
        assertEquals(DBFeedBack.REFUSE, dbManager.toFind(tableName, false, null) );
    }

    @Test
    public void toFindWitConnectWithoutTable() {
        dbManager.toDrop ( tableName );
        assertEquals(DBFeedBack.REFUSE, dbManager.toFind(tableName, false, null) );
    }

    @Test
    public void toFind(){
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet);
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        dbManager.toInsert ( tableName, dataSet );

        assertEquals ( DBFeedBack.OK, dbManager.toFind ( tableName, true, dataSet ) );
        dbManager.toDrop(tableName);
    }

    // view has done
    @Test
    public void toViewWithoutConnect() {
        dbManager.toExit();
        assertEquals(DBFeedBack.REFUSE, dbManager.toView(false,false, tableName) );
    }

    @Test
    public void toViewWithConnectAllTables() {
        assertEquals(DBFeedBack.OK, dbManager.toView(false,false, tableName) );
    }

    @Test
    public void toViewWithConnectOneTable() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName, dataSet );
        assertEquals(DBFeedBack.OK, dbManager.toView(true,true, tableName) );
        dbManager.toDrop(tableName);
    }

    @Test
    public void toViewWithConnectAllTablesDetails() {
        assertEquals(DBFeedBack.OK, dbManager.toView(true,false, tableName) );
    }

    // insert has done
    @Test
    public void toInsertWithoutConnect() {
        dbManager.toExit();
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toInsert(tableName,dataSet) );
    }

    @Test
    public void toInsertWithConnectWithoutTable() {
        dbManager.toDrop ( tableName );
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toInsert(tableName,dataSet) );
    }

    @Test
    public void toInsert() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet);
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        assertEquals(DBFeedBack.OK, dbManager.toInsert(tableName,dataSet) );
        dbManager.toDrop(tableName);
    }

    // create has done
    @Test
    public void toCreateWithoutConnect() {
        dbManager.toExit();
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        assertEquals(DBFeedBack.REFUSE, dbManager.toCreate(tableName,dataSet) );
    }

    @Test
    public void toCreateWithConnect() {
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        assertEquals(DBFeedBack.OK, dbManager.toCreate(tableName,dataSet) );
        dbManager.toDrop(tableName);
    }
}