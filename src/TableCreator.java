
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bala
 */
public class TableCreator {

    public static void main(String args[]) throws Exception {
       // checkData();
     Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        // Class.forName("org.hsqldb.jdbc.JDBCDriver");
        String url = "jdbc:derby:G:\\newdb\\typesdb;create=true";
        //String
    // String url = "jdbc:derby:" + dbName + ";create=true";
        Connection conn = DriverManager.getConnection(url, "app", "");
        Statement stmt = conn.createStatement();
        String sql;
         sql = "drop table TYPES";
         stmt.executeUpdate(sql);
         sql="drop table OBSVALUES";
        stmt.executeUpdate(sql);
         sql="create table  TYPES ( uid INT , type VARCHAR(10),hp FLOAT, kw FLOAT,delsize FLOAT,discharge FLOAT,head FLOAT, eff FLOAT,mcurrent FLOAT,headLower FLOAT,headUpper FLOAT, voltage INTEGER, phases INTEGER)";
         stmt.executeUpdate(sql);
         sql="create table  OBSVALUES ( obid INT , sno VARCHAR(20) UNIQUE,obdate DATE,type VARCHAR(20),head FLOAT,disch FLOAT, eff FLOAT,mcurrent FLOAT) ";
       stmt.executeUpdate(sql);
         /* sql = "drop table OBSERVED if exists";
        stmt.executeUpdate(sql);
        sql = "drop table DECLARED if exists";
        stmt.executeUpdate(sql);
        sql = "drop table READINGROWS if exists";
        stmt.executeUpdate(sql);
        sql = "drop table READINGS if exists";
        stmt.executeUpdate(sql);*/

       /* sql = "create table if not exists OBSERVED  ( oid INT , readingsno VARCHAR(20) UNIQUE,discharge FLOAT,head FLOAT, eff FLOAT,mcurrent FLOAT)";
        stmt.executeUpdate(sql);
        sql = "create table if not exists DECLARED ( ptype varchar(20) UNIQUE,hp FLOAT, kw FLOAT,delsize FLOAT,discharge FLOAT,head FLOAT, eff FLOAT,mcurrent FLOAT,headLower FLOAT,headUpper FLOAT, voltage INTEGER, phases INTEGER)";
        stmt.executeUpdate(sql);
        sql = "create table if not exists READINGROWS ( id INT ,parentsno VARCHAR(20),idx INT, cardinalno INT,freq FLOAT,delgaugereading FLOAT, disch FLOAT,volt FLOAT,curr FLOAT,watts FLOAT)";
        stmt.executeUpdate(sql);
        sql = "create table if not exists READINGS ( sno VARCHAR(20) UNIQUE,inpassno VARCHAR(20) ,rdate DATE,isref VARCHAR(20),remarks VARCHAR(20),dischmax FLOAT,headmax FLOAT,currmax FLOAT,"
                + "effmax FLOAT,declvalues VARCHAR(20))";
        stmt.executeUpdate(sql);*/

        // sql = "select * from types";
        //ResultSet rs = stmt.executeQuery(sql);
        //rs.next();
        //System.out.print(rs.getString("type")+rs.getInt("uid"));
        //stmt.executeUpdate(sql);*/
       // sql="delete  from declared where ptype ='ATS04255'";
       // stmt.executeUpdate(sql);
        stmt.close();
        conn.close();
        System.out.println("Created successfully");
       // checkData();

    }

    public static void checkData() {
        HibernateUtil.checkData("select * from OBSERVED ");
        HibernateUtil.checkData("select * from DECLARED ");
        HibernateUtil.checkData("select * from READINGROWS");
        HibernateUtil.checkData("select * from READINGS");
        System.exit(0);
    }

    public static void createTablesIfNotExists(){
        try
        {
         Class.forName("org.hsqldb.jdbc.JDBCDriver");
        String url = "jdbc:derby:G:\\newdb\\typesdb;create=true";
      //  jdbc:derby:" + dbName + ";create=true
        Connection conn = DriverManager.getConnection(url, "app", "");
        Statement stmt = conn.createStatement();
        String sql;

       sql="create table  TYPES ( uid INT , type VARCHAR(10),hp FLOAT, kw FLOAT,delsize FLOAT,discharge FLOAT,head FLOAT, eff FLOAT,mcurrent FLOAT,headLower FLOAT,headUpper FLOAT, voltage INTEGER, phases INTEGER)";
     //stmt.executeUpdate(sql);
        sql="create table  OBSVALUES ( obid INT , sno VARCHAR(20) UNIQUE,obdate DATE,type VARCHAR(20),head FLOAT,disch FLOAT, eff FLOAT,mcurrent FLOAT) ";
     // stmt.executeUpdate(sql);
        sql = "create table  OBSERVED  ( oid INT , readingsno VARCHAR(20) UNIQUE,discharge FLOAT,head FLOAT, eff FLOAT,mcurrent FLOAT)";
        stmt.executeUpdate(sql);
        sql = "create table  DECLARED ( ptype varchar(20) UNIQUE,hp FLOAT, kw FLOAT,delsize FLOAT,discharge FLOAT,head FLOAT, eff FLOAT,mcurrent FLOAT,headLower FLOAT,headUpper FLOAT, voltage INTEGER, phases INTEGER)";
        stmt.executeUpdate(sql);
        sql = "create table  READINGROWS ( id INT ,parentsno VARCHAR(20),idx INT, cardinalno INT,freq FLOAT,delgaugereading FLOAT, disch FLOAT,volt FLOAT,curr FLOAT,watts FLOAT)";
        stmt.executeUpdate(sql);
        sql = "create table  READINGS ( sno VARCHAR(20) UNIQUE,inpassno VARCHAR(20) ,rdate DATE,isref VARCHAR(20),remarks VARCHAR(20),dischmax FLOAT,headmax FLOAT,currmax FLOAT,"
                + "effmax FLOAT,declvalues VARCHAR(20))";
        stmt.executeUpdate(sql);
          stmt.close();
        conn.close();
  System.out.println("Tables are there now");
        }catch(Exception ex){
// System.out.println("Tables are not there now");
            ///ex.printStackTrace();
        }
    }



}
