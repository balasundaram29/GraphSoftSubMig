
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    public static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    private static final SessionFactory sessionFactory1 = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
// Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
// Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory1;
    }
    public static final ThreadLocal session = new ThreadLocal();

    public static Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        // Open a new Session, if this thread has none yet
        if (s == null) {
            s = sessionFactory.openSession();
            // Store it in the ThreadLocal variable
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s != null) {
            s.close();
        }
        session.set(null);
    }
    static Connection conn;
    static Statement st;

    public static void cleanOldTables() {
        try {
            // Step 1: Load the JDBC driver.
            Class.forName("org.hsqldb.jdbcDriver");
            System.out.println("Driver Loaded.");
            // Step 2: Establish the connection to the database.
            String url = "jdbc:derby:G:\\newdb\\typesdb;create=true";
            conn = DriverManager.getConnection(url, "app", "");
            System.out.println("Got Connection.");
            String drop1 = "drop table TYPEDETAILS if exists";
            String drop2 = "drop table PUMPDETAILS if exists";
            st = conn.createStatement();
            st.executeUpdate(drop1);
            st.executeUpdate(drop2);
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();
            System.exit(0);
        }

    }

    public static void setup(String sql) {
        try {
            // Step 1: Load the JDBC driver.
            Class.forName("org.hsqldb.jdbcDriver");
            System.out.println("Driver Loaded.");
            // Step 2: Establish the connection to the database.
            String url = "jdbc:derby:G:\\newdb\\typesdb;create=true";
            conn = DriverManager.getConnection(url, "app", "");
            System.out.println("Got Connection.");
            st = conn.createStatement();
            st.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void checkData(String sql) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            System.out.println("Driver Loaded.");
            // Step 2: Establish the connection to the database.
            String url = "jdbc:derby:G:\\newdb\\typesdb;create=true";
            conn = DriverManager.getConnection(url, "app", "");


            st = conn.createStatement();
            HibernateUtil.outputResultSet(st.executeQuery(sql));
//			conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void outputResultSet(ResultSet rs) throws Exception {
        ResultSetMetaData metadata = rs.getMetaData();

        int numcols = metadata.getColumnCount();
        String[] labels = new String[numcols];
        int[] colwidths = new int[numcols];
        int[] colpos = new int[numcols];
        int linewidth;
        System.out.println("Table Name : " + metadata.getTableName(1));
        for (int i = 0; i < numcols; i++) {
            labels[i] = metadata.getColumnLabel(i + 1); // get its label
            System.out.print(labels[i] + "  ");
        }

        System.out.println("");

        System.out.println("------------------------");

        while (rs.next()) {
            for (int i = 0; i < numcols; i++) {
                Object value = rs.getObject(i + 1);
                if (value == null) {
                    System.out.print("       ");
                } else {
                    System.out.print(value.toString().trim() + "   ");
                }
            }
            System.out.println("       ");
            System.out.println("       ");
        }
    }
}
