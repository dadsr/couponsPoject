package DAO;

import BEANS.CategoryEnum;
import BEANS.Coupon;
import BEANS.CouponException;

import java.sql.*;
import java.util.ArrayList;

public class CouponsDBDAO implements CouponsDAO {
    private ConnectionPool connectionPool;

    public CouponsDBDAO() throws SQLException {
        connectionPool = ConnectionPool.getInstance();
    }


    @Override
    // Method to add a new coupon to the database
    public void addCoupon(Coupon coupon) throws CouponException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "INSERT INTO coupons (company_id,category_id,title,description,start_date,end_date,amount,price,image,insert_date) VALUES (?,?,?,?,?,?,?,?,?,current_time());";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, coupon.getCompanyId());//company_id
            statement.setInt(2, coupon.getCategory().getId());//category_id
            statement.setString(3, coupon.getTitle());//title
            statement.setString(4, coupon.getDescription());//description
            statement.setDate(5, coupon.getStartDate());//start_date
            statement.setDate(6, coupon.getEndDate());//end_date
            statement.setInt(7, coupon.getAmount());//amount
            statement.setDouble(8, coupon.getPrice());//price
            statement.setString(9, coupon.getImage());//image
            if (statement.execute())
                throw new CouponException("addCoupon failed");
        } catch (InterruptedException | SQLException e) {
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
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "UPDATE coupons SET category_id = ?,title = ?,description = ?,start_date = ?,end_date = ?,amount = ?,price = ?,image = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, coupon.getCategoryId());
            statement.setString(2, coupon.getTitle());
            statement.setString(3, coupon.getDescription());
            statement.setDate(4, coupon.getStartDate());
            statement.setDate(5, coupon.getEndDate());
            statement.setInt(6, coupon.getAmount());
            statement.setDouble(7, coupon.getPrice());
            statement.setString(8, coupon.getImage());
            statement.setInt(9, coupon.getId());
            if (statement.execute())
                throw new CouponException("updateCoupon failed");
        } catch (InterruptedException | SQLException e) {
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
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "DELETE FROM coupons WHERE id =?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, couponID);
            if (statement.execute())
                throw new CouponException("deleteCoupon failed");
        } catch (InterruptedException | SQLException e) {
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
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM Coupons;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(new Coupon(
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
                ));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
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
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM coupons WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, couponID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return (new Coupon(
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
                ));
            } else
                throw new CouponException("getSelectedCoupon failed");
        } catch (InterruptedException | SQLException e) {
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
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "INSERT INTO customers_vs_coupons (customer_id,coupon_id,purchase_date) VALUES (?,?,current_time());";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, customerId);
            statement.setInt(2, couponID);
            if (statement.execute())
                throw new CouponException("addCouponPurchase failed");
            else {
                String query2 = "UPDATE coupons SET amount = amount - 1 WHERE id = ? ;";
                statement = connection.prepareStatement(query2);
                Coupon coupon = getSelectedCoupon(couponID);
                // Set the values for the prepared statement to decrease the coupon's amount
                statement.setInt(1, couponID);
                if (statement.execute())
                    throw new CouponException("addCouponPurchase failed");
            }
        } catch (InterruptedException | SQLException e) {
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
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE customers_vs_coupons WHERE customer_id = ? and coupon_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, customerId);
            statement.setInt(2, couponID);
            if (statement.execute())
                throw new CouponException("deleteCouponPurchase failed");
            else {
                String query2 = "UPDATE coupons SET amount = amount - 1 WHERE id = ? ;";
                statement = connection.prepareStatement(query2);
                Coupon coupon = getSelectedCoupon(couponID);
                // Set the values for the prepared statement to increase the coupon's amount
                statement.setInt(1, coupon.getAmount() + 1);
                statement.setInt(2, couponID);

                if (statement.execute())
                    throw new CouponException("deleteCouponPurchase failed");
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ delete by methods ***************************************************/
    public void deletePurchasesByCompany(int companyId) throws CouponException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM customers_vs_coupons WHERE coupon_id IN (SELECT id FROM coupons WHERE company_id = ?);";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, companyId);
            if (statement.execute())
                throw new CouponException("deletePurchasesByCompany failed");
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    public void deleteCouponsByCompany(int companyId) throws CouponException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM coupons WHERE company_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, companyId);
            if (statement.execute())
                throw new CouponException("deleteCouponsByCompany failed");
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    public void deletePurchasesByCustomer(int customerID) throws CouponException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "UPDATE coupons SET amount = amount + 1 WHERE id IN (SELECT coupon_id FROM customers_vs_coupons WHERE customer_id=?);";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, customerID);
            if (statement.execute())
                throw new CouponException("deletePurchasesByCustomer failed");
            else {
                query1 = "DELETE FROM customers_vs_coupons WHERE customer_id =?;";
                statement = connection.prepareStatement(query1);
                statement.setInt(1, customerID);
                if (statement.execute())
                    throw new CouponException("deletePurchasesByCustomer failed");
            }
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    public void deletePurchasesByCoupon(int couponID) throws CouponException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query1 = "DELETE FROM customers_vs_coupons WHERE coupon_id =?;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.setInt(1, couponID);
            if (statement.execute())
                throw new CouponException("deletePurchasesByCustomer failed");

        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    public void deleteCouponsByExpirationDate() throws CouponException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "DELETE FROM customers_vs_coupons WHERE coupon_id in (SELECT id FROM coupons WHERE end_date <= current_time());";
            PreparedStatement statement = connection.prepareStatement(query);
            if (!statement.execute()) {
                query = "DELETE FROM coupons WHERE end_date <= current_time();";
                statement = connection.prepareStatement(query);
                if (!statement.execute())
                    throw new CouponException("deleteCouponsByExpirationDate failed");
            }else
                throw new CouponException("deleteCouponsByExpirationDate failed");

        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ get by methods ***************************************************/
    public ArrayList<Coupon> allCouponsByCompany(int companyId) throws CouponException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM Coupons WHERE company_id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(new Coupon(
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
                ));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCompanyAndCategory(int companyId, int categoryId) throws CouponException {
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
                Coupons.add(new Coupon(
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
                ));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCompanyAndMaxPrice(int companyId, double maxPrice) throws CouponException {
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
                Coupons.add(new Coupon(
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
                ));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    public ArrayList<Coupon> allCouponsByCustomer(int customerId) throws CouponException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT b.* FROM customers_vs_coupons a JOIN coupons b ON b.id = a.coupon_id WHERE a.customer_id =?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Coupon> Coupons = new ArrayList<>();
            while (resultSet.next()) {
                Coupons.add(new Coupon(
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
                ));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCustomerAndCategory(int customerId, int categoryId) throws CouponException {
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
                Coupons.add(new Coupon(
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
                ));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public ArrayList<Coupon> allCouponsByCustomerAndMaxPrice(int customerId, double maxPrice) throws CouponException {
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
                Coupons.add(new Coupon(
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
                ));
            }
            return Coupons;
        } catch (InterruptedException | SQLException e) {
            throw new CouponException(e.getMessage());
        } finally {
            // Always restore the connection to the pool in the finally block
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ check by methods ***************************************************/
    public boolean checkCouponPurchase(int customerID, int couponID) {
        try {
            Connection connection = connectionPool.getConnection();
            String query = "SELECT * FROM customers_vs_coupons WHERE customer_id =? AND coupon_id =? ;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerID);
            statement.setInt(2, couponID);
            if (!statement.execute()) {
                query = "SELECT * FROM Coupons WHERE id = ? AND amount > 0 AND end_date > current_time();";
                statement = connection.prepareStatement(query);
                statement.setInt(1, couponID);
                return (!statement.execute());

            } else
                return false;
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

