package FACADE;

import BEANS.*;
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
        logger.info("AdminFacade");
    }
    @Override
    public int login(String email, String password) {
        logger.info("login");
        return (Objects.equals(email, "admin@admin.com") && Objects.equals(password, "admin"))?1:0;
    }
    public void addCompany(Company company){
        logger.info("addCompany");
        try {
            if(companiesDbDao.isCompanyExists(company.getEmail(), company.getName()) == 0)
                companiesDbDao.addCompany(company);
            else {
                logger.error("addCompany - Company already exists");
                throw new CompanyException("Company already exists");
            }
        } catch (CompanyException e) {
            logger.error("addCompany {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
    public void updateCompany(Company company){
        logger.info("updateCompany");
        try {
            if(companiesDbDao.isCompanyExists(company.getEmail(), company.getName()) !=0 )
                companiesDbDao.updateCompany(company);
            else {
                logger.error("updateCompany - Company does not exist");
                throw new CompanyException("Company does not exist");
            }
        } catch (CompanyException e) {
            logger.error("updateCompany {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void deleteCompany(int companyID){
        logger.info("deleteCompany");
        try {
            couponsDbDao.deletePurchasesByCompany(companyID);
            couponsDbDao.deleteCouponsByCompany(companyID);
            companiesDbDao.deleteCompany(companyID);
        } catch (CompanyException | CouponException e) {
            logger.error("deleteCompany {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Company> getAllCompanies(){
        logger.info("getAllCompanies");
        try {
            return companiesDbDao.getAllCompanies();
        } catch (CompanyException e) {
            logger.error("getAllCompanies {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
    public Company getOneCompany(int companyID) throws CompanyException {
        logger.info("getOneCompany1");
        try {
            return companiesDbDao.getSelectedCompany(companyID);
        } catch (CompanyException e) {
            logger.error("getOneCompany1 {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
    public Company getOneCompany(String email, String password){
        logger.info("getOneCompany2");
        try {
            return companiesDbDao.getSelectedCompany(email,password);
        } catch (CompanyException e) {
            logger.error("getOneCompany2 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void addCustomer(Customer customer){
        logger.info("addCustomer");
        try {
            if(!customersDbDao.isCustomerExists(customer.getEmail()))
                customersDbDao.addCustomer(customer);
        } catch (CustomerException e) {
            logger.error("addCustomer {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void updateCustomer(Customer customer){
        logger.info("updateCustomer");
        try {
            if(customersDbDao.isCustomerExists(customer.getEmail(),customer.getPassword()) != 0)
                customersDbDao.updateCustomer(customer);
        } catch (CustomerException e) {
            logger.error("updateCustomer {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void deleteCustomer(int customerID){
        logger.info("deleteCustomer");
        try {
            couponsDbDao.deletePurchasesByCustomer(customerID);
            customersDbDao.deleteCustomer(customerID);
        } catch (CouponException | CustomerException e) {
            logger.error("deleteCustomer {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
    public ArrayList<Customer> getAllCustomers(){
        logger.info("getAllCustomers");
        try {
            return customersDbDao.getAllCustomers();
        } catch (CustomerException e) {
            logger.error("getAllCustomers {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public Customer getOneCustomer(int customerID){
        logger.info("getOneCustomer");
        try {
            return customersDbDao.getSelectedCustomer(customerID);
        } catch (CustomerException e) {
            logger.error("getOneCustomer {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
