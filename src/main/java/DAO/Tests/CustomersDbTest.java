package DAO.Tests;

import BEANS.Customer;
import BEANS.CustomerException;
import DAO.CustomersDBDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class CustomersDbTest {
    public static void main(String[] args) throws CustomerException, SQLException {


    Random rnd =new Random();
    CustomersDBDAO customersDbDao = new CustomersDBDAO();


    System.out.println("////////////////////////////////customers////////////////////////////////////////////");
    Customer[] customers =new Customer[5];
        for (int i = 0; i <5 ; i++) {
        //(String firstName, String lastName, String email, String password, ArrayList<BEANS.Coupon> coupons)
        customers[i] = new Customer("firstName"+i,"lastName",
                i+"@y.co.il",
                "" + rnd.nextInt(1000,999999),
                null);
    }

   System.out.println("**addCustomer**");
        for (Customer customer : customers) {
            customersDbDao.addCustomer(customer);
        }

        System.out.println("**getSelectedCustomer**");
        customersDbDao.getSelectedCustomer(1);
        Customer tempCustomer =customersDbDao.getSelectedCustomer(1);
        tempCustomer.setId(2);
        System.out.println("**deleteCustomer**");
        customersDbDao.deleteCustomer(1);
        System.out.println("**updateCompany**");
        customersDbDao.updateCustomer(tempCustomer);
        System.out.println("**getSelectedCompany**");
        customersDbDao.getSelectedCustomer(2);
        System.out.println("**getAllCompanies**");
        ArrayList<Customer> allCustomers = customersDbDao.getAllCustomers();
        for (Customer allCustomer : allCustomers) {
            System.out.println(allCustomer.toString());
        }

    }

}

