package PROGRAM;

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
    }
    public static LoginManager getInstance() {
        if(instance == null)
            instance = new LoginManager();
        return instance;
    }
    public ClientFacade login (String email, String password , ClientTypeEnum clientType) throws SQLException {
        logger.info("starting login");
        switch (clientType.name()){
            case "ADMINISTRATOR" :{
                AdminFacade admin = new AdminFacade();
                return (admin.login(email,password)!=1)?admin:null;
            }
            case "COMPANY" :{
                CompanyFacade comp = new CompanyFacade();
                comp.setCompanyID(comp.login(email,password));
                return  (comp.getCompanyID() == 0)?null:comp;
            }
            case "CUSTOMER" :{
                CustomerFacade cust = new CustomerFacade();
                cust.setCustomerID(cust.login(email,password));
                return (cust.getCustomerID()!=0)?cust:null;
            } default:
                return null;
        }

    }
}
