package FACADE;

import DAO.CompaniesDBDAO;
import DAO.CouponsDBDAO;
import DAO.CustomersDBDAO;
import java.sql.SQLException;

 public abstract class ClientFacade {
     protected CompaniesDBDAO companiesDbDao ;
     protected  CustomersDBDAO customersDbDao;
     protected CouponsDBDAO couponsDbDao;

     public ClientFacade() throws SQLException {
         this.companiesDbDao = new CompaniesDBDAO();
         this.customersDbDao = new CustomersDBDAO();
         this.couponsDbDao = new CouponsDBDAO();
     }
     /**
      * Abstract method for login functionality.
      *
      * @param email    the email of the user.
      * @param password the password of the user.
      * @return boolean indicating if the login was successful.
      */
     public abstract int login(String email, String password);


}
