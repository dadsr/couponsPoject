package FACADE;

import BEANS.CustomerException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CustomerFacadeTest {

    //   void login()  - tested on LoginManagerTest

    @Test
    void purchaseCoupons() {
    }

    @Test
    void getCustomerCoupons() {
    }

    @Test
    void testGetCustomerCoupons() {
        System.out.print("getting all coupons of a customer");
        try {
            if(new CustomerFacade(19).getCustomerCodupons() != null)
                System.out.println(" - success");
            else
                System.out.println(" - failed");
        } catch (SQLException | CustomerException e) {
            System.out.println(" - failed");
        }
    }
    }

    @Test
    void testGetCustomerCoupons1() {
    }

    @Test
    void getCustomerDetails() {
    }
}