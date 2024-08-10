package FACADE;

import BEANS.*;
import BEANS.CouponException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyFacade  extends ClientFacade {
    private int companyID;
    protected static final Logger logger = LogManager.getLogger();

    public CompanyFacade() throws SQLException {
        logger.info("CompanyFacade");
    }
    public CompanyFacade(int companyID) throws SQLException {
        this.companyID = companyID;
        logger.info("CompanyFacade{}", companyID);
    }
    public int getCompanyID() {
        logger.info("getCompanyID");
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    @Override
    public int login(String email, String password) throws CompanyException {
        logger.info("login{}", email);
        try {
            return companiesDbDao.isCompanyExists(email,password);
        } catch (CompanyException e) {
            logger.error("login {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    public void addCoupon(Coupon coupon) throws CouponException {
        logger.info("addCoupon{}", coupon.getTitle());
        try {
            couponsDbDao.addCoupon(coupon);
        } catch (CouponException e) {
            logger.error("addCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());        }
    }
    public void updateCoupon(Coupon coupon) throws CouponException {
        logger.info("updateCoupon{}", coupon.getTitle());
        try {
            couponsDbDao.updateCoupon(coupon);
        } catch (CouponException e) {
            logger.error("updateCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    public void deleteCoupon(int couponID) throws CouponException {
        logger.info("deleteCoupon{}", couponID);
        try {
            couponsDbDao.deletePurchasesByCoupon(couponID);
            couponsDbDao.deleteCoupon(couponID);
        } catch (CouponException e) {
            logger.error("deleteCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCompanyCoupons() throws CompanyException {
        logger.info("getCompanyCoupons1{}", companyID);
        try {
            return couponsDbDao.allCouponsByCompany(companyID);
        } catch (CouponException e) {
            logger.error("getCompanyCoupons {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(CategoryEnum category) throws CompanyException {
        logger.info("getCompanyCoupons2{}", category);
        try {
            return couponsDbDao.allCouponsByCompanyAndCategory(companyID,category.getId());
        } catch (CouponException e) {
            logger.error("getCompanyCoupons2 {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(Double maxPrice) throws CompanyException {
        logger.info("getCompanyCoupons3{}", maxPrice);
        try {
            return couponsDbDao.allCouponsByCompanyAndMaxPrice(companyID,maxPrice);
        } catch (CouponException e) {
            logger.error("getCompanyCoupons3 {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    public Company getCompanyDetails() throws CompanyException {
        logger.info("getCompanyDetails");
        try {
            return companiesDbDao.getSelectedCompany(companyID);
        } catch (CompanyException e) {
            logger.error("getCompanyDetails {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
}

