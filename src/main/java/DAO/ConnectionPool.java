package DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The DAO.ConnectionPool class manages a pool of database connections.
 * This implementation ensures a maximum of 20 concurrent connections.
 */
public class ConnectionPool {
    private final int MAX_CONNECTIONS =20;
    private final String URL ="jdbc:mysql://localhost:3306/coupons_project";
    private final String USER ="root";
    private final String PASSWORD ="admin";
    private List<Connection> connections = new ArrayList<>(MAX_CONNECTIONS);
    private static ConnectionPool instance;
    protected static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());

    /**
     * Private constructor to initialize the connection pool.
     * Creates MAX_CONNECTIONS number of connections and adds them to the pool.
     *
     * @throws SQLException if a database access error occurs
     */
    private ConnectionPool() throws SQLException {
        logger.info("ConnectionPool");
        for (int i = 0; i <MAX_CONNECTIONS ; i++)
            connections.add(DriverManager.getConnection(URL, USER, PASSWORD));
    }
    /**
     * Returns the singleton instance of the DAO.ConnectionPool.
     * @return the singleton instance of the DAO.ConnectionPool.
     * @throws SQLException if a database access error occurs.
     */
    public static ConnectionPool getInstance() throws SQLException {
        logger.info("getInstance");
        if(instance == null)
            instance = new ConnectionPool();
        return instance;
    }
    /**
     * Retrieves a connection from the pool.
     * Waits if no connection is available.
     * @return a database connection.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    public synchronized Connection getConnection() throws InterruptedException {
        logger.info("getConnection{}", connections.size());
        while (connections.size()==0)
                wait();
        Connection connection =connections.get(connections.size()-1);
        connections.remove(connection);
        return connection;
    }
    /**
     * Restores a connection to the pool.
     * @param connection the connection to be restored.
     */
    public synchronized void restoreConnection (Connection connection){
        logger.info("restoreConnection{}", connections.size());
        connections.add(connection);
        notifyAll();
    }
    /**
     * Closes all connections in the pool.
     * @throws SQLException if a database access error occurs.
     */
    public void closeAllConnections () throws SQLException {
        logger.info("closeAllConnections");
        for (Connection connection : connections) {
            connection.close();
        }
        connections.clear();
    }
}
