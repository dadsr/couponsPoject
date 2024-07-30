package FACADE;

import BEANS.CouponException;
import DAO.CouponsDBDAO;

import java.sql.Date;
import java.sql.SQLException;

import static FACADE.AdminFacade.logger;

public class CouponFacade extends ClientFacade{

    public CouponFacade() throws SQLException {
    }

    @Override
    public int login(String email, String password) {
        logger.info("updateCompany");
        return 1;
    }

    public void deleteExpiredCoupons(){
        logger.info("deleteExpiredCoupons");
        try {
            new CouponsDBDAO().deleteCouponsByExpirationDate();
        } catch (CouponException | SQLException e) {
            logger.error("deleteExpiredCoupons {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
