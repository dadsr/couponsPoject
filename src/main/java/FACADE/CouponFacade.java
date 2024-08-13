package FACADE;

import BEANS.CouponException;
import DAO.CouponsDBDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class CouponFacade extends ClientFacade{
    protected static final Logger logger = LogManager.getLogger();

    public CouponFacade() throws SQLException {
        logger.info("CouponFacade constructor");
    }

    /**
     * This method is an implementation of the `login` method inherited from `ClientFacade`,
     * but it is not used in practice and does not perform any real authentication.
     * <p>
     * The method logs the email and password used for the login attempt and always returns a fixed
     * value of `1`. This placeholder implementation is included to satisfy the method signature
     * required by extending `ClientFacade`, but does not actually interact with any authentication
     * mechanism or database.
     * <p>
     * All actions, including the attempt to log in and the fixed return value, are logged for auditing
     * and debugging purposes.
     *
     * @param email the email address of the company attempting to log in.
     * @param password the password of the company attempting to log in.
     * @return a fixed value of `1`, indicating a successful login attempt (for placeholder purposes).
     */
    @Override
    public int login(String email, String password) {
        logger.info("login - trying to login using email:{} and password:{}", email, password);
        return 1;
    }
    /**
     * Deletes coupons from the database that have expired based on their end date.
     * <p>
     * This method invokes `deleteCouponsByExpirationDate` on a new instance of `CouponsDBDAO` to remove
     * all coupons whose end dates have passed. It is intended to clean up expired coupons and maintain
     * the integrity of the coupon data in the database.
     * <p>
     * If an exception occurs during the deletion process (e.g., `CouponException` or `SQLException`),
     * it is caught, logged, and rethrown as a `CouponException`.
     * <p>
     * All actions, including the attempt to delete expired coupons and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @throws CouponException if an error occurs during the deletion process or if the deletion cannot be completed.
     */
    public void deleteExpiredCoupons() throws CouponException {
        logger.info("deleteExpiredCoupons - Deleting coupons whose endDate has expired");
        try {
            new CouponsDBDAO().deleteCouponsByExpirationDate();
        } catch (CouponException | SQLException e) {
            logger.error("deleteExpiredCoupons threw exception - {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }
//
}
