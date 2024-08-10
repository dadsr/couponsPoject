package FACADE;

import BEANS.CouponException;
import DAO.CouponsDBDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class CouponFacade extends ClientFacade{
    protected static final Logger logger = LogManager.getLogger();

    public CouponFacade() throws SQLException {
        logger.info("CouponFacade");
    }

    @Override
    public int login(String email, String password) {
        logger.info("updateCompany{}", email);
        return 1;
    }

    public void deleteExpiredCoupons() throws CouponException {
        logger.info("deleteExpiredCoupons");
        try {
            new CouponsDBDAO().deleteCouponsByExpirationDate();
        } catch (CouponException | SQLException e) {
            logger.error("deleteExpiredCoupons {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

}
