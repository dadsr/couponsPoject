package FACADE;

import BEANS.CouponException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CouponFacadeTest {

    //   void login()  - tested on LoginManagerTest

    @Test
    void deleteExpiredCoupons() {
        System.out.print("deleting expired coupons");
        try {
            new CouponFacade().deleteExpiredCoupons();
            System.out.println(" - success");
        } catch (SQLException |CouponException e) {
            System.out.println(" - failed");
        }
    }
}