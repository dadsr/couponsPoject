package FACADE;

import BEANS.Company;
import BEANS.CompanyException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdminFacadeTest {

    //   void login()  - tested on LoginManagerTest

    @Test
    void getOneCompany() {
        System.out.print("getting non existing company ");
        try {
            new AdminFacade().getOneCompany(666);
            System.out.println(" - failed");
        } catch (CompanyException e) {
            System.out.println(" - success");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.print("getting an existing company ");
        int tstId = 33;
        Company company =null;
        try {

            company = new AdminFacade().getOneCompany(tstId);
        } catch (CompanyException e) {
            System.out.println(" - failed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(company.getId() == tstId){
            System.out.println(" - success");
        }else {
            System.out.println(" - failed");
        }
    }

    @Test
    void addCompany() {
        AdminFacade admin = null;
        try {
            admin = new AdminFacade();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.print("Adding existing company");
            int tstId = 33;
            Company    company = new AdminFacade().getOneCompany(tstId);
            admin.addCompany(company);
            System.out.print("Adding new company + getOneCompany by email & password ");
            admin.addCompany(new Company("company", "email@email.com", "password", null));
            if (new AdminFacade().getOneCompany("email@email.com", "password").getId() > 0) {
                System.out.println(" - success");
            } else {
                System.out.println(" - failed");
            }
        } catch (SQLException | CompanyException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void updateCompany() {
        try {
            Company company =new AdminFacade().getOneCompany("email@email.com", "password");
            company.setEmail("XnewEmail@new.mail");
            company.setName("ynapmoc");
            System.out.print("Update a new company ");

            new AdminFacade().updateCompany(company);
            System.out.println(" - success");
        } catch (SQLException |CompanyException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void deleteCompany() {
    }

    @Test
    void getAllCompanies() {
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