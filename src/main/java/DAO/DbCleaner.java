package DAO;

import BEANS.Company;
import BEANS.CompanyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbCleaner {
    protected static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());

    /**
     * Constructor for DbCleaner.
     * This constructor is responsible for resetting specific information in the database.
     * It deletes all records from the `customers_vs_coupons`, `coupons`, `companies`, and `customers` tables.
     * Additionally, it resets the AUTO_INCREMENT values for the `coupons`, `customers`, and `companies` tables.
     */
    public DbCleaner() {
        Connection connection = null;
        ConnectionPool connectionPool = null;
        logger.info("DbCleaner - deletes and resets the AUTO_INCREMENT values  from the `customers_vs_coupons`, `coupons`, `companies`, and `customers` tables.");

        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            // SQL query that deletes all records from specified tables and resets the AUTO_INCREMENT values.
            String[] queries = {
                    "DELETE from customers_vs_coupons;",
                    "DELETE from coupons;",
                    "DELETE from companies ;",
                    "DELETE from customers;",
                    "ALTER TABLE coupons AUTO_INCREMENT = 1;",
                    "ALTER TABLE customers AUTO_INCREMENT = 1;",
                    "ALTER TABLE companies AUTO_INCREMENT = 1;"
            };
            for (String query : queries) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.executeUpdate();
            }

        } catch (SQLException | InterruptedException e) {
            logger.info("DbCleaner - caught exception : " + e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

}
