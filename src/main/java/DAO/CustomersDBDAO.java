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
        logger.info("CustomersDBDAO constructor");
    }
    /**
     * Checks if a customer exists in the database based on the provided email and password.
     * <p>
     * This method queries the `customers` table to find a record matching the provided email and password.
     * If a matching record is found, it returns the customer's ID. If no matching record is found, it returns 0.
     *
     * @param email The email of the customer to check.
     * @param password The password of the customer to check.
     * @return The ID of the customer if a match is found, or 0 if no match is found.
     * @throws CustomerException If an error occurs while querying the database.
     */
    @Override
    public int isCustomerExists(String email, String password) throws CustomerException {
        logger.info("isCustomerExists - Checks whether a customer exists by email:{} and password:{}",email,password);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers WHERE email =? and password =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1,email);
            statement.setString(2,password);
            ResultSet resultSet =statement.executeQuery();
            if (resultSet.next()) {
                logger.info("isCustomerExists - succeeded - customer id:{}", resultSet.getInt(1));
                return resultSet.getInt(1);//customer id
            }
            logger.info("isCustomerExists - failed - email:{} and password:{}",email,password);
            return 0;//false

        } catch (InterruptedException | SQLException e) {
            logger.error("isCustomerExists threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Adds a new customer to the database.
     * <p>
     * This method inserts a new customer record into the `customers` table using the provided
     * customer information. The `insert_date` field is automatically set to the current time.
     * If the insertion fails, a `CustomerException` is thrown.
     *
     * @param customer The customer object containing the details to be added to the database.
     * @throws CustomerException If an error occurs while adding the customer to the database.
     */
    @Override
    public void addCustomer(Customer customer) throws CustomerException {
        logger.info("addCustomer - trying to add new customer customer name:{} {}", customer.getFirstName(), customer.getLastName());
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="INSERT INTO customers (first_name,last_name,email,password,insert_date) VALUES (?,?,?,?,current_time());";
            PreparedStatement statement =connection.prepareStatement(query);
            customerToStatement(customer,statement);
            if(statement.execute()) {
                logger.error("addCustomer - adding customer failed customer name:{} {}", customer.getFirstName(), customer.getLastName());
                throw new CustomerException("addCustomer failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("addCustomer threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Updates an existing customer record in the database.
     * <p>
     * This method updates the details of an existing customer in the `customers` table using
     * the provided customer information. The `id` of the customer is used to identify which
     * record to update. If the update operation fails, a `CustomerException` is thrown.
     *
     * @param customer The customer object containing the updated details to be applied to the database record.
     * @throws CustomerException If an error occurs while updating the customer record in the database.
     */
    @Override
    public void updateCustomer(Customer customer) throws CustomerException {
        logger.info("updateCustomer - trying to update customer customer name:{} {}", customer.getFirstName(), customer.getLastName());
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="UPDATE customers SET first_name = ?,last_name = ?,email = ?,password =? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            customerToStatement(customer,statement);
            if(statement.execute()) {
                logger.error("updateCustomer - updating customer failed customer name:{} {}", customer.getFirstName(), customer.getLastName());
                throw new CustomerException("updateCustomer failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("updateCustomer threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Deletes a customer record from the database based on the customer ID.
     * <p>
     * This method removes the customer record from the `customers` table using the
     * provided customer ID. If the deletion operation fails, a `CustomerException` is thrown.
     *
     * @param customerID The ID of the customer to be deleted from the database.
     * @throws CustomerException If an error occurs while deleting the customer record from the database.
     */
    @Override
    public void deleteCustomer(int customerID) throws CustomerException {
        logger.info("deleteCustomer - trying to delete customer - customer id:{}", customerID);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="DELETE FROM customers WHERE id =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, customerID);
            if(statement.execute()) {
                logger.error("deleteCustomer - delete failed - customer id:{}", customerID);
                throw new CustomerException("deleteCustomer failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCustomer threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves all customer records from the database.
     * <p>
     * This method executes a query to fetch all records from the `customers` table.
     * Each record is converted to a `Customer` object and added to an `ArrayList<Customer>`,
     * which is then returned. If an error occurs while retrieving the records, a
     * `CustomerException` is thrown.
     *
     * @return An `ArrayList` containing all `Customer` objects retrieved from the database.
     * @throws CustomerException If an error occurs while accessing the database or retrieving the customer records.
     */
    @Override
    public ArrayList<Customer> getAllCustomers() throws CustomerException {
        logger.info("getAllCustomers - retrieves all customer records");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers;";
            PreparedStatement statement =connection.prepareStatement(query);
            ResultSet resultSet =statement.executeQuery();
            ArrayList<Customer> customers =new ArrayList<>();
            while (resultSet.next()){
                customers.add(resultSetToCustomer(resultSet));
            }
            return customers;
        } catch (InterruptedException | SQLException e) {
            logger.error("getAllCustomers threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves a specific customer record from the database based on the provided customer ID.
     * <p>
     * This method executes a query to fetch a record from the `customers` table where the
     * `id` matches the provided `customerID`. If a matching record is found, it is converted
     * to a `Customer` object and returned. If no record is found, a `CustomerException` is thrown.
     *
     * @param customerID The ID of the customer to be retrieved.
     * @return The `Customer` object corresponding to the provided ID.
     * @throws CustomerException If an error occurs while accessing the database or if no customer is found with the provided ID.
     */
    @Override
    public Customer getSelectedCustomer(int customerID) throws CustomerException {
        logger.info("getSelectedCustomer - Retrieves a specific customer by customer id:{}", customerID);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers WHERE id = ?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, customerID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSetToCustomer(resultSet);
            }else {
                logger.error("getSelectedCustomer - getting customer failed customer id:{}", customerID);
                throw new CustomerException("getSelectedCustomer failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("getSelectedCustomer threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves all coupons associated with a specific customer from the database.
     * <p>
     * This method executes a query to fetch all coupons that have been purchased by the
     * customer with the given `customerID`. It performs a join between the `customers_vs_coupons`
     * and `coupons` tables to get the relevant coupon details. The retrieved coupons are then
     * converted to `Coupon` objects and added to an `ArrayList`, which is returned to the caller.
     *
     * @param customerID The ID of the customer whose coupons are to be retrieved.
     * @return An `ArrayList` of `Coupon` objects associated with the specified customer.
     * @throws CustomerException If an error occurs while accessing the database.
     */
    private ArrayList<Coupon> getAllCouponsByCustomer(int customerID) throws CustomerException {
        logger.info("getAllCouponsByCustomer -Retrieves all coupons by customer id:{}", customerID);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT b.* FROM customers_vs_coupons a JOIN coupons b on b.id = a.coupon_id WHERE a.customer_id = ?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, customerID);
            ResultSet resultSet =statement.executeQuery();
            ArrayList<Coupon> coupons =new ArrayList<>();
            while (resultSet.next()){
                coupons.add(new CouponsDBDAO().resultSetToCoupon(resultSet));
            }
            return coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("getAllCouponsByCustomer threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ check by methods ***************************************************/
    /**
     * Checks if a customer with the specified email address exists in the database.
     * <p>
     * This method executes a query to search for a customer record that matches the given
     * email address. If a record is found, the method returns `true`, indicating that a
     * customer with that email exists. If no record is found, it returns `false`.
     *
     * @param email The email address of the customer to check.
     * @return `true` if a customer with the specified email exists, otherwise `false`.
     * @throws CustomerException If an error occurs while accessing the database.
     */
    public boolean isCustomerExists(String email) throws CustomerException {
        logger.info("isCustomerExists - Checks if a customer exists with the email:{}", email);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers WHERE email =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1,email);
            ResultSet resultSet =statement.executeQuery();
            return (resultSet.next());
        } catch (InterruptedException | SQLException e) {
            logger.error("isCustomerExists threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /******************************************************************************************************** */

    /**
     * Retrieves a customer record from the database based on the provided email and password.
     * <p>
     * This method performs a query to find a customer whose email and password match the
     * provided values. If a matching customer is found, the method returns a `Customer` object
     * representing that customer. If no matching customer is found, it throws a `CustomerException`.
     *
     * @param email The email address of the customer to retrieve.
     * @param password The password of the customer to retrieve.
     * @return A `Customer` object representing the customer with the specified email and password.
     * @throws CustomerException If an error occurs while accessing the database or if no customer
     *         is found with the given email and password.
     */
    public Customer getSelectedCustomer(String email, String password) throws CustomerException {
        logger.info("getSelectedCustomer - Retrieves a customer bu email:{}", email);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM customers WHERE  email = ? AND password = ? ;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSetToCustomer(resultSet);
            }else {
                logger.error("getSelectedCustomer - getting customer failed{}", email);
                throw new CustomerException("getSelectedCustomer failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("getSelectedCustomer  threw exception {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /************************************ resultSetTo methods ***************************************************/

    /**
     * Sets the parameters of a `PreparedStatement` using the values from a `Customer` object.
     * <p>
     * This method populates the `PreparedStatement` with values from the provided `Customer` object.
     * The order and type of parameters are based on the expected SQL query. If the `Customer` object
     * has a positive ID, it is set as the last parameter in the statement.
     *
     * @param customer The `Customer` object whose values will be used to populate the `PreparedStatement`.
     * @param statement The `PreparedStatement` to be populated with customer data.
     * @throws SQLException If an SQL error occurs while setting the parameters.
     */
    public void customerToStatement (Customer customer,PreparedStatement statement) throws SQLException {
        logger.info("customerToStatement - converting customer object into statement - customer name:{} {}",customer.getFirstName(),customer.getLastName());
        statement.setString(1, customer.getFirstName());
        statement.setString(2, customer.getLastName());
        statement.setString(3, customer.getEmail());
        statement.setString(4,customer.getPassword());
        if(customer.getId()>0)
            statement.setInt(5, customer.getId());
    }

    /**
     * Converts a `ResultSet` into a `Customer` object.
     * <p>
     * This method extracts customer data from the given `ResultSet` and constructs a `Customer` object.
     * It also retrieves the list of coupons associated with the customer using their ID.
     *
     * @param resultSet The `ResultSet` containing customer data.
     * @return A `Customer` object populated with the data from the `ResultSet`.
     * @throws SQLException If an SQL error occurs while retrieving data from the `ResultSet`.
     * @throws CustomerException If an error occurs while retrieving the customer's coupons.
     */
    public Customer resultSetToCustomer (ResultSet resultSet) throws SQLException, CustomerException {
        logger.info("resultSetToCustomer - converting resultSet object into customer - customer id:{}",resultSet.getInt(1));
        return new Customer(resultSet.getInt(1),//int id
                resultSet.getString(2),// String firstName
                resultSet.getString(3),//String lastName
                resultSet.getString(4),//String email
                resultSet.getString(5),//String password
                getAllCouponsByCustomer(resultSet.getInt(1)));//ArrayList<BEANS.Coupon> coupons
    }
//
}