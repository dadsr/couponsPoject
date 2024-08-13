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
        logger.info("CompanyFacade constructor");
    }
    public CompanyFacade(int companyID) throws SQLException {
        this.companyID = companyID;
        logger.info("CompanyFacade constructor using companyID:{}", companyID);
    }
    public int getCompanyID() {
        logger.info("getCompanyID -returning this companyID :{}",companyID);
        return companyID;
    }
    /**
     * Sets the ID for the company in the context of COMPANY login.
     * <p>
     * This method is used specifically for setting the company ID during the login process
     * for users of type COMPANY. It logs the company ID being set for debugging and auditing purposes.
     *
     * @param companyID the ID of the company to be set.
     */
    public void setCompanyID(int companyID) {
        logger.info("setCompanyID -setting this companyID :{}",companyID);
        this.companyID = companyID;
    }
    /**
     * Authenticates a company by checking the provided email and password.
     * <p>
     * This method calls `isCompanyExists` on the `companiesDbDao` to verify whether a company
     * with the given email and password exists in the database. The result is an integer
     * representing the company's ID if the credentials are correct, or `0` if they are not.
     * <p>
     * If an exception occurs during the authentication process (e.g., a `CompanyException`),
     * it is caught, logged, and rethrown.
     * <p>
     * All actions, including the attempt to authenticate the company and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param email the email address of the company trying to log in.
     * @param password the password of the company trying to log in.
     * @return the ID of the company if authentication is successful, or `0` if the credentials are invalid.
     * @throws CompanyException if an error occurs during the authentication process.
     */
    @Override
    public int login(String email, String password) throws CompanyException {
        logger.info("login - trying to login using email:{} and password:{}", email,password);
        try {
            return companiesDbDao.isCompanyExists(email,password);
        } catch (CompanyException e) {
            logger.error("login threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Adds a new coupon to the database.
     * <p>
     * This method calls `addCoupon` on the `couponsDbDao` to insert a new coupon into the database.
     * The coupon object provided contains the details of the coupon to be added.
     * <p>
     * If an exception occurs during the addition process (e.g., a `CouponException`),
     * it is caught, logged, and rethrown.
     * <p>
     * All actions, including the attempt to add the coupon and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param coupon the `Coupon` object representing the new coupon to be added.
     * @throws CouponException if an error occurs during the addition process.
     */
    public void addCoupon(Coupon coupon) throws CouponException {
        logger.info("addCoupon - trying to add new coupon - coupon title:{}", coupon.getTitle());
        try {
            couponsDbDao.addCoupon(coupon);
        } catch (CouponException e) {
            logger.error("addCoupon threw exception - {}", e.getMessage());
            throw new CouponException(e.getMessage());        }
    }
    /**
     * Updates the details of an existing coupon in the database.
     * <p>
     * This method calls `updateCoupon` on the `couponsDbDao` to update the coupon's details
     * in the database, identified by the coupon's ID. The coupon object provided contains
     * the updated details.
     * <p>
     * If an exception occurs during the update process (e.g., a `CouponException`),
     * it is caught, logged, and rethrown.
     * <p>
     * All actions, including the attempt to update the coupon and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param coupon the `Coupon` object containing the updated details of the coupon.
     * @throws CouponException if an error occurs during the update process.
     */
    public void updateCoupon(Coupon coupon) throws CouponException {
        logger.info("updateCoupon -trying to update coupon - coupon id:{}", coupon.getId());
        try {
            couponsDbDao.updateCoupon(coupon);
        } catch (CouponException e) {
            logger.error("updateCoupon threw exception - {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    /**
     * Deletes a coupon and its associated purchases from the database.
     * <p>
     * This method performs a cascade deletion to remove a coupon and all related purchase records.
     * It first deletes any purchases associated with the specified coupon ID by calling
     * `deletePurchasesByCoupon` on the `couponsDbDao`. Then, it deletes the coupon itself
     * from the database by invoking `deleteCoupon` on the DAO.
     * <p>
     * If an exception occurs during the deletion process (e.g., a `CouponException`),
     * it is caught, logged, and rethrown.
     * <p>
     * All actions, including the attempt to delete the coupon and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param couponID the ID of the coupon to be deleted.
     * @throws CouponException if an error occurs during the deletion process.
     */
    public void deleteCoupon(int couponID) throws CouponException {
        logger.info("deleteCoupon -trying to delete coupon by coupon id:{}", couponID);
        try {
            couponsDbDao.deletePurchasesByCoupon(couponID);
            couponsDbDao.deleteCoupon(couponID);
        } catch (CouponException e) {
            logger.error("deleteCoupon threw exception - {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
    /**
     * Retrieves all coupons for a specific company.
     * <p>
     * This method calls `allCouponsByCompany` on the `couponsDbDao` to fetch all coupons
     * associated with the current company (identified by `companyID`). The result is returned
     * as an `ArrayList` of `Coupon` objects.
     * <p>
     * If an exception occurs during the retrieval process (e.g., a `CouponException`), it is caught, logged,
     * and rethrown as a `CompanyException`.
     * <p>
     * All actions, including the attempt to retrieve the coupons and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @return an `ArrayList` containing all coupons for the company.
     * @throws CompanyException if an error occurs during the retrieval process.
     */
    public ArrayList<Coupon> getCompanyCoupons() throws CompanyException {
        logger.info("getCompanyCoupons - getting all company's coupons for company id:{}", companyID);
        try {
            return couponsDbDao.allCouponsByCompany(companyID);
        } catch (CouponException e) {
            logger.error("getCompanyCoupons threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Retrieves all coupons for a specific company that belong to the specified category.
     * <p>
     * This method calls `allCouponsByCompanyAndCategory` on the `couponsDbDao` to fetch all coupons
     * associated with the current company (identified by `companyID`) that are in the given category
     * (identified by the category ID). The result is returned as an `ArrayList` of `Coupon` objects.
     * <p>
     * If an exception occurs during the retrieval process (e.g., a `CouponException`), it is caught, logged,
     * and rethrown as a `CompanyException`.
     * <p>
     * All actions, including the attempt to retrieve the coupons and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param category the `CategoryEnum` representing the category of coupons to retrieve.
     * @return an `ArrayList` containing coupons for the company that belong to the specified category.
     * @throws CompanyException if an error occurs during the retrieval process.
     */
    public ArrayList<Coupon> getCompanyCoupons(CategoryEnum category) throws CompanyException {
        logger.info("getCompanyCoupons - getting all company's coupons for company company id:{} by category:{}",companyID, category);
        try {
            return couponsDbDao.allCouponsByCompanyAndCategory(companyID,category.getId());
        } catch (CouponException e) {
            logger.error("getCompanyCoupons threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Retrieves all coupons for a specific company that are below or equal to the specified maximum price.
     * <p>
     * This method calls `allCouponsByCompanyAndMaxPrice` on the `couponsDbDao` to fetch all coupons
     * associated with the current company (identified by `companyID`) that have a price less than or equal
     * to the given `maxPrice`. The result is returned as an `ArrayList` of `Coupon` objects.
     * <p>
     * If an exception occurs during the retrieval process (e.g., a `CouponException`), it is caught, logged,
     * and rethrown as a `CompanyException`.
     * <p>
     * All actions, including the attempt to retrieve the coupons and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param maxPrice the maximum price of coupons to retrieve.
     * @return an `ArrayList` containing coupons for the company with a price less than or equal to `maxPrice`.
     * @throws CompanyException if an error occurs during the retrieval process.
     */
    public ArrayList<Coupon> getCompanyCoupons(Double maxPrice) throws CompanyException {
        logger.info("getCompanyCoupons - getting all company's coupons for company company id:{} by max price:{}", companyID,maxPrice);
        try {
            return couponsDbDao.allCouponsByCompanyAndMaxPrice(companyID,maxPrice);
        } catch (CouponException e) {
            logger.error("getCompanyCoupons threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Retrieves the details of the company associated with the current company ID.
     * <p>
     * This method calls `getSelectedCompany` on the `companiesDbDao` to fetch the details
     * of the company identified by `companyID`. The result is returned as a `Company` object
     * containing the company's details.
     * <p>
     * If an exception occurs during the retrieval process (e.g., a `CompanyException`),
     * it is caught, logged, and rethrown.
     * <p>
     * All actions, including the attempt to retrieve the company's details and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @return the `Company` object representing the details of the company with the current ID.
     * @throws CompanyException if an error occurs during the retrieval process or if the company cannot be found.
     */
   public Company getCompanyDetails() throws CompanyException {
        logger.info("getCompanyDetails - getting details for this company id:{}",companyID);
        try {
            return companiesDbDao.getSelectedCompany(companyID);
        } catch (CompanyException e) {
            logger.error("getCompanyDetails threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }


}

