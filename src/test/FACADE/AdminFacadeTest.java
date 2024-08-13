package FACADE;

import BEANS.Company;
import BEANS.CompanyException;
import BEANS.Customer;
import BEANS.CustomerException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class AdminFacadeTest {
    int tstId = 3;//getOneCompany + addCompany

    //void login()  - tested on LoginManagerTest

    @Test
    void getOneCompany() {
        System.out.print("getting non existing company ");
        try {
        AdminFacade admin =new AdminFacade();
            admin.getOneCompany(666);
            System.out.println(" - failed");
        } catch (CompanyException e) {
            System.out.println(" - success");
        } catch (SQLException e) {
            System.out.println(" - failed");
        }

        System.out.print("getting an existing company ");
        Company company =null;
        try {
            AdminFacade admin =new AdminFacade();
            company = admin.getOneCompany(tstId);
        } catch (CompanyException|SQLException e) {
            System.out.println(" - failed");
        }
        if(company.getId() == tstId){
            System.out.println(" - success");
        }else {
            System.out.println(" - failed");
        }
    }

    @Test
    void addCompany() {

        try {
            AdminFacade admin  = new AdminFacade();
            System.out.print("Adding existing company");
            Company company = admin.getOneCompany(tstId);
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
            AdminFacade admin  = new AdminFacade();
            Company company =admin.getOneCompany(71);
            company.setEmail("XnewEmail@new.mail");
            company.setPassword("newPassword");
            System.out.print("Update a new company ");
            admin.updateCompany(company);
            System.out.println(" - success");
        } catch (SQLException |CompanyException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void deleteCompany() {
        try {
            System.out.print("delete selected company ");
            AdminFacade admin = new AdminFacade();
            if(admin.getOneCompany(60) !=null) {
                admin.deleteCompany(60);
                if(admin.getOneCompany(60) == null){
                    System.out.println(" - success");
                }else
                    System.out.println(" - failed");
            }else
                System.out.println(" - failed");

        } catch (CompanyException | SQLException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void getAllCompanies() {
        try {
            AdminFacade admin = new AdminFacade();
            System.out.print("getting all companies ");
            if(admin.getAllCompanies() !=null)
                System.out.println(" - success");
            else
                System.out.println(" - failed");
        } catch (CompanyException|SQLException e) {
            System.out.println(" - failed");
        }
    }


    @Test
    void getOneCustomer() {
        try {
            AdminFacade admin = new AdminFacade();
            System.out.print("getting selected customer ");
            int tstId = 24;
            if(admin.getOneCustomer(tstId)!=null)
                System.out.println(" - success");
            else
                System.out.println(" - failed");

        } catch (SQLException | CustomerException e) {
            System.out.println(" - failed");
        }
    }


    @Test
    void addCustomer() {
        try {
            AdminFacade admin = new AdminFacade();

            System.out.print("Adding existing customer ");
            int tstId = 33;

            Customer customer = admin.getOneCustomer(tstId);
            admin.addCustomer(customer);
            System.out.print("Adding new customer + getOneCustomer by email & password ");
            //String firstName, String lastName, String email, String password, ArrayList<Coupon> coupons
            admin.addCustomer(new Customer("first","last","email@email.com", "password",null));
            if (admin.getOneCustomer("email@email.com", "password").getId() > 0) {
                System.out.println(" - success");
            } else {
                System.out.println(" - failed");
            }
        } catch (SQLException | CustomerException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void updateCustomer() {
        try {
            AdminFacade admin = new AdminFacade();
            Customer customer = admin.getOneCustomer(71);
            customer.setEmail("XnewEmail@new.mail");
            customer.setPassword("newPassword");
            System.out.print("Update a new customer ");
            admin.updateCustomer(customer);
            System.out.println(" - success");
        } catch (SQLException |CustomerException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void deleteCustomer() {
        try {
            System.out.print("delete selected customer ");
            AdminFacade admin = new AdminFacade();
            if(admin.getOneCustomer(60) !=null) {
                admin.deleteCustomer(60);
                if(admin.getOneCustomer(60) == null){
                    System.out.println(" - success");
                }else
                    System.out.println(" - failed");
            }else
                System.out.println(" - failed");

        } catch (CustomerException | SQLException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void getAllCustomers() {
        try {
            AdminFacade admin = new AdminFacade();
            System.out.print("getting all customers ");
            if(admin.getAllCustomers() !=null)
                System.out.println(" - success");
            else
                System.out.println(" - failed");
        } catch (CustomerException|SQLException e) {
            System.out.println(" - failed");
        }
    }
}