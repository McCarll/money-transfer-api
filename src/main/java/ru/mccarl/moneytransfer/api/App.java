package ru.mccarl.moneytransfer.api;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;
import ru.mccarl.moneytransfer.api.controller.RESTClientController;
import ru.mccarl.moneytransfer.api.dao.AccountDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class App extends Application<AppConfig> {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/database";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    public static void main(String[] args) throws Exception {
        createDb();
        new App().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-jdbi";
    }

    @Override
    public void initialize(Bootstrap<AppConfig> bootstrap) {
    }

    @Override
    public void run(AppConfig configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");

        final AccountDao personDAO = jdbi.onDemand(AccountDao.class);
        final RESTClientController personResource = new RESTClientController(personDAO);

        environment.jersey().register(personResource);
    }

    public static void createDb() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            log.info("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            log.info("Creating table in given database...");
            stmt = conn.createStatement();

            String sql = "DROP TABLE IF EXISTS ACCOUNTS;";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE ACCOUNTS " +
                    "(id INTEGER not NULL, " +
                    " amount INTEGER, " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            log.info("Created table in given database...");

            sql = "INSERT INTO ACCOUNTS VALUES(1,1)";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ACCOUNTS VALUES(2,2)";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO ACCOUNTS VALUES(3,3)";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            log.error(se.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                log.error(se2.getMessage());
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                log.error(se.getMessage());
            }
        }
        log.info("db is ready!");
    }

}