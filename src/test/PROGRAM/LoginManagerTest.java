package PROGRAM;

import FACADE.ClientFacade;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginManagerTest {

    @Test
    void login() {
        try {
            //ADMINISTRATOR
            if(LoginManager.getInstance().login("badMail@admin.com","admin", ClientTypeEnum.ADMINISTRATOR) ==null){
                System.out.println("bad email - success");
            }else
                System.out.println("bad email - faild");
            if(LoginManager.getInstance().login("admin@admin.com","badpass", ClientTypeEnum.ADMINISTRATOR)==null){
                System.out.println("bad password - success");
            }else
                System.out.println("bad password - faild");

            ClientFacade admin = LoginManager.getInstance().login("admin@admin.com","admin", ClientTypeEnum.ADMINISTRATOR);
            if(admin.getClass().toString().equals("class FACADE.AdminFacade")){
                System.out.println("ADMINISTRATOR login - success");
            } else{
                System.out.println("ADMINISTRATOR login - faild");
            }

            //COMPANY

            ClientFacade company = LoginManager.getInstance().login("4@y.co.il","25201",ClientTypeEnum.COMPANY);
            //CUSTOMER
            ClientFacade customer = LoginManager.getInstance().login("0@y.co.il","97198",ClientTypeEnum.CUSTOMER);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}