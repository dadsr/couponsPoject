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
    /**
     * Provides access to the singleton instance of the `LoginManager` class.
     * <p>
     * This method ensures that only one instance of the `LoginManager` is created
     * by using the Singleton design pattern. If the instance does not already exist,
     * it is created; otherwise, the existing instance is returned.
     *
     * @return The single instance of the `LoginManager` class.
     */
    public static LoginManager getInstance() {
        logger.info("getInstance");
        if(instance == null)
            instance = new LoginManager();
        return instance;
    }
    /**
     * Authenticates a user based on their email, password, and client type, and returns the appropriate `ClientFacade` instance.
     * <p>
     * This method performs authentication for different types of clients (Administrator, Company, Customer) by
     * delegating the login process to the specific facade class corresponding to the client type.
     * It returns the corresponding `ClientFacade` instance if the login is successful, or `null` if authentication fails.
     *
     * @param email The email of the client trying to log in.
     * @param password The password of the client trying to log in.
     * @param clientType The type of client (ADMINISTRATOR, COMPANY, CUSTOMER) attempting to log in.
     * @return A `ClientFacade` instance corresponding to the client type if authentication is successful; `null` otherwise.
     * @throws LoginException If an error occurs during the login process specific to client type exceptions.
     * @throws RuntimeException If a SQL error occurs during the login process.
     */
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
