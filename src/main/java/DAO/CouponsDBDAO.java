package DAO;

import BEANS.CategoryEnum;
import BEANS.Coupon;
import BEANS.CouponException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class CouponsDBDAO implements CouponsDAO {
    private ConnectionPool connectionPool;
    protected static final Logger logger = LogManager.getLogger();

    public CouponsDBDAO() throws SQLException {
        connectionPool = ConnectionPool.getInstance();
        logger.info("CouponsDBDAO constructor");
    }
    /**
     * Adds a new coupon to the database.
     * <p>
     * This method attempts to insert a new coupon into the `coupons` table. It prepares an SQL `INSERT` statement using
     * the provided coupon's details and executes it. If the insertion fails, the method logs an error and throws a
     * `CouponException`. If the insertion is successful, the method does not return any value.
     * <p>
     * The method also handles exceptions that might occur during database access and logs the details of any exceptions
     * thrown. It ensures that the database connection is restored to the connection pool, regardless of whether the
     * operation succeeded or failed.
     *
     * @param coupon the `Coupon` object to be added to the database.
     * @throws CouponException if an error occurs while adding the coupon to the database.
     */
    @Override
    public void addCoupon(Coupon coupon) throws CouponException {
        logger.info("addCoupon -trying to add new coupon - coupon title:{}", coupon.getTitle());
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "INSERT INTO coupons (category_id,title,description,start_date,end_date,amount,price,image,company_id,insert_date) VALUES (?,?,?,?,?,?,?,?,?,current_time());";
            PreparedStatement statement = connection.prepareStatement(query);
            couponToStatement(coupon,statement);
            if (statement.execute()) {
                logger.error("addCoupon - adding coupon faild - coupon title:{}", coupon.getTitle());
                throw new CouponException("addCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("addCoupon threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Updates an existing coupon in the database.
     * <p>
     * This method attempts to update the details of an existing coupon in the `coupons` table. It prepares an SQL `UPDATE`
     * statement using the provided coupon's details and executes it. If the update operation fails, the method logs an
     * error and throws a `CouponException`. If the update is successful, the method does not return any value.
     * <p>
     * The method also handles exceptions that might occur during database access and logs the details of any exceptions
     * thrown. It ensures that the database connection is restored to the connection pool, regardless of whether the
     * operation succeeded or failed.
     *
     * @param coupon the `Coupon` object containing the updated details of the coupon.
     * @throws CouponException if an error occurs while updating the coupon in the database.
     */
    @Override
    public void updateCoupon(Coupon coupon) throws CouponException {
        logger.info("updateCoupon - update an existing coupon - coupon title:{}", coupon.getTitle());
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "UPDATE coupons SET category_id = ?,title = ?,description = ?,start_date = ?,end_date = ?,amount = ?,price = ?,image = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            couponToStatement(coupon,statement);
            if (statement.execute()) {
                logger.error("updateCoupon - updating failed - coupon title:{}", coupon.getTitle());
                throw new CouponException("updateCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("updateCoupon threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Deletes a coupon from the database.
     * <p>
     * This method attempts to delete a coupon from the `coupons` table based on the provided coupon ID. It prepares
     * an SQL `DELETE` statement using the coupon ID and executes it. If the deletion operation fails, the method logs
     * an error and throws a `CouponException`. If the deletion is successful, the method does not return any value.
     * <p>
     * The method also handles exceptions that might occur during database access and logs the details of any exceptions
     * thrown. It ensures that the database connection is restored to the connection pool, regardless of whether the
     * operation succeeded or failed.
     *
     * @param couponID the ID of the coupon to be deleted.
     * @throws CouponException if an error occurs while deleting the coupon from the database.
     */
    @Override
    public void deleteCoupon(int couponID) throws CouponException {
        logger.info("deleteCoupon - delete coupon coupon id:{}", couponID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "DELETE FROM coupons WHERE id =?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, couponID);
            if (statement.execute()) {
                logger.error("deleteCoupon - delete failed - coupon id:{}", couponID);
                throw new CouponException("deleteCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCoupon threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves all coupons from the database.
     * <p>
     * This method queries the `coupons` table to retrieve all coupon records. It prepares and executes an SQL `SELECT`
     * statement to fetch the records, and then processes the result set to create a list of `Coupon` objects. If an
     * error occurs during the database operation, it logs the error and throws a `CouponException`. If successful,
     * it returns an `ArrayList` containing all coupons.
     * <p>
     * The method ensures that the database connection is always restored to the connection pool, even if an exception
     * occurs or the operation completes successfully.
     *
     * @return an `ArrayList` of `Coupon` objects representing all coupons in the database.
     * @throws CouponException if an error occurs while retrieving the coupons from the database.
     */
    @Override
    public ArrayList<Coupon> getAllCoupons() throws CouponException {
        logger.info("getAllCoupons - retrieve all coupons");
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM Coupons;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(resultSetToCoupon(resultSet));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("getAllCoupons threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves a specific coupon from the database based on its ID.
     * <p>
     * This method queries the `coupons` table to retrieve the coupon with the specified ID. It prepares and executes
     * an SQL `SELECT` statement to fetch the record, and then processes the result set to create a `Coupon` object.
     * If no coupon with the given ID is found, it logs the error and throws a `CouponException`. If successful,
     * it returns the `Coupon` object.
     * <p>
     * The method ensures that the database connection is always restored to the connection pool, even if an exception
     * occurs or the operation completes successfully.
     *
     * @param couponID the ID of the coupon to retrieve.
     * @return a `Coupon` object representing the coupon with the specified ID.
     * @throws CouponException if an error occurs while retrieving the coupon or if the coupon is not found.
     */
    @Override
    public Coupon getSelectedCoupon(int couponID) throws CouponException {
        logger.info("getSelectedCoupon - retrieve a specific coupon coupon id:{}", couponID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM coupons WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, couponID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return (resultSetToCoupon(resultSet));
            } else {
                logger.error("getSelectedCoupon - getting coupon failed {}", couponID);
                throw new CouponException("getSelectedCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("getSelectedCoupon threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Records a coupon purchase by a customer and updates the coupon's amount in the database.
     * <p>
     * This method performs two main actions:
     * 1. Inserts a record into the `customers_vs_coupons` table to register the purchase.
     * 2. Updates the `coupons` table to decrement the amount of the purchased coupon.
     * <p>
     * The method first inserts a new purchase record into the `customers_vs_coupons` table. If this operation is
     * successful, it then updates the `coupons` table to decrease the coupon's amount by 1. Both actions are logged.
     * If any error occurs during the process, a `CouponException` is thrown.
     * <p>
     * The method ensures that the database connection is always restored to the pool, even if an exception occurs
     * or the operations complete successfully.
     *
     * @param customerId the ID of the customer making the purchase.
     * @param couponID the ID of the coupon being purchased.
     * @throws CouponException if an error occurs while recording the purchase or updating the coupon's amount.
     */
    @Override
    public void addCouponPurchase(int customerId, int couponID) throws CouponException {
        logger.info("addCouponPurchase - adding coupon purchase - coupon id:{} and customer id:{}", couponID, customerId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "INSERT INTO customers_vs_coupons (customer_id,coupon_id,purchase_date) VALUES (?,?,current_time());";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, customerId);
            statement.setInt(2, couponID);
            if(!statement.execute()){
                logger.info("addCouponPurchase - INSERT INTO customers_vs_coupons - coupon id:{} and customer id:{} - succeeded", couponID, customerId);
                String query2 = "UPDATE coupons SET amount = amount - 1 WHERE id = ? ;";
                statement = connection.prepareStatement(query2);
                Coupon coupon = getSelectedCoupon(couponID);
                // Set the values for the prepared statement to decrease the coupon's amount
                statement.setInt(1, couponID);
                if(!statement.execute())
                    logger.info("addCouponPurchase - UPDATE coupons - coupon id:{} and customer id:{} - succeeded", couponID, customerId);
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("addCouponPurchase threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Deletes a coupon purchase record by a customer and updates the coupon's amount in the database.
     * <p>
     * This method performs two main actions:
     * 1. Deletes the record of the coupon purchase from the `customers_vs_coupons` table.
     * 2. Updates the `coupons` table to increment the coupon's amount by 1.
     * <p>
     * The method first deletes the coupon purchase record from the `customers_vs_coupons` table. If this operation
     * is successful, it then updates the `coupons` table to increase the coupon's amount by 1. Both actions are logged.
     * If any error occurs during the process, a `CouponException` is thrown.
     * <p>
     * The method ensures that the database connection is always restored to the pool, even if an exception occurs
     * or the operations complete successfully.
     *
     * @param customerId the ID of the customer whose coupon purchase record is to be deleted.
     * @param couponID the ID of the coupon for which the purchase record is being deleted.
     * @throws CouponException if an error occurs while deleting the purchase record or updating the coupon's amount.
     */
    @Override
    public void deleteCouponPurchase(int customerId, int couponID) throws CouponException {
        logger.info("deleteCouponPurchase - delete a coupon purchase by customer id:{} and coupon id:{}", customerId, couponID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE customers_vs_coupons WHERE customer_id = ? and coupon_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, customerId);
            statement.setInt(2, couponID);
            if (statement.execute()) {
                logger.error("deleteCouponPurchase failed {} {}", customerId, couponID);
                throw new CouponException("deleteCouponPurchase failed");
            }else {
                logger.info("deleteCouponPurchase - DELETE customers_vs_coupons - coupon id:{} and customer id:{} - succeeded", couponID, customerId);
                String query2 = "UPDATE coupons SET amount = amount - 1 WHERE id = ? ;";
                statement = connection.prepareStatement(query2);
                Coupon coupon = getSelectedCoupon(couponID);
                // Set the values for the prepared statement to increase the coupon's amount
                statement.setInt(1, coupon.getAmount() + 1);
                statement.setInt(2, couponID);
                if (statement.execute()) {
                    logger.error("deleteCouponPurchase - deleting purchase failed{}", couponID);
                    throw new CouponException("deleteCouponPurchase failed");
                }else
                    logger.info("deleteCouponPurchase - UPDATE coupons - coupon id:{} and customer id:{} - succeeded", couponID, customerId);
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCouponPurchase threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ delete by methods ***************************************************/

    /**
     * Deletes all coupon purchase records associated with coupons from a specified company.
     * <p>
     * This method performs the following actions:
     * 1. Deletes all records from the `customers_vs_coupons` table where the `coupon_id` matches any ID
     *    from the `coupons` table that is associated with the given company ID.
     * <p>
     * If the deletion operation fails, a `CouponException` is thrown. The process is logged for tracking purposes.
     * <p>
     * The method ensures that the database connection is always restored to the pool, even if an exception occurs
     * or the operation completes successfully.
     *
     * @param companyId the ID of the company whose associated coupon purchases are to be deleted.
     * @throws CouponException if an error occurs while deleting the coupon purchase records.
     */
    public void deletePurchasesByCompany(int companyId) throws CouponException {
        logger.info("deletePurchasesByCompany - delete coupon purchase by company id:{}", companyId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM customers_vs_coupons WHERE coupon_id IN (SELECT id FROM coupons WHERE company_id = ?);";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, companyId);
            if (statement.execute()) {
                logger.error("deletePurchasesByCompany - deleting purchases failed company id:{}", companyId);
                throw new CouponException("deletePurchasesByCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deletePurchasesByCompany threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Deletes all coupons associated with a specified company.
     * <p>
     * This method performs the following actions:
     * 1. Deletes all records from the `coupons` table where the `company_id` matches the given company ID.
     * <p>
     * If the deletion operation fails, a `CouponException` is thrown. The process is logged for tracking purposes.
     * <p>
     * The method ensures that the database connection is always restored to the pool, even if an exception occurs
     * or the operation completes successfully.
     *
     * @param companyId the ID of the company whose associated coupons are to be deleted.
     * @throws CouponException if an error occurs while deleting the coupons.
     */
    public void deleteCouponsByCompany(int companyId) throws CouponException {
        logger.info("deleteCouponsByCompany - delete coupon by company id:{}", companyId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM coupons WHERE company_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, companyId);
            if (statement.execute()) {
                logger.error("deleteCouponsByCompany - deleting coupons failed company id:{}", companyId);
                throw new CouponException("deleteCouponsByCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCouponsByCompany threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /**
     * Deletes all coupon purchases made by a specified customer and updates the coupon amounts accordingly.
     * <p>
     * This method performs the following actions:
     * 1. Increases the amount of all coupons that the customer has purchased. This is done by updating the `coupons` table
     *    to increment the `amount` of each coupon where the `id` is found in the `customers_vs_coupons` table.
     * 2. Deletes all coupon purchase records for the specified customer from the `customers_vs_coupons` table.
     * <p>
     * If any operation fails, a `CouponException` is thrown. The process is logged for tracking purposes.
     * <p>
     * The method ensures that the database connection is always restored to the pool, even if an exception occurs
     * or the operation completes successfully.
     *
     * @param customerID the ID of the customer whose coupon purchases are to be deleted.
     * @throws CouponException if an error occurs while updating coupon amounts or deleting purchase records.
     */
    public void deletePurchasesByCustomer(int customerID) throws CouponException {
        logger.info("deletePurchasesByCustomer - delete coupon purchase by customer customer id:{}", customerID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "UPDATE coupons SET amount = amount + 1 WHERE id IN (SELECT coupon_id FROM customers_vs_coupons WHERE customer_id=?);";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, customerID);
            if (statement.execute()) {
                logger.error("deletePurchasesByCustomer - update amount failed customer id:{}", customerID);
                throw new CouponException("deletePurchasesByCustomer failed");
            }
            else {
                query1 = "DELETE FROM customers_vs_coupons WHERE customer_id =?;";
                statement = connection.prepareStatement(query1);
                statement.setInt(1, customerID);
                if (statement.execute()){
                    logger.error("deletePurchasesByCustomer - deleting purchases failed customer id:{}", customerID);
                    throw new CouponException("deletePurchasesByCustomer failed");
                }
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deletePurchasesByCustomer threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Deletes all coupon purchases associated with a specified coupon.
     * <p>
     * This method performs the following action:
     * 1. Deletes all records from the `customers_vs_coupons` table where the `coupon_id` matches the specified coupon ID.
     * <p>
     * If the operation fails, a `CouponException` is thrown. The process is logged for tracking purposes.
     * <p>
     * The method ensures that the database connection is always restored to the pool, even if an exception occurs
     * or the operation completes successfully.
     *
     * @param couponID the ID of the coupon whose purchases are to be deleted.
     * @throws CouponException if an error occurs while deleting the purchase records.
     */
    public void deletePurchasesByCoupon(int couponID) throws CouponException {
        logger.info("deletePurchasesByCoupon - delete coupon purchase by coupon coupon id:{}", couponID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM customers_vs_coupons WHERE coupon_id =?;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, couponID);
            if (statement.execute()) {
                logger.error("deletePurchasesByCoupon - deleting purchases failed coupon id:{}",couponID);
                throw new CouponException("deletePurchasesByCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deletePurchasesByCoupon threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Deletes expired coupons from the database.
     * <p>
     * This method performs the following actions:
     * 1. Deletes all records from the `customers_vs_coupons` table where the `coupon_id` corresponds to a coupon with an expiration date less than or equal to the current time.
     * 2. Deletes all expired coupons from the `coupons` table, where the expiration date is less than or equal to the current time.
     * <p>
     * If the deletion fails at any step, a `CouponException` is thrown. The process is logged for tracking purposes.
     * <p>
     * The method ensures that the database connection is always restored to the pool, even if an exception occurs
     * or the operation completes successfully.
     *
     * @throws CouponException if an error occurs while deleting the expired coupons or related purchase records.
     */
    public void deleteCouponsByExpirationDate() throws CouponException {
        logger.info("deleteCouponsByExpirationDate - deletes expired coupons");
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "DELETE FROM customers_vs_coupons WHERE coupon_id in (SELECT id FROM coupons WHERE end_date <= current_time());";
            PreparedStatement statement = connection.prepareStatement(query);
            if (!statement.execute()) {
                query = "DELETE FROM coupons WHERE end_date <= current_time();";
                statement = connection.prepareStatement(query);
                if (statement.execute()) {
                    logger.error("deleteCouponsByExpirationDate - deleting coupons from coupons failed");
                    throw new CouponException("deleteCouponsByExpirationDate failed");
                }
            }else {
                logger.error("deleteCouponsByExpirationDate - deleting coupons from customers_vs_coupons failed");
                throw new CouponException("deleteCouponsByExpirationDate failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCouponsByExpirationDate threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ get by methods ***************************************************/

    /**
     * Retrieves all coupons associated with a specific company from the database.
     * <p>
     * This method executes a SQL query to select all coupons where the `company_id` matches the given company ID.
     * It then iterates over the results and converts each record into a `Coupon` object, which is added to an `ArrayList`.
     * <p>
     * The method handles exceptions related to SQL operations and restores the database connection to the pool
     * even if an exception occurs or the operation completes successfully.
     *
     * @param companyId the ID of the company whose coupons are to be retrieved.
     * @return an `ArrayList` of `Coupon` objects associated with the specified company.
     * @throws CouponException if an error occurs while retrieving the coupons from the database.
     */
    public ArrayList<Coupon> allCouponsByCompany(int companyId) throws CouponException {
        logger.info("allCouponsByCompany - getting all coupons company id:{}", companyId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM Coupons WHERE company_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(resultSetToCoupon(resultSet));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("allCouponsByCompany threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves all coupons for a specific company that belong to a given category.
     * <p>
     * This method executes a SQL query to select coupons from the `coupons` table where the `company_id` matches the
     * specified company ID and the `category_id` matches the given category ID. It iterates over the result set, converts
     * each record into a `Coupon` object, and adds it to an `ArrayList`.
     * <p>
     * The method handles exceptions related to SQL operations and ensures that the database connection is properly
     * restored to the pool, even if an exception occurs or the operation completes successfully.
     *
     * @param companyId the ID of the company whose coupons are to be retrieved.
     * @param categoryId the ID of the category to filter the coupons by.
     * @return an `ArrayList` of `Coupon` objects associated with the specified company and category.
     * @throws CouponException if an error occurs while retrieving the coupons from the database.
     */
    public ArrayList<Coupon> allCouponsByCompanyAndCategory(int companyId, int categoryId) throws CouponException {
        logger.info("allCouponsByCompanyAndCategory - getting all coupons company id:{} and category id:{}", companyId, categoryId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM Coupons WHERE company_id = ? AND category_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, companyId);
            statement.setInt(2, categoryId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(resultSetToCoupon(resultSet));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("allCouponsByCompanyAndCategory threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves all coupons for a specific company that are priced at or below a given maximum price.
     * <p>
     * This method executes a SQL query to select coupons from the `coupons` table where the `company_id` matches the
     * specified company ID and the `price` is less than or equal to the given maximum price. It iterates over the
     * result set, converts each record into a `Coupon` object, and adds it to an `ArrayList`.
     * <p>
     * The method handles exceptions related to SQL operations and ensures that the database connection is properly
     * restored to the pool, even if an exception occurs or the operation completes successfully.
     *
     * @param companyId the ID of the company whose coupons are to be retrieved.
     * @param maxPrice the maximum price of the coupons to be retrieved.
     * @return an `ArrayList` of `Coupon` objects associated with the specified company and within the maximum price.
     * @throws CouponException if an error occurs while retrieving the coupons from the database.
     */
    public ArrayList<Coupon> allCouponsByCompanyAndMaxPrice(int companyId, double maxPrice) throws CouponException {
        logger.info("allCouponsByCompanyAndMaxPrice - getting all coupons company id:{} and max price:{}", companyId, maxPrice);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM Coupons WHERE company_id = ? AND  price <= ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, companyId);
            statement.setDouble(2, maxPrice);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(resultSetToCoupon(resultSet));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("allCouponsByCompanyAndMaxPrice threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves all coupons associated with a specific customer from the database.
     * <p>
     * This method executes a SQL query to join the `customers_vs_coupons` table with the `coupons` table,
     * retrieving all coupons for a given customer ID. It iterates over the results, converts each record into
     * a `Coupon` object, and adds it to an `ArrayList`.
     * <p>
     * The method handles exceptions related to SQL operations and ensures that the database connection is
     * properly restored to the pool, even if an exception occurs or the operation completes successfully.
     *
     * @param customerId the ID of the customer whose coupons are to be retrieved.
     * @return an `ArrayList` of `Coupon` objects associated with the specified customer.
     * @throws CouponException if an error occurs while retrieving the coupons from the database.
     */
    public ArrayList<Coupon> allCouponsByCustomer(int customerId) throws CouponException {
        logger.info("allCouponsByCustomer - getting  all coupons by customer id:{}", customerId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT b.* FROM customers_vs_coupons a JOIN coupons b ON b.id = a.coupon_id WHERE a.customer_id =?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(resultSetToCoupon(resultSet));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("allCouponsByCustomer threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves all coupons purchased by a specific customer that belong to a given category.
     * <p>
     * This method performs a SQL query to select coupons from the `coupons` table that have been purchased by the customer
     * (as indicated by the `customers_vs_coupons` table) and that belong to the specified category. It then converts each result
     * set row into a `Coupon` object and adds it to an `ArrayList`.
     * <p>
     * The method ensures that any exceptions related to SQL operations are caught and handled appropriately. It also guarantees
     * that the database connection is restored to the pool after use, regardless of whether the operation succeeds or fails.
     *
     * @param customerId the ID of the customer whose purchased coupons are to be retrieved.
     * @param categoryId the ID of the category to filter the coupons by.
     * @return an `ArrayList` of `Coupon` objects purchased by the customer that belong to the specified category.
     * @throws CouponException if an error occurs while retrieving the coupons from the database.
     */
    public ArrayList<Coupon> allCouponsByCustomerAndCategory(int customerId, int categoryId) throws CouponException {
        logger.info("allCouponsByCustomerAndCategory - getting  all coupons by customer id:{} and category id:{}", customerId, categoryId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT b.* FROM customers_vs_coupons a JOIN coupons b ON b.id = a.coupon_id WHERE a.customer_id = ? AND b.category_id = ? ;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerId);
            statement.setInt(2, categoryId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(resultSetToCoupon(resultSet));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("allCouponsByCustomerAndCategory threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves all coupons purchased by a specific customer that are priced below or equal to a given maximum price.
     * <p>
     * This method performs a SQL query to select coupons from the `coupons` table that have been purchased by the customer
     * (as indicated by the `customers_vs_coupons` table) and that have a price less than or equal to the specified maximum price.
     * It then converts each result set row into a `Coupon` object and adds it to an `ArrayList`.
     * <p>
     * The method ensures that any exceptions related to SQL operations are caught and handled appropriately. It also guarantees
     * that the database connection is restored to the pool after use, regardless of whether the operation succeeds or fails.
     *
     * @param customerId the ID of the customer whose purchased coupons are to be retrieved.
     * @param maxPrice the maximum price to filter the coupons by.
     * @return an `ArrayList` of `Coupon` objects purchased by the customer that are priced below or equal to the maximum price.
     * @throws CouponException if an error occurs while retrieving the coupons from the database.
     */
    public ArrayList<Coupon> allCouponsByCustomerAndMaxPrice(int customerId, double maxPrice) throws CouponException {
        logger.info("allCouponsByCustomerAndMaxPrice - getting  all coupons by customer id:{} and max price:{}", customerId, maxPrice);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT b.* FROM customers_vs_coupons a JOIN coupons b ON b.id = a.coupon_id WHERE a.customer_id = ? AND b.price <= ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerId);
            statement.setDouble(2, maxPrice);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(resultSetToCoupon(resultSet));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            logger.error("allCouponsByCustomerAndMaxPrice threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ check by methods ***************************************************/

    /**
     * Checks if a specific coupon has been purchased by a customer.
     * <p>
     * Returns `false` if no such purchase exists, and `true` if the coupon has been purchased by the customer or if
     * the coupon is out of stock or expired.
     * <p>
     * This method performs two checks:
     * 1. Verifies if there is a record of the coupon purchase by the customer.
     * 2. If no purchase record is found, checks if the coupon exists, is in stock, and has not expired.
     *
     * @param customerID the ID of the customer.
     * @param couponID the ID of the coupon.
     * @return `true` if the coupon has been purchased or is out of stock/expired, `false` if no purchase record exists.
     * @throws CouponException if an error occurs while querying the database.
     */
    public boolean checkCouponPurchase(int customerID, int couponID) throws CouponException {
        logger.info("checkCouponPurchase - Looking for an existing purchase of the coupon id:{} by the customer id:{}",couponID,customerID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM customers_vs_coupons WHERE customer_id =? AND coupon_id =? ;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerID);
            statement.setInt(2, couponID);
            if (statement.execute()) {
                if (!statement.getResultSet().next()) { // No results found, continue
                    query = "SELECT * FROM Coupons WHERE id = ? AND amount > 0 AND end_date > current_time();";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, couponID);
                    if (statement.execute()) {
                        if (statement.getResultSet().next()) {
                            return false;//no such purchase
                        } else {
                            logger.info("checkCouponPurchase {} - Coupon id:{} is out of stock or expired", couponID);
                            return true;//Coupons are out of stock
                        }
                    }
                } else {
                    logger.info("checkCouponPurchase {} {} - There is an identical purchase for customer id:{} and coupon id:{} ", customerID, couponID);
                    return true;//There is an identical sale
                }
            }
            logger.info("checkCouponPurchase - statement.execute() return false customer id:{} and coupon id:{}", customerID, couponID);
            return true;
        } catch (SQLException | InterruptedException e) {
            logger.error("checkCouponPurchase threw exception {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ resultSetTo methods ***************************************************/

    /**
     * Populates a {@link PreparedStatement} with the values from a {@link Coupon} object.
     * This method is used to either insert a new coupon or update an existing coupon in the database.
     * <p>
     * The method sets the parameters for the SQL query based on the type of operation:
     * <ul>
     *     <li>For an INSERT operation: The ID is not set as it is usually auto-generated by the database.</li>
     *     <li>For an UPDATE operation: The ID is included to specify which record to update.</li>
     * </ul>
     * <p>
     * Parameters are set in the following order:
     * <ol>
     *     <li><b>category_id:</b> The ID of the coupon's category.</li>
     *     <li><b>title:</b> The title of the coupon.</li>
     *     <li><b>description:</b> A brief description of the coupon.</li>
     *     <li><b>start_date:</b> The start date of the coupon's validity.</li>
     *     <li><b>end_date:</b> The end date of the coupon's validity.</li>
     *     <li><b>amount:</b> The quantity of the coupon available.</li>
     *     <li><b>price:</b> The price of the coupon.</li>
     *     <li><b>image:</b> The URL or path to the image associated with the coupon.</li>
     *     <li><b>id:</b> The ID of the coupon, set only if it's an UPDATE operation. For INSERT operations, this parameter is not set.</li>
     * </ol>
     *
     * @param coupon The {@link Coupon} object containing the data to be inserted or updated.
     * @param statement The {@link PreparedStatement} object to be populated with the coupon data.
     * @throws SQLException If an SQL error occurs while setting the parameters.
     */
    public void couponToStatement(Coupon coupon,PreparedStatement statement) throws SQLException {
        logger.info("couponToStatement - converting coupon object into statement - coupon title:{}",coupon.getTitle());
        statement.setInt(1, coupon.getCategory().getId());//category_id
        statement.setString(2, coupon.getTitle());//title
        statement.setString(3, coupon.getDescription());//description
        statement.setDate(4, coupon.getStartDate());//start_date
        statement.setDate(5, coupon.getEndDate());//end_date
        statement.setInt(6, coupon.getAmount());//amount
        statement.setDouble(7, coupon.getPrice());//price
        statement.setString(8, coupon.getImage());//image
        if(coupon.getId()==0)
            statement.setInt(9, coupon.getCompanyId());//insert
        else
            statement.setInt(9, coupon.getId());//update
    }
    /**
     * Converts a {@link ResultSet} object into a {@link Coupon} object.
     * This method maps the columns of the result set to the properties of the Coupon object.
     * <p>
     * The method assumes that the result set contains the following columns in the specified order:
     * <ol>
     *     <li><b>id:</b> The unique identifier for the coupon.</li>
     *     <li><b>companyId:</b> The ID of the company that issued the coupon.</li>
     *     <li><b>category:</b> The category of the coupon, converted from an integer ID to a {@link CategoryEnum}.</li>
     *     <li><b>title:</b> The title of the coupon.</li>
     *     <li><b>description:</b> A description of the coupon.</li>
     *     <li><b>startDate:</b> The start date of the coupon's validity.</li>
     *     <li><b>endDate:</b> The end date of the coupon's validity.</li>
     *     <li><b>amount:</b> The quantity of the coupon available.</li>
     *     <li><b>price:</b> The price of the coupon.</li>
     *     <li><b>image:</b> The URL or path to the image associated with the coupon.</li>
     * </ol>
     *
     * @param resultSet The {@link ResultSet} object containing the coupon data from the database.
     * @return A {@link Coupon} object populated with data from the result set.
     * @throws SQLException If an SQL error occurs while accessing the result set.
     */
    public Coupon resultSetToCoupon(ResultSet resultSet) throws SQLException {
        logger.info("resultSetToCoupon - converting resultSet object into Coupon - coupon id:{}",resultSet.getInt(1));
        return new Coupon(
                resultSet.getInt(1),//id
                resultSet.getInt(2),//int companyId,
                CategoryEnum.fromId(resultSet.getInt(3)),// BEANS.CategoryEnum category,
                resultSet.getString(4),// String title,
                resultSet.getString(5),// String description,
                resultSet.getDate(6),// Date startDate,
                resultSet.getDate(7),// Date endDate,
                resultSet.getInt(8),// int amount,
                resultSet.getDouble(9),// Double price,
                resultSet.getString(10)// String image
        );
    }
///
}