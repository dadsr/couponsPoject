package FACADE;

import BEANS.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import static FACADE.AdminFacade.logger;

public class CustomerFacade extends ClientFacade {
    private int customerID;
    protected static final Logger logger = LogManager.getLogger();


    public CustomerFacade() throws SQLException {
        logger.info("CustomerFacade1");
    }
    public CustomerFacade(int customerID) throws SQLException {
        this.customerID = customerID;
        logger.info("CustomerFacade2");
    }
    public int getCustomerID() {
        logger.info("getCustomerID1");
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
        logger.info("getCustomerID2");
    }
    @Override
    public int login(String email, String password) throws CustomerException {
        logger.info("login");
        try {
            return customersDbDao.isCustomerExists(email,password);
        } catch (CustomerException e) {
            logger.error("login {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    public void purchaseCoupons(Coupon coupon){
        logger.info("purchaseCoupons");
        try {
            if(couponsDbDao.checkCouponPurchase(customerID,coupon.getId()))
                couponsDbDao.addCouponPurchase(customerID,coupon.getId());
            else {
                logger.error("purchaseCoupons - purchase failed");
                throw new CouponException("purchaseCoupons failed");
            }
        } catch (CouponException e) {
            logger.error("purchaseCoupons {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCustomerCoupons() throws CustomerException {
        logger.info("getCustomerCoupons1");
        try {
            return couponsDbDao.allCouponsByCustomer(customerID);
        } catch (CouponException e) {
            logger.error("getCustomerCoupons1 {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCustomerCoupons(CategoryEnum category) throws CustomerException {
        logger.info("getCustomerCoupons2");
        try {
            return couponsDbDao.allCouponsByCustomerAndCategory(customerID, category.getId());
        } catch (CouponException e) {
            logger.error("getCustomerCoupons2 {}", e.getMessage());
            throw new CustomerException(e.getMessage());

        }
    }
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws CustomerException {
        logger.info("getCustomerCoupons3");
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
