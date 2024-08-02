package FACADE;

import BEANS.Company;
import BEANS.CompanyException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdminFacadeTest {
    Company company;
 //   void login()  - tested on LoginManagerTest

    @Test
    void getOneCompany() {
        System.out.print("getting non existing company ");
        try {
            new AdminFacade().getOneCompany(666);
            System.out.println(" - faild");
        } catch (CompanyException e) {
            System.out.println(" - success");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.print("getting an existing company ");
        int tstId = 33;
        try {

            company = new AdminFacade().getOneCompany(tstId);
        } catch (CompanyException e) {
            System.out.println(" - faild");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(company.getId() == tstId){
            System.out.println(" - success");
        }else {
            System.out.println(" - faild");
        }
    }

    @Test
    void addCompany() {
        System.out.print("Adding an existing company");
        try {
            AdminFacade admin = new AdminFacade();
            admin.addCompany(company);
            System.out.println(" - faild");
        }catch (Exception e){
            System.out.println(" - success");
        }
        System.out.print("Adding new company");
    }

    @Test
    void updateCompany() {

    }

    @Test
    void deleteCompany() {
    }

    @Test
    void getAllCompanies() {
    }


    @Test
    void testGetOneCompany() {
    }

    @Test
    void addCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void getAllCustomers() {
    }

    @Test
    void getOneCustomer() {
    }
}