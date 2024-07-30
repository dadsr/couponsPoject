package FACADE;

import BEANS.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static FACADE.AdminFacade.logger;

public class CompanyFacade  extends ClientFacade {
    private int companyID;

    public CompanyFacade() throws SQLException {
        logger.info("CompanyFacade");
    }
    public CompanyFacade(int companyID) throws SQLException {
        this.companyID = companyID;
        logger.info("CompanyFacade");
    }
    public int getCompanyID() {
        logger.info("getCompanyID");
        return companyID;
    }
    public void setCompanyID(int companyID) {
        this.companyID = companyID;
        logger.info("getCompanyID");
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
    public void addCoupon(Coupon coupon){
        logger.info("addCoupon");
        try {
            couponsDbDao.addCoupon(coupon);
        } catch (CouponException e) {
            logger.error("addCoupon {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void updateCoupon(Coupon coupon){
        logger.info("updateCoupon");
        try {
            couponsDbDao.updateCoupon(coupon);
        } catch (CouponException e) {
            logger.error("updateCoupon {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void deleteCoupon(int couponID){
        logger.info("deleteCoupon");
        try {
            couponsDbDao.deletePurchasesByCoupon(couponID);
            couponsDbDao.deleteCoupon(couponID);
        } catch (CouponException e) {
            logger.error("deleteCoupon {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(){
        logger.info("getCompanyCoupons1");
        try {
            return couponsDbDao.allCouponsByCompany(companyID);
        } catch (CouponException e) {
            logger.error("getCompanyCoupons {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(CategoryEnum category){
        logger.info("getCompanyCoupons2");
        try {
            return couponsDbDao.allCouponsByCompanyAndCategory(companyID,category.getId());
        } catch (CouponException e) {
            logger.error("getCompanyCoupons2 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(Double maxPrice){
        logger.info("getCompanyCoupons3");
        try {
            return couponsDbDao.allCouponsByCompanyAndMaxPrice(companyID,maxPrice);
        } catch (CouponException e) {
            logger.error("getCompanyCoupons3 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public Company getCompanyDetails(){
        logger.info("getCompanyDetails");
        try {
            return companiesDbDao.getSelectedCompany(companyID);
        } catch (CompanyException e) {
            logger.error("getCompanyDetails {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

