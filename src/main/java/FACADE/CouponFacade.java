package FACADE;

import BEANS.CouponException;
import DAO.CouponsDBDAO;

import java.sql.Date;
import java.sql.SQLException;

public class CouponFacade extends ClientFacade{

    public CouponFacade() throws SQLException {
    }

    @Override
    public int login(String email, String password) {
        return 1;
    }

    public void deleteExpiredCoupons(){
        try {
            new CouponsDBDAO().deleteCouponsByExpirationDate();
        } catch (CouponException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
