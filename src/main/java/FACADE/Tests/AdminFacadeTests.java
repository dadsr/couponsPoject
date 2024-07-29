package FACADE.Tests;

import BEANS.CategoryEnum;
import BEANS.Company;
import BEANS.Coupon;
import BEANS.Customer;
import DAO.CompaniesDBDAO;
import FACADE.AdminFacade;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class AdminFacadeTests {
    public static void main(String[] args) throws SQLException {
        Random rnd =new Random();
        AdminFacade  admin = new AdminFacade();

        System.out.println("////////////////////////////////companies////////////////////////////////////////////");
        Company[] companies =new Company[5];
        for (int i = 0; i <5 ; i++) {
            //(String name, String email, String password, ArrayList<BEANS.Coupon> coupons)
            companies[i] = new Company("comp"+i,
                    i+"@y.co.il",
                    "" + rnd.nextInt(1000,999999),
                    null);
        }
        System.out.println("addCompany");
        for (Company comp : companies) {
            admin.addCompany(comp);
        }
        Customer[] customers =new Customer[5];

        for (int i = 0; i <5 ; i++) {
            customers[i] =new Customer("Fname"+i,"Lname","XXX"+i+"@GGG.COM","ABC"+i,null);
        }
        System.out.println("addCustomer");
        for (Customer customer : customers) {
            admin.addCustomer(customer);
        }
        System.out.println("getAllCompanies");
        admin.getAllCompanies();
        System.out.println("getAllCustomer");
        admin.getAllCustomer();
        System.out.println("getOneCompany");
        admin.getOneCompany(4);
        System.out.println("getOneCustomer");
        admin.getOneCustomer(3);
    }
}
