package FACADE;

import BEANS.CategoryEnum;
import BEANS.Company;
import BEANS.CompanyException;
import BEANS.Coupon;
import MAIN.RandomGenerators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdminFacadeTestJ {
    private AdminFacade adminFacade;
    private int compId = 2396;
    @BeforeEach
    void setUp() throws SQLException {
        adminFacade = new AdminFacade();
    }
//Login
    @Test
    void testAdminLoginSuccessful() {
        String email = "admin@admin.com";
        String password = "admin";

        int result = adminFacade.login(email, password);

        assertEquals(1, result, "Admin login should return 1 for correct credentials");
    }
    @Test
    void testAdminLoginFailedWithWrongPassword() {
        String email = "admin@admin.com";
        String password = "wrongPass";

        int result = adminFacade.login(email, password);

        assertEquals(0, result, "Admin login should return 0 for incorrect password");
    }
    @Test
    void testAdminLoginFailedWithWrongCredentials() {
        String email = "wrong@admin.com";
        String password = "wrongPass";

        int result = adminFacade.login(email, password);

        assertEquals(0, result, "Admin login should return 0 for incorrect email and password");
    }

    @Test
    void testCompanyloginSuccessful() {
        try {
            Company company =new AdminFacade().getOneCompany(compId);
            int result = new CompanyFacade().login(company.getEmail(), company.getPassword());
            assertEquals(company.getId(), result, "login should return companyID");
        } catch (CompanyException|SQLException e) {
            //
        }
    }
    @Test
    void testCompanyloginFailedWithWrongCompanyID() {
        int result = 0;
        try {
            result = new CompanyFacade().login("wrong@admin.com","wrongPass");
            assertEquals(0, result, "login should return 0");
        } catch (CompanyException|SQLException e) {
            //
        }
    }




    //addCompany
    @Test
    void testAddCompanySuccessful()  {

        Company company = new Company("meinComp","XXX@meinCamp.com","killAllJuice",null);
        try {
            adminFacade.addCompany(company);

        } catch (CompanyException e) {
            //
        }
    }

//deleteCoupon

//getCompanyCoupons

//getCompanyCoupons

//getCompanyDetails


//
}