package test;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/database-test";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    public static boolean createDb(){
        System.out.println("createDb start");
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "DROP TABLE IF EXISTS ACCOUNTS;";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE ACCOUNTS " +
                    "(id INTEGER not NULL, " +
                    " amount INTEGER, " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            sql = "INSERT INTO ACCOUNTS VALUES(1,50)";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ACCOUNTS VALUES(2,50)";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ACCOUNTS VALUES(3,50)";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ACCOUNTS VALUES(4,50)";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("db is ready!");
        return true;
    }

}
