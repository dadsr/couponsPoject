package PROGRAM;

import BEANS.ClientTypeEnum;
import BEANS.CompanyException;
import BEANS.CustomerException;
import BEANS.LoginException;
import FACADE.AdminFacade;
import FACADE.ClientFacade;
import FACADE.CompanyFacade;
import FACADE.CustomerFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class LoginManager {
    private static LoginManager instance;
    protected static final Logger logger = LogManager.getLogger();


    private LoginManager() {
        logger.info("LoginManager");
    }
    public static LoginManager getInstance() {
        logger.info("getInstance");
        if(instance == null)
            instance = new LoginManager();
        return instance;
    }

    public ClientFacade login (String email, String password , ClientTypeEnum clientType) throws LoginException {
        logger.info("login");
        try {
            switch (clientType.name()) {
                case "ADMINISTRATOR": {
                    AdminFacade admin;
                    admin = new AdminFacade();
                    return (admin.login(email, password) == 1) ? admin : null;
                }
                case "COMPANY": {
                    CompanyFacade comp = new CompanyFacade();
                    comp.setCompanyID(comp.login(email, password));
                    return (comp.getCompanyID() == 0) ? null : comp;
                }
                case "CUSTOMER": {
                    CustomerFacade cust = new CustomerFacade();
                    cust.setCustomerID(cust.login(email, password));
                    return (cust.getCustomerID() != 0) ? cust : null;
                }
                default:
                    return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (CustomerException | CompanyException e) {
            throw new LoginException(e.getMessage());


        }
    }
}
