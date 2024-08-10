package FACADE;

import BEANS.CompanyException;
import BEANS.CouponException;
import BEANS.CustomerException;
import DAO.CompaniesDBDAO;
import DAO.CouponsDBDAO;
import DAO.CustomersDBDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;


public abstract class ClientFacade {
     protected CompaniesDBDAO companiesDbDao ;
     protected  CustomersDBDAO customersDbDao;
     protected CouponsDBDAO couponsDbDao;
    protected static final Logger logger = LogManager.getLogger();

     public ClientFacade() throws SQLException {
         this.companiesDbDao = new CompaniesDBDAO();
         this.customersDbDao = new CustomersDBDAO();
         this.couponsDbDao = new CouponsDBDAO();
         logger.info("ClientFacade");
     }
     /**
      * Abstract method for login functionality.
      *
      * @param email    the email of the user.
      * @param password the password of the user.
      * @return boolean indicating if the login was successful.
      */
     public abstract int login(String email, String password) throws CouponException, CustomerException, CompanyException;


}
