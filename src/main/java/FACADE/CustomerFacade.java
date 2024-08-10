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
        logger.info("CustomerFacade1");
    }
    public CustomerFacade(int customerID) throws SQLException {
        this.customerID = customerID;
        logger.info("CustomerFacade2{}", customerID);
    }
    public int getCustomerID() {
        logger.info("getCustomerID1{}", customerID);
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
        logger.info("getCustomerID2{}", customerID);
    }
    @Override
    public int login(String email, String password) throws CustomerException {
        logger.info("login{}", email);
        try {
            return customersDbDao.isCustomerExists(email,password);
        } catch (CustomerException e) {
            logger.error("login {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    public void purchaseCoupons(Coupon coupon) throws CustomerException {
        logger.info("purchaseCoupons{}", coupon.getId());
        try {
            //Checking that there is no existing purchase == false
            if(!couponsDbDao.checkCouponPurchase(customerID,coupon.getId())) {
                logger.info("checkCouponPurchase returns true");
                couponsDbDao.addCouponPurchase(customerID, coupon.getId());
            }else {
                logger.info("checkCouponPurchase returns false");
            }
        } catch (CouponException e) {
            logger.error("purchaseCoupons {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCustomerCoupons() throws CustomerException {
        logger.info("getCustomerCoupons1{}", customerID);
        try {
            return couponsDbDao.allCouponsByCustomer(customerID);
        } catch (CouponException e) {
            logger.error("getCustomerCoupons1 {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCustomerCoupons (CategoryEnum category) throws CustomerException {
        logger.info("getCustomerCoupons2{}", category);
        try {
            return couponsDbDao.allCouponsByCustomerAndCategory(customerID, category.getId());
        } catch (CouponException e) {
            logger.error("getCustomerCoupons2 {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws CustomerException {
        logger.info("getCustomerCoupons3{}", maxPrice);
        try {
            return couponsDbDao.allCouponsByCustomerAndMaxPrice(customerID,maxPrice);
        } catch (CouponException e) {
            logger.error("getCustomerCoupons3 {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    public Customer getCustomerDetails() throws CustomerException {
        logger.info("getCustomerDetails");
        try {
            return customersDbDao.getSelectedCustomer(customerID);
        } catch (CustomerException e) {
            logger.error("getCustomerDetails {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
}
