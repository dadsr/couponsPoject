package FACADE;

import BEANS.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

import static FACADE.AdminFacade.logger;

public class CompanyFacade  extends ClientFacade {
    private int companyID;
    protected static final Logger logger = LogManager.getLogger();

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
    }

    @Override
    public int login(String email, String password) throws CouponException {
        logger.info("login");
        try {
            return companiesDbDao.isCompanyExists(email,password);
        } catch (CompanyException e) {
            logger.error("login {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    public void addCoupon(Coupon coupon) throws CouponException {
        logger.info("addCoupon");
        try {
            couponsDbDao.addCoupon(coupon);
        } catch (CouponException e) {
            logger.error("addCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());        }
    }
    public void updateCoupon(Coupon coupon) throws CouponException {
        logger.info("updateCoupon");
        try {
            couponsDbDao.updateCoupon(coupon);
        } catch (CouponException e) {
            logger.error("updateCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    public void deleteCoupon(int couponID) throws CouponException {
        logger.info("deleteCoupon");
        try {
            couponsDbDao.deletePurchasesByCoupon(couponID);
            couponsDbDao.deleteCoupon(couponID);
        } catch (CouponException e) {
            logger.error("deleteCoupon {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCompanyCoupons() throws CouponException {
        logger.info("getCompanyCoupons1");
        try {
            return couponsDbDao.allCouponsByCompany(companyID);
        } catch (CouponException e) {
            logger.error("getCompanyCoupons {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(CategoryEnum category) throws CouponException {
        logger.info("getCompanyCoupons2");
        try {
            return couponsDbDao.allCouponsByCompanyAndCategory(companyID,category.getId());
        } catch (CouponException e) {
            logger.error("getCompanyCoupons2 {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    public ArrayList<Coupon> getCompanyCoupons(Double maxPrice) throws CouponException {
        logger.info("getCompanyCoupons3");
        try {
            return couponsDbDao.allCouponsByCompanyAndMaxPrice(companyID,maxPrice);
        } catch (CouponException e) {
            logger.error("getCompanyCoupons3 {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    public Company getCompanyDetails() throws CouponException {
        logger.info("getCompanyDetails");
        try {
            return companiesDbDao.getSelectedCompany(companyID);
        } catch (CompanyException e) {
            logger.error("getCompanyDetails {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
}

