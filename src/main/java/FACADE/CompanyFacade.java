package FACADE;

import BEANS.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyFacade  extends ClientFacade {
    private int companyID;

    public CompanyFacade() throws SQLException {

    }
    public CompanyFacade(int companyID) throws SQLException {
        this.companyID = companyID;
    }
    public int getCompanyID() {
        return companyID;
    }
    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
    @Override
    public int login(String email, String password) {
        try {
            return customersDbDao.isCustomerExists(email,password);
        } catch (CustomerException e) {
            throw new RuntimeException(e);
        }
    }
    public void addCoupon(Coupon coupon){
        try {
            couponsDbDao.addCoupon(coupon);
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateCoupon(Coupon coupon){
        try {
            couponsDbDao.updateCoupon(coupon);
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteCoupon(int couponID){
        try {
            couponsDbDao.deletePurchasesByCoupon(couponID);
            couponsDbDao.deleteCoupon(couponID);
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(){
        try {

            return couponsDbDao.allCouponsByCompany(companyID);
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(CategoryEnum category){
        try {
            return couponsDbDao.allCouponsByCompanyAndCategory(companyID,category.getId());
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(Double maxPrice){
        try {
            return couponsDbDao.allCouponsByCompanyAndMaxPrice(companyID,maxPrice);
        } catch (CouponException e) {
            throw new RuntimeException(e);
        }
    }
    public Company getCompanyDetails(){
        try {
            return companiesDbDao.getSelectedCompany(companyID);
        } catch (CompanyException e) {
            throw new RuntimeException(e);
        }
    }
}

