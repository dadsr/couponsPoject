package FACADE;

import BEANS.*;
import BEANS.CouponException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;


public class AdminFacade extends ClientFacade {
    ClientFacade facade;
    protected static final Logger logger = LogManager.getLogger(AdminFacade.class.getName());


    public AdminFacade() throws SQLException {
        super();
        logger.info("AdminFacade constructor");
    }
    @Override
    public int login(String email, String password) {
        logger.info("login - trying to login using email:{} and password:{}", email, password);
        return (Objects.equals(email, "admin@admin.com") && Objects.equals(password, "admin"))?1:0;
    }
    /**
     * Adds a new company to the database if it does not already exist.
     * <p>
     * This method first checks whether a company with the given email and name
     * already exists in the database by calling `isCompanyExists` on the `companiesDbDao`.
     * If the company does not exist (i.e., the returned ID is `0`), the company is added
     * to the database by invoking `addCompany` on the DAO.
     * <p>
     * If the company already exists (i.e., the returned ID is not `0`), a `CompanyException`
     * is thrown indicating that the company already exists.
     * <p>
     * All actions are logged for auditing purposes, including both the attempt to add
     * the company and any errors encountered.
     *
     * @param company the `Company` object representing the company to be added.
     * @throws CompanyException if the company already exists or if an error occurs during the process.
     */
    public void addCompany(Company company) throws CompanyException {
        logger.info("addCompany - trying to add company name:{} and email:{}",company.getName(),company.getEmail());
        try {
            int id =companiesDbDao.isCompanyExists(company.getEmail(), company.getName());
            if(id == 0)
                companiesDbDao.addCompany(company);
            else {
                logger.error("addCompany - Company already exists - company id :{}", id);
                throw new CompanyException("Company already exists");
            }
        } catch (CompanyException e) {
            logger.error("addCompany threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Updates the details of an existing company in the database.
     * <p>
     * This method first checks if a company with the specified ID exists in the database
     * by calling `isCompanyExists` on the `companiesDbDao`. If the company exists (i.e., the
     * returned ID is greater than `0`), the company details are updated in the database
     * by invoking `updateCompany` on the DAO.
     * <p>
     * If the company does not exist (i.e., the returned ID is `0` or less), a `CompanyException`
     * is thrown indicating that the company does not exist.
     * <p>
     * All actions, including the attempt to update the company and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param company the `Company` object containing the updated details of the company.
     * @throws CompanyException if the company does not exist or if an error occurs during the update process.
     */
    public void updateCompany(Company company) throws CompanyException {
        logger.info("updateCompany - trying to update company id:{}", company.getId());
        try {
            if(companiesDbDao.isCompanyExists(company.getId()) > 0 )
                companiesDbDao.updateCompany(company);
            else {
                logger.error("updateCompany - Company does not exist - company id:{}", company.getId());
                throw new CompanyException("Company does not exist");
            }
        } catch (CompanyException e) {
            logger.error("updateCompany threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Deletes a company and its associated coupons from the database.
     * <p>
     * This method performs a cascade deletion to remove a company and all related data.
     * It first deletes any coupon purchases associated with the specified company ID by
     * calling `deletePurchasesByCompany` on the `couponsDbDao`. Next, it deletes all
     * coupons associated with the company by invoking `deleteCouponsByCompany`.
     * Finally, the company itself is deleted from the database by calling `deleteCompany`
     * on the `companiesDbDao`.
     * <p>
     * If any exception occurs during the deletion process (either a `CompanyException` or a `CouponException`),
     * it is caught, logged, and rethrown as a `CompanyException`.
     * <p>
     * All actions, including the attempt to delete the company and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param companyID the ID of the company to be deleted.
     * @throws CompanyException if an error occurs during the deletion process.
     */
    public void deleteCompany(int companyID) throws CompanyException {
        logger.info("deleteCompany - trying to  delete company id:{}", companyID);
        try {
            couponsDbDao.deletePurchasesByCompany(companyID);
            couponsDbDao.deleteCouponsByCompany(companyID);
            companiesDbDao.deleteCompany(companyID);
        } catch (CompanyException | CouponException e) {
            logger.error("deleteCompany threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Retrieves a list of all companies from the database.
     * <p>
     * This method calls `getAllCompanies` on the `companiesDbDao` to fetch all the companies
     * stored in the database. The result is returned as an `ArrayList` of `Company` objects.
     * <p>
     * If an exception occurs during the retrieval process, it is caught, logged, and rethrown
     * as a `CompanyException`.
     * <p>
     * All actions, including the attempt to retrieve all companies and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @return an `ArrayList` containing all companies in the database.
     * @throws CompanyException if an error occurs during the retrieval process.
     */
    public ArrayList<Company> getAllCompanies() throws CompanyException {
        logger.info("getAllCompanies - trying to get all Companies");
        try {
            return companiesDbDao.getAllCompanies();
        } catch (CompanyException e) {
            logger.error("getAllCompanies threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }

    }
    /**
     * Retrieves a specific company from the database by its ID.
     * <p>
     * This method calls `getSelectedCompany` on the `companiesDbDao` to fetch the company
     * associated with the specified `companyID`. The result is returned as a `Company` object.
     * <p>
     * If an exception occurs during the retrieval process, it is caught, logged, and rethrown
     * as a `CompanyException`.
     * <p>
     * All actions, including the attempt to retrieve the company by its ID and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param companyID the ID of the company to be retrieved.
     * @return the `Company` object representing the company with the specified ID.
     * @throws CompanyException if the company cannot be retrieved or an error occurs during the process.
     */
    public Company getOneCompany(int companyID) throws CompanyException {
        logger.info("getOneCompany -  trying to get company by id:{}", companyID);
        try {
            return companiesDbDao.getSelectedCompany(companyID);
        } catch (CompanyException e) {
            logger.error("getOneCompany threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Retrieves a specific company from the database by its email and password.
     * <p>
     * This method calls `getSelectedCompany` on the `companiesDbDao` to fetch the company
     * associated with the specified email and password. The result is returned as a `Company` object.
     * <p>
     * If an exception occurs during the retrieval process, it is caught, logged, and rethrown
     * as a `CompanyException`.
     * <p>
     * All actions, including the attempt to retrieve the company by its email and password and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param email the email of the company to be retrieved.
     * @param password the password of the company to be retrieved.
     * @return the `Company` object representing the company with the specified email and password.
     * @throws CompanyException if the company cannot be retrieved or an error occurs during the process.
     */
    public Company getOneCompany(String email, String password) throws CompanyException {
        logger.info("getOneCompany -  trying to get company by email:{} and password:{}", email,password);
        try {
            return companiesDbDao.getSelectedCompany(email,password);
        } catch (CompanyException e) {
            logger.error("getOneCompany threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    /**
     * Adds a new customer to the database if the customer does not already exist.
     * <p>
     * This method first checks whether a customer with the given email already exists in the database
     * by calling `isCustomerExists` on the `customersDbDao`. If the customer does not exist,
     * the customer is added to the database by invoking `addCustomer` on the DAO.
     * <p>
     * If an exception occurs during the process, it is caught, logged, and rethrown
     * as a `CustomerException`.
     * <p>
     * All actions, including the attempt to add the customer and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param customer the `Customer` object representing the customer to be added.
     * @throws CustomerException if an error occurs during the process of adding the customer.
     */
    public void addCustomer(Customer customer) throws CustomerException {
        logger.info("addCustomer - trying to add new customer - customer name:{} {}", customer.getFirstName(), customer.getLastName());
        try {
            if(!customersDbDao.isCustomerExists(customer.getEmail()))
                customersDbDao.addCustomer(customer);
        } catch (CustomerException e) {
            logger.error("addCustomer threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Updates the details of an existing customer in the database.
     * <p>
     * This method first checks if a customer with the specified email and password exists in the database
     * by calling `isCustomerExists` on the `customersDbDao`. If the customer exists (i.e., the returned value is not `0`),
     * the customer's details are updated in the database by invoking `updateCustomer` on the DAO.
     * <p>
     * If an exception occurs during the update process, it is caught, logged, and rethrown as a `CustomerException`.
     * <p>
     * All actions, including the attempt to update the customer and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param customer the `Customer` object containing the updated details of the customer.
     * @throws CustomerException if the customer does not exist or if an error occurs during the update process.
     */
    public void updateCustomer(Customer customer) throws CustomerException {
        logger.info("updateCustomer - trying to update customer - customer id:{}", customer.getId());
        try {
            if(customersDbDao.isCustomerExists(customer.getEmail(),customer.getPassword()) != 0)
                customersDbDao.updateCustomer(customer);
        } catch (CustomerException e) {
            logger.error("updateCustomer threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Deletes a customer and their associated coupon purchases from the database.
     * <p>
     * This method performs a cascade deletion to remove a customer and all related data.
     * It first deletes any coupon purchases associated with the specified customer ID by
     * calling `deletePurchasesByCustomer` on the `couponsDbDao`. Then, the customer is deleted
     * from the database by calling `deleteCustomer` on the `customersDbDao`.
     * <p>
     * If an exception occurs during the deletion process (either a `CouponException` or a `CustomerException`),
     * it is caught, logged, and rethrown as a `CustomerException`.
     * <p>
     * All actions, including the attempt to delete the customer and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param customerID the ID of the customer to be deleted.
     * @throws CustomerException if an error occurs during the deletion process.
     */
    public void deleteCustomer(int customerID) throws CustomerException {
        logger.info("deleteCustomer - trying to delete customer - customer id:{}", customerID);
        try {
            couponsDbDao.deletePurchasesByCustomer(customerID);
            customersDbDao.deleteCustomer(customerID);
        } catch (CouponException | CustomerException e) {
            logger.error("deleteCustomer threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Retrieves a list of all customers from the database.
     * <p>
     * This method calls `getAllCustomers` on the `customersDbDao` to fetch all the customers
     * stored in the database. The result is returned as an `ArrayList` of `Customer` objects.
     * <p>
     * If an exception occurs during the retrieval process, it is caught, logged, and rethrown
     * as a `CustomerException`.
     * <p>
     * All actions, including the attempt to retrieve all customers and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @return an `ArrayList` containing all customers in the database.
     * @throws CustomerException if an error occurs during the retrieval process.
     */
    public ArrayList<Customer> getAllCustomers() throws CustomerException {
        logger.info("getAllCustomers - trying to get all customer");
        try {
            return customersDbDao.getAllCustomers();
        } catch (CustomerException e) {
            logger.error("getAllCustomers threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Retrieves a specific customer from the database by their ID.
     * <p>
     * This method calls `getSelectedCustomer` on the `customersDbDao` to fetch the customer
     * associated with the specified `customerID`. The result is returned as a `Customer` object.
     * <p>
     * If an exception occurs during the retrieval process, it is caught, logged, and rethrown
     * as a `CustomerException`.
     * <p>
     * All actions, including the attempt to retrieve the customer by their ID and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param customerID the ID of the customer to be retrieved.
     * @return the `Customer` object representing the customer with the specified ID.
     * @throws CustomerException if the customer cannot be retrieved or an error occurs during the process.
     */
    public Customer getOneCustomer(int customerID) throws CustomerException {
        logger.info("getOneCustomer - trying to get customer by customer id:{}", customerID);
        try {
            return customersDbDao.getSelectedCustomer(customerID);
        } catch (CustomerException e) {
            logger.error("getOneCustomer threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
    /**
     * Retrieves a specific customer from the database using their email and password.
     * <p>
     * This method calls `getSelectedCustomer` on the `customersDbDao` to fetch the customer
     * associated with the specified email and password. The result is returned as a `Customer` object.
     * <p>
     * If an exception occurs during the retrieval process, it is caught, logged, and rethrown
     * as a `CustomerException`.
     * <p>
     * All actions, including the attempt to retrieve the customer by their email and password and any errors encountered,
     * are logged for auditing and debugging purposes.
     *
     * @param email the email of the customer to be retrieved.
     * @param password the password of the customer to be retrieved.
     * @return the `Customer` object representing the customer with the specified email and password.
     * @throws CustomerException if the customer cannot be retrieved or an error occurs during the process.
     */
    public Customer getOneCustomer(String email, String password) throws CustomerException {
        logger.info("getOneCustomer - trying to get customer by customer email:{} and password:{}", email,password);
        try {
            return customersDbDao.getSelectedCustomer(email,password);
        } catch (CustomerException e) {
            logger.error("getOneCustomer threw exception - {}", e.getMessage());
            throw new CustomerException(e.getMessage());
        }
    }
//
}
