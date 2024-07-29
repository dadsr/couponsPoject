package FACADE;

import BEANS.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {
    private int customerID;

    public CustomerFacade() throws SQLException {

    }
    public CustomerFacade(int customerID) throws SQLException {
        this.customerID = customerID;
    }
    public int getCustomerID() {
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    @Override
    public int login(String email, String password) {
        try {
            return customersDbDao.isCustomerExists(email,password);
        } catch (CustomerException e) {
            throw new RuntimeException(e);
        }
    }
    public void purchaseCoupons(Coupon coupon){
        try {
            if(couponsDbDao.checkCouponPurchase(customerID,coupon.getId()))
                couponsDbDao.addCouponPurchase(customerID,coupon.getId());
            else
                throw new CouponException("purchaseCoupons failed");
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCustomerCoupons(){
        try {
            return couponsDbDao.allCouponsByCustomer(customerID);
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCustomerCoupons(CategoryEnum category){
        try {
            return couponsDbDao.allCouponsByCustomerAndCategory(customerID, category.getId());
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice){
        try {
            return couponsDbDao.allCouponsByCustomerAndMaxPrice(customerID,maxPrice);
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public Customer getCustomerDetails(){
        try {
            return customersDbDao.getSelectedCustomer(customerID);
        } catch (CustomerException e) {
            throw new RuntimeException(e);
        }
    }
}
