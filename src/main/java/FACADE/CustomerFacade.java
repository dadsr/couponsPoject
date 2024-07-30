package FACADE;

import BEANS.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static FACADE.AdminFacade.logger;

public class CustomerFacade extends ClientFacade {
    private int customerID;

    public CustomerFacade() throws SQLException {
        logger.info("CustomerFacade");
    }
    public CustomerFacade(int customerID) throws SQLException {
        this.customerID = customerID;
        logger.info("CustomerFacade");
    }
    public int getCustomerID() {
        logger.info("getCustomerID");
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
        logger.info("getCustomerID");
    }
    @Override
    public int login(String email, String password) {
        logger.info("login");
        try {
            return customersDbDao.isCustomerExists(email,password);
        } catch (CustomerException e) {
            logger.error("login {}", e.getMessage());
            throw new RuntimeException(e);
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
    public ArrayList<Coupon> getCustomerCoupons(){
        logger.info("getCustomerCoupons1");
        try {
            return couponsDbDao.allCouponsByCustomer(customerID);
        } catch (CouponException e) {
            logger.error("getCustomerCoupons1 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCustomerCoupons(CategoryEnum category){
        logger.info("getCustomerCoupons2");
        try {
            return couponsDbDao.allCouponsByCustomerAndCategory(customerID, category.getId());
        } catch (CouponException e) {
            logger.error("getCustomerCoupons2 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice){
        logger.info("getCustomerCoupons3");
        try {
            return couponsDbDao.allCouponsByCustomerAndMaxPrice(customerID,maxPrice);
        } catch (CouponException e) {
            logger.error("getCustomerCoupons3 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public Customer getCustomerDetails(){
        logger.info("getCustomerDetails");
        try {
            return customersDbDao.getSelectedCustomer(customerID);
        } catch (CustomerException e) {
            logger.error("getCustomerDetails {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
