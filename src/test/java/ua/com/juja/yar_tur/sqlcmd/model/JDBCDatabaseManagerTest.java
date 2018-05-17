package ua.com.juja.yar_tur.sqlcmd.model;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class JDBCDatabaseManagerTest {

    private DBCommandManager dbManager;
    private String tableName = "clone", ipAddr, connPort, dbSid, dbSidLine, login, passwd;
    private DataSet dataSet, dataSetSet, dataSetWhere;

    @BeforeClass
    public static void  beforeClass(){
        System.out.println("Before JDBCDatabaseManagerTest class");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("After JDBCDatabaseManagerTest class");
    }

    @Before
    public void initTest() throws SQLException {
        ipAddr ="127.0.0.1";
        connPort = "5432";
        dbSid = "postgres";
        dbSidLine = String.format("jdbc:postgresql://%s:%s/%s", ipAddr, connPort, dbSid);
        login = "postgres";
        passwd = "1";
        dbManager = new JDBCDatabaseManager();
        dbManager.toConnect(dbSidLine, login, passwd );
    }

    @After
    public void afterTest() throws SQLException {
        dbManager.toExit();
        dbManager = null;
    }

    // exit has TODO
    @Test
    public void toExit() throws SQLException {
        assertEquals(FeedBack.OK, dbManager.toExit());
    }

    //connect has TODO
    @Test
    public void toConnect() throws SQLException {
        assertEquals(FeedBack.OK, dbManager.toConnect( dbSidLine, login, passwd ) );
    }

    @Test
    public void toConnectWithout() throws SQLException {
        passwd = "ppp";
        System.out.println( String.format(" %s %s %s ", dbSidLine, login, passwd ) );
        assertEquals(FeedBack.REFUSE, dbManager.toConnect( dbSidLine, login, passwd ) );
    }

    //update has TODO
    @Test
    public void toUpdateWithoutConnect()  throws SQLException{
        dbManager.toExit();
        dataSetSet = new DataSet();
        dataSetSet.add ( "fld", "1" );
        dataSetWhere = new DataSet();
        dataSetWhere.add ( "fld", "1" );
        assertEquals(FeedBack.REFUSE, dbManager.toUpdate(tableName, this.dataSetSet, dataSetWhere) );
    }

    @Test
    public void toUpdateWithConnectWithoutTable() throws SQLException {
        dataSetSet = new DataSet();
        dataSetSet.add ( "fld", "1" );
        dataSetWhere = new DataSet();
        dataSetWhere.add ( "fld", "1" );
        assertEquals(FeedBack.REFUSE, dbManager.toUpdate(tableName, this.dataSetSet, dataSetWhere) );
    }

    @Test
    public void toUpdate() throws SQLException{
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
        assertEquals(FeedBack.OK, dbManager.toUpdate(tableName, this.dataSetSet, dataSetWhere) );
        dbManager.toDrop(tableName);
    }

    // delete has TODO
    @Test
    public void toDeleteWithoutConnect()  throws SQLException{
        dbManager.toExit();
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        assertEquals(FeedBack.REFUSE, dbManager.toDelete(tableName, dataSet) );
    }

   @Test
    public void toDeleteWithConnectWithoutTable() throws SQLException {
        dataSet = new DataSet();
        dataSet.add ( "fld", "flower" );
        assertEquals(FeedBack.REFUSE, dbManager.toDelete(tableName, dataSet) );
    }

    @Test
    public void toDelete() throws SQLException{
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet );
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        dbManager.toInsert ( tableName, dataSet );
        assertEquals(FeedBack.OK, dbManager.toDelete (tableName, dataSet) );
        dbManager.toDrop ( tableName );
    }

    //drop has TODO
    @Test
    public void toDropWithoutConnect()  throws SQLException{
        dbManager.toExit();
        assertEquals(FeedBack.REFUSE, dbManager.toDrop(tableName) );
    }

    @Test
    public void toDropWithConnectWithoutTable() throws SQLException {
        assertEquals(FeedBack.REFUSE, dbManager.toDrop(tableName) );
    }

    @Test
    public void toDropWithConnect() throws SQLException {
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet);
        assertEquals(FeedBack.OK, dbManager.toDrop(tableName) );
    }

    // clean has TODO
    @Test
    public void toCleanWithoutConnect()  throws SQLException{
        dbManager.toExit();
        assertEquals(FeedBack.REFUSE, dbManager.toClean(tableName) );
    }

    @Test
    public void toCleanWithConnectWithoutTable() throws SQLException {
        dbManager.toDrop ( tableName );
        assertEquals(FeedBack.REFUSE, dbManager.toClean(tableName) );
    }

    @Test
    public void toClean()  throws SQLException{
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet );
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        dbManager.toInsert ( tableName, dataSet );
        assertEquals(FeedBack.OK, dbManager.toClean(tableName) );
        dbManager.toDrop ( tableName );
    }

    // find has TODO
    @Test
    public void toFindWithoutConnect() throws SQLException {
        dbManager.toExit();
        assertEquals(FeedBack.REFUSE, dbManager.toFind(tableName, false, null) );
    }

    @Test
    public void toFindWitConnectWithoutTable() throws SQLException {
        dbManager.toDrop ( tableName );
        assertEquals(FeedBack.REFUSE, dbManager.toFind(tableName, false, null) );
    }

    @Test
    public void toFind() throws SQLException{
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet);
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        dbManager.toInsert ( tableName, dataSet );

        assertEquals ( FeedBack.OK, dbManager.toFind ( tableName, true, dataSet ) );
        dbManager.toDrop(tableName);
    }

    // view has TODO
    @Test
    public void toViewWithoutConnectAllTables()  throws SQLException{
        dbManager.toExit();
        assertEquals(FeedBack.REFUSE, dbManager.toView() );
    }

    @Test
    public void toViewWithoutConnectOneTable()  throws SQLException{
        dbManager.toExit();
        assertEquals(FeedBack.REFUSE, dbManager.toView(tableName) );
    }

    @Test
    public void toViewWithConnectAllTables() throws SQLException {
        assertEquals(FeedBack.OK, dbManager.toView() );
    }

    @Test
    public void toViewWithConnectOneTable()  throws SQLException{
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName, dataSet );
        assertEquals(FeedBack.OK, dbManager.toView(tableName) );
        dbManager.toDrop(tableName);
    }

    // insert has TODO
    @Test
    public void toInsertWithoutConnect()  throws SQLException{
        dbManager.toExit();
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        assertEquals(FeedBack.REFUSE, dbManager.toInsert(tableName,dataSet) );
    }

    @Test
    public void toInsertWithConnectWithoutTable()  throws SQLException{
        dbManager.toDrop ( tableName );
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        assertEquals(FeedBack.REFUSE, dbManager.toInsert(tableName,dataSet) );
    }

    @Test
    public void toInsert() throws SQLException {
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate(tableName,dataSet);
        dataSet = new DataSet();
        dataSet.add ( "fld", "1" );
        assertEquals(FeedBack.OK, dbManager.toInsert(tableName,dataSet) );
        dbManager.toDrop(tableName);
    }

    // create has TODO
    @Test
    public void toCreateWithoutConnect()  throws SQLException{
        dbManager.toExit();
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        assertEquals(FeedBack.REFUSE, dbManager.toCreate(tableName,dataSet) );
    }

    @Test
    public void toCreateWithConnect()  throws SQLException{
        dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        assertEquals(FeedBack.OK, dbManager.toCreate(tableName,dataSet) );
        dbManager.toDrop(tableName);
    }

}