package DAO.Tests;

import BEANS.Customer;
import BEANS.CustomerException;
import DAO.CustomersDBDAO;
import com.sun.tools.javac.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class CustomersDbTest {
    public static void main(String[] args) throws CustomerException, SQLException {
        final Logger logger = LogManager.getLogger(Main.class);


        logger.trace("Trace message");
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message");
        logger.error("Error message");

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
        customersDbDao.getSelectedCustomer(5);
        Customer tempCustomer =customersDbDao.getSelectedCustomer(5);
        System.out.println("**deleteCustomer**");
        customersDbDao.deleteCustomer(7);
        System.out.println("**updateCompany**");
        customersDbDao.updateCustomer(tempCustomer);
        System.out.println("**getSelectedCompany**");
        customersDbDao.getSelectedCustomer(8);
        System.out.println("**getAllCompanies**");
        ArrayList<Customer> allCustomers = customersDbDao.getAllCustomers();
        for (Customer allCustomer : allCustomers) {
            System.out.println(allCustomer.toString());
        }

    }

}

