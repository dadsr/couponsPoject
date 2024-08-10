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
        logger.info("CouponsDBDAO");
    }

    @Override
    // Method to add a new coupon to the database
    public void addCoupon(Coupon coupon) throws CouponException {
        logger.info("addCoupon {}", coupon.getTitle());
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "INSERT INTO coupons (category_id,title,description,start_date,end_date,amount,price,image,company_id,insert_date) VALUES (?,?,?,?,?,?,?,?,?,current_time());";
            PreparedStatement statement = connection.prepareStatement(query);
            couponToStatement(coupon,statement);
            if (statement.execute()) {
                logger.error("addCoupon - adding failed {}", coupon.getTitle());
                throw new CouponException("addCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("addCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    @Override
    // Method to update an existing coupon in the database
    public void updateCoupon(Coupon coupon) throws CouponException {
        logger.info("updateCoupon {}", coupon.getTitle());
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "UPDATE coupons SET category_id = ?,title = ?,description = ?,start_date = ?,end_date = ?,amount = ?,price = ?,image = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            couponToStatement(coupon,statement);
            if (statement.execute()) {
                logger.error("updateCoupon - updating failed {}", coupon.getTitle());
                throw new CouponException("updateCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("updateCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    @Override
    // Method to delete a coupon from the database
    public void deleteCoupon(int couponID) throws CouponException {
        logger.info("deleteCoupon {}", couponID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "DELETE FROM coupons WHERE id =?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, couponID);
            if (statement.execute()) {
                logger.error("deleteCoupon - delete failed {}", couponID);
                throw new CouponException("deleteCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    @Override
    // Method to retrieve all coupons from the database
    public ArrayList<Coupon> getAllCoupons() throws CouponException {
        logger.info("getAllCoupons");
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
            logger.error("getAllCoupons {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }

    }
    @Override
    // Method to retrieve a specific coupon from the database by its ID
    public Coupon getSelectedCoupon(int couponID) throws CouponException {
        logger.info("getSelectedCoupon {}", couponID);
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
            logger.error("getSelectedCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    @Override
    // Method to record a coupon purchase by a customer and update the coupon's amount
    public void addCouponPurchase(int customerId, int couponID) throws CouponException {
        logger.info("addCouponPurchase {} {}", couponID, customerId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "INSERT INTO customers_vs_coupons (customer_id,coupon_id,purchase_date) VALUES (?,?,current_time());";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, customerId);
            statement.setInt(2, couponID);
            if(!statement.execute()){
                String query2 = "UPDATE coupons SET amount = amount - 1 WHERE id = ? ;";
                statement = connection.prepareStatement(query2);
                Coupon coupon = getSelectedCoupon(couponID);
                // Set the values for the prepared statement to decrease the coupon's amount
                statement.setInt(1, couponID);
                if(!statement.execute())
                    logger.info("addCouponPurchase {} {} - success", couponID, customerId);
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("addCouponPurchase {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    @Override
    // Method to delete a coupon purchase by a customer and update the coupon's amount
    public void deleteCouponPurchase(int customerId, int couponID) throws CouponException {
        logger.info("deleteCouponPurchase {} {}", customerId, couponID);
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
                String query2 = "UPDATE coupons SET amount = amount - 1 WHERE id = ? ;";
                statement = connection.prepareStatement(query2);
                Coupon coupon = getSelectedCoupon(couponID);
                // Set the values for the prepared statement to increase the coupon's amount
                statement.setInt(1, coupon.getAmount() + 1);
                statement.setInt(2, couponID);

                if (statement.execute()) {
                    logger.error("deleteCouponPurchase - deleting purchase failed{}", couponID);
                    throw new CouponException("deleteCouponPurchase failed");
                }
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCouponPurchase {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /************************************ delete by methods ***************************************************/
    public void deletePurchasesByCompany(int companyId) throws CouponException {
        logger.info("deletePurchasesByCompany {}", companyId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM customers_vs_coupons WHERE coupon_id IN (SELECT id FROM coupons WHERE company_id = ?);";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, companyId);
            if (statement.execute()) {
                logger.error("deletePurchasesByCompany - deleting purchases failed {}", companyId);
                throw new CouponException("deletePurchasesByCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deletePurchasesByCompany {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public void deleteCouponsByCompany(int companyId) throws CouponException {
        logger.info("deleteCouponsByCompany {}", companyId);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM coupons WHERE company_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, companyId);
            if (statement.execute()) {
                logger.error("deleteCouponsByCompany - deleting coupons failed {}", companyId);
                throw new CouponException("deleteCouponsByCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCouponsByCompany {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public void deletePurchasesByCustomer(int customerID) throws CouponException {
        logger.info("deletePurchasesByCustomer {}", customerID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "UPDATE coupons SET amount = amount + 1 WHERE id IN (SELECT coupon_id FROM customers_vs_coupons WHERE customer_id=?);";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, customerID);
            if (statement.execute()) {
                logger.error("deletePurchasesByCustomer - update amount failed {}", customerID);
                throw new CouponException("deletePurchasesByCustomer failed");
            }
            else {
                query1 = "DELETE FROM customers_vs_coupons WHERE customer_id =?;";
                statement = connection.prepareStatement(query1);
                statement.setInt(1, customerID);
                if (statement.execute()){
                    logger.error("deletePurchasesByCustomer - deleting purchases failed {}", customerID);
                    throw new CouponException("deletePurchasesByCustomer failed");
                }
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deletePurchasesByCustomer {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public void deletePurchasesByCoupon(int couponID) throws CouponException {
        logger.info("deletePurchasesByCoupon {}", couponID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM customers_vs_coupons WHERE coupon_id =?;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, couponID);
            if (statement.execute()) {
                logger.error("deletePurchasesByCoupon - deleting purchases failed"+couponID);
                throw new CouponException("deletePurchasesByCoupon failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deletePurchasesByCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public void deleteCouponsByExpirationDate() throws CouponException {
        logger.info("deleteCouponsByExpirationDate");
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
            logger.error("deleteCouponsByExpirationDate {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /************************************ get by methods ***************************************************/
    public ArrayList<Coupon> allCouponsByCompany(int companyId) throws CouponException {
        logger.info("allCouponsByCompany {}", companyId);
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
            logger.error("allCouponsByCompany {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCompanyAndCategory(int companyId, int categoryId) throws CouponException {
        logger.info("allCouponsByCompanyAndCategory {} {}", companyId, categoryId);
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
            logger.error("allCouponsByCompanyAndCategory {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCompanyAndMaxPrice(int companyId, double maxPrice) throws CouponException {
        logger.info("allCouponsByCompanyAndMaxPrice {} {}", companyId, maxPrice);
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
            logger.error("allCouponsByCompanyAndMaxPrice {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCustomer(int customerId) throws CouponException {
        logger.info("allCouponsByCustomer {}", customerId);
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
            logger.error("allCouponsByCustomer {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCustomerAndCategory(int customerId, int categoryId) throws CouponException {
        logger.info("allCouponsByCustomerAndCategory {} {}", customerId, categoryId);
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
            logger.error("allCouponsByCustomerAndCategory {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCustomerAndMaxPrice(int customerId, double maxPrice) throws CouponException {
        logger.info("allCouponsByCustomerAndMaxPrice {} {}", customerId, maxPrice);
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
            logger.error("allCouponsByCustomerAndMaxPrice {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ check by methods ***************************************************/

    /**
    * return false when no such purchase
    * */
    public boolean checkCouponPurchase(int customerID, int couponID) throws CouponException {
        logger.info("checkCouponPurchase {} {}", customerID, couponID);
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
                            logger.info("checkCouponPurchase {} - Coupons are out of stock ", couponID);
                            return true;//Coupons are out of stock
                        }
                    }
                } else {
                    logger.info("checkCouponPurchase {} {} - There is an identical sale ", customerID, couponID);
                    return true;//There is an identical sale
                }
            }
            logger.info("checkCouponPurchase {} {} - false", customerID, couponID);
            return false;
        } catch (SQLException | InterruptedException e) {
            logger.error("checkCouponPurchase {}", e.getMessage());
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /************************************ resultSetTo methods ***************************************************/
    public void couponToStatement(Coupon coupon,PreparedStatement statement) throws SQLException {


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
    public Coupon resultSetToCoupon(ResultSet resultSet) throws SQLException {
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