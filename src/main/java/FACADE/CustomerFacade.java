package FACADE;

import BEANS.*;
import BEANS.CouponException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {
    private int customerID;
    protected static final Logger logger = LogManager.getLogger();


    public CustomerFacade() throws SQLException {
        logger.info("CustomerFacade constructor");
    }
    public CustomerFacade(int customerID) throws SQLException {
        this.customerID = customerID;
        logger.info("CustomerFacade constructor by customer id:{}", customerID);
    }
    /**
     * Retrieves the ID of the current customer.
     * <p>
     * This method logs the current `customerID` and returns it. It is used to access
     * the customer ID associated with the current instance.
     * <p>
     * All actions, including the retrieval of the customer ID, are logged for auditing
     * and debugging purposes.
     *
     * @return the ID of the current customer.
     */
    public int getCustomerID() {
        logger.info("getCustomerID - returning this customerId:{}", customerID);
        return customerID;
    }
    /**
     * Sets the ID of the current customer.
     * <p>
     * This method assigns the provided customer ID to the instance and logs the action.
     * It is specifically used for setting the customer ID during the login process for users
     * of type CUSTOMER.
     * <p>
     * All actions, including setting the customer ID, are logged for auditing and debugging
     * purposes.
     *
     * @param customerID the ID of the customer to be set.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
        logger.info("setCustomerID - setting this customer id:{}", customerID);
    }
    /**
     * Authenticates a customer by checking the provided email and password.
     * <p>
     * This method calls `isCustomerExists` on the `customersDbDao` to verify whether a customer
     * with the given email and password exists in the database. It returns an integer representing
     * the customer's ID if the credentials are correct, or `0` if they are not.
     * <p>
     * If an exception occurs during the authentication process (e.g., a `CustomerException`),
     * it is caught, logged, and rethrown.
     * <p>
     * All actions, including the attempt to authenticate the customer and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param email the email address of the customer trying to log in.
     * @param password the password of the customer trying to log in.
     * @return the ID of the customer if authentication is successful, or `0` if the credentials are invalid.
     * @throws CustomerException if an error occurs during the authentication process.
     */
    @Override
    public int login(String email, String password) throws CustomerException {
        logger.info("login - trying to login using email:{} and password:{}", email,password);
        try {
            return customersDbDao.isCustomerExists(email,password);
        } catch (CustomerException e) {
            logger.error("login threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Purchases a coupon for the customer by recording the purchase in the database.
     * <p>
     * This method first checks if the customer has already purchased the coupon by calling
     * `checkCouponPurchase` on the `couponsDbDao`. If the coupon has not been purchased yet,
     * the method adds the purchase to the database by calling `addCouponPurchase`.
     * <p>
     * If an exception occurs during the process (e.g., a `CouponException`), it is caught,
     * logged, and rethrown as a `CustomerException`.
     * <p>
     * All actions, including the attempt to add a purchase and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param coupon the `Coupon` object representing the coupon to be purchased.
     * @throws CustomerException if an error occurs during the purchase process.
     */
    public void purchaseCoupons(Coupon coupon) throws CustomerException {
        logger.info("purchaseCoupons - trying to add purchase for this customer id:{} and coupon id {}",customerID,coupon.getId());
        try {
            //Checking that there is no existing purchase == false
            if(!couponsDbDao.checkCouponPurchase(customerID,coupon.getId())) {
                logger.info("purchaseCoupons - checkCouponPurchase - returns true");
                couponsDbDao.addCouponPurchase(customerID, coupon.getId());
            }else {
                logger.info("purchaseCoupons - checkCouponPurchase - returns false");
            }
        } catch (CouponException e) {
            logger.error("purchaseCoupons threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Retrieves all coupons for a specific customer.
     * <p>
     * This method calls `allCouponsByCustomer` on the `couponsDbDao` to fetch all coupons
     * associated with the current customer (identified by `customerID`). The result is returned
     * as an `ArrayList` of `Coupon` objects.
     * <p>
     * If an exception occurs during the retrieval process (e.g., a `CouponException`),
     * it is caught, logged, and rethrown as a `CustomerException`.
     * <p>
     * All actions, including the attempt to retrieve the coupons and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @return an `ArrayList` containing all coupons for the customer.
     * @throws CustomerException if an error occurs during the retrieval process.
     */
    public ArrayList<Coupon> getCustomerCoupons() throws CustomerException {
        logger.info("getCustomerCoupons  - getting coupons for this customerID:{}", customerID);
        try {
            return couponsDbDao.allCouponsByCustomer(customerID);
        } catch (CouponException e) {
            logger.error("getCustomerCoupons threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Retrieves all coupons for a specific customer that belong to the specified category.
     * <p>
     * This method calls `allCouponsByCustomerAndCategory` on the `couponsDbDao` to fetch all coupons
     * associated with the current customer (identified by `customerID`) that are in the given category
     * (identified by the category ID). The result is returned as an `ArrayList` of `Coupon` objects.
     * <p>
     * If an exception occurs during the retrieval process (e.g., a `CouponException`),
     * it is caught, logged, and rethrown as a `CustomerException`.
     * <p>
     * All actions, including the attempt to retrieve the coupons and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param category the `CategoryEnum` representing the category of coupons to retrieve.
     * @return an `ArrayList` containing coupons for the customer that belong to the specified category.
     * @throws CustomerException if an error occurs during the retrieval process.
     */
    public ArrayList<Coupon> getCustomerCoupons (CategoryEnum category) throws CustomerException {
        logger.info("getCustomerCoupons  - getting coupons for this customerID:{} and category:{}",customerID,category);
        try {
            return couponsDbDao.allCouponsByCustomerAndCategory(customerID, category.getId());
        } catch (CouponException e) {
            logger.error("getCustomerCoupons threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Retrieves all coupons for a specific customer that are below or equal to the specified maximum price.
     * <p>
     * This method calls `allCouponsByCustomerAndMaxPrice` on the `couponsDbDao` to fetch all coupons
     * associated with the current customer (identified by `customerID`) that have a price less than or equal
     * to the given `maxPrice`. The result is returned as an `ArrayList` of `Coupon` objects.
     * <p>
     * If an exception occurs during the retrieval process (e.g., a `CouponException`),
     * it is caught, logged, and rethrown as a `CustomerException`.
     * <p>
     * All actions, including the attempt to retrieve the coupons and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param maxPrice the maximum price of coupons to retrieve.
     * @return an `ArrayList` containing coupons for the customer with a price less than or equal to `maxPrice`.
     * @throws CustomerException if an error occurs during the retrieval process.
     */
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws CustomerException {
        logger.info("getCustomerCoupons  - getting coupons for this customerID:{} and max price:{}",customerID,maxPrice);
        try {
            return couponsDbDao.allCouponsByCustomerAndMaxPrice(customerID,maxPrice);
        } catch (CouponException e) {
            logger.error("getCustomerCoupons threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Retrieves the details of the customer associated with the current customer ID.
     * <p>
     * This method calls `getSelectedCustomer` on the `customersDbDao` to fetch the details
     * of the customer identified by `customerID`. The result is returned as a `Customer` object
     * containing the customer's details.
     * <p>
     * If an exception occurs during the retrieval process (e.g., a `CustomerException`),
     * it is caught, logged, and rethrown.
     * <p>
     * All actions, including the attempt to retrieve the customer's details and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @return the `Customer` object representing the details of the customer with the current ID.
     * @throws CustomerException if an error occurs during the retrieval process or if the customer cannot be found.
     */
    public Customer getCustomerDetails() throws CustomerException {
        logger.info("getCustomerDetails - getting details for this customer id:{}",customerID);
        try {
            return customersDbDao.getSelectedCustomer(customerID);
        } catch (CustomerException e) {
            logger.error("getCustomerDetails threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
}
