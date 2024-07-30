package DAO;

import BEANS.CategoryEnum;
import BEANS.Coupon;
import BEANS.Customer;
import BEANS.CustomerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class CustomersDBDAO implements CustomersDAO {
    private ConnectionPool connectionPool;
    protected static final Logger logger = LogManager.getLogger();

    public CustomersDBDAO() throws SQLException {
        connectionPool = ConnectionPool.getInstance();
        logger.info("CustomersDBDAO");
    }

    @Override
    public int isCustomerExists(String email, String password) throws CustomerException {
        logger.info("isCustomerExists");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers WHERE email =? and password =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1,email);
            statement.setString(2,password);
            ResultSet resultSet =statement.executeQuery();
            if (resultSet.next())
                return resultSet.getInt(1);//customer id
            return 0;//false

        } catch (InterruptedException | SQLException e) {
            logger.error("isCustomerExists {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void addCustomer(Customer customer) throws CustomerException {
        logger.info("addCustomer");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="INSERT INTO customers (first_name,last_name,email,password,insert_date) VALUES (?,?,?,?,current_time());";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4,customer.getPassword());
            if(statement.execute())
                logger.error("addCustomer - add failed");
                throw new CustomerException("addCustomer failed");
        } catch (InterruptedException | SQLException e) {
            logger.error("addCustomer {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerException {
        logger.info("updateCustomer");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="UPDATE customers SET first_name = ?,last_name = ?,email = ?,password =? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4,customer.getPassword());
            statement.setInt(5,customer.getId());

            if(statement.execute())
                logger.error("updateCustomer - update failed");
                throw new CustomerException("updateCustomer failed");
        } catch (InterruptedException | SQLException e) {
            logger.error("updateCustomer {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }

    }

    @Override
    public void deleteCustomer(int customerID) throws CustomerException {
        logger.info("deleteCustomer");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="DELETE FROM customers WHERE id =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, customerID);
            if(statement.execute()) {
                logger.error("deleteCustomer - delete failed");
                throw new CustomerException("deleteCustomer failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCustomer {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }

    }

    @Override
    public ArrayList<Customer> getAllCustomers() throws CustomerException {
        logger.info("getAllCustomers");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers;";
            PreparedStatement statement =connection.prepareStatement(query);
            ResultSet resultSet =statement.executeQuery();
            ArrayList<Customer> customers =new ArrayList<>();
            while (resultSet.next()){
                customers.add(new Customer(resultSet.getInt(1),//int id
                        resultSet.getString(2),// String firstName
                        resultSet.getString(3),//String lastName
                        resultSet.getString(4),//String email
                        resultSet.getString(5),//String password
                        getAllCouponsByCustomer(resultSet.getInt(1))));//ArrayList<BEANS.Coupon> coupons
            }
            return customers;
        } catch (InterruptedException | SQLException e) {
            logger.error("getAllCustomers {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public Customer getSelectedCustomer(int customerID) throws CustomerException {
        logger.info("getSelectedCustomer");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers WHERE id = ?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, customerID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new Customer(resultSet.getInt(1),//int id
                        resultSet.getString(2),// String firstName
                        resultSet.getString(3),//String lastName
                        resultSet.getString(4),//String email
                        resultSet.getString(5),//String password
                        getAllCouponsByCustomer(customerID));//ArrayList<BEANS.Coupon> coupons
            }else {
                logger.error("getSelectedCustomer - geting customer failed");
                throw new CustomerException("getSelectedCustomer failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("getSelectedCustomer {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    private ArrayList<Coupon> getAllCouponsByCustomer(int customerID) throws CustomerException {
        logger.info("getAllCouponsByCustomer");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT b.* FROM customers_vs_coupons a JOIN coupons b on b.id = a.coupon_id WHERE a.customer_id = ?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, customerID);
            ResultSet resultSet =statement.executeQuery();
            ArrayList<Coupon> coupons =new ArrayList<>();
            while (resultSet.next()){
                coupons.add(new Coupon(resultSet.getInt(1),//int id
                        resultSet.getInt(2),// int companyId
                        CategoryEnum.fromId(resultSet.getInt(3)),// BEANS.CategoryEnum category
                        resultSet.getString(4),// String title
                        resultSet.getString(5),// String description
                        resultSet.getDate(6),// Date startDate
                        resultSet.getDate(7),// Date endDate
                        resultSet.getInt(8),// int amount
                        resultSet.getDouble(9),// Double price
                        resultSet.getString(10)// String image);
                ));
            }
            return coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("getAllCouponsByCustomer {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }

    }

    /************************************ check by methods ***************************************************/
    public boolean isCustomerExists(String email) throws CustomerException {
        logger.info("isCustomerExists");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers WHERE email =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1,email);
            ResultSet resultSet =statement.executeQuery();
            return (resultSet.next());
        } catch (InterruptedException | SQLException e) {
            logger.error("isCustomerExists {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

}
