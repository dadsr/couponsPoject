package MAIN;

import BEANS.*;
import FACADE.AdminFacade;
import FACADE.CompanyFacade;
import FACADE.CustomerFacade;
import BEANS.ClientTypeEnum;
import PROGRAM.LoginManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

public class mainTestsFlow {
    protected static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        LoginManager login =  LoginManager.getInstance();
        AdminFacade admin;
        try {
            //admin@admin.com & admin
            admin = (AdminFacade) login.login("admin@admin.com","admin", ClientTypeEnum.ADMINISTRATOR);

/*            System.out.println("-------------------------- Adding new companies ------------------------------------");
            addNewCompanies(admin,20);
            System.out.println("-------------------------- Adding new coupons for company---------------------------");
            addNewCoupons4Companies(login,admin,50);
            System.out.println("-------------------------- Adding new clients --------------------------------------");
            addNewClients(admin,30);
            System.out.println("-------------------------- purchasing coupons --------------------------------------");
            */purchasingCoupons(login,admin,100);
            System.out.println("-------------------------- BIG SUCCESS --------------------------------------");


        } catch (LoginException | CompanyException  e) {
            System.out.println(e.getMessage());
        } catch (CustomerException e) {
            throw new RuntimeException(e);
        }
    }


    private static void addNewClients(AdminFacade admin, int numOf) throws CustomerException {
        Random rnd =new Random();
        for (int i = 0; i < 30; i++) {
            admin.addCustomer(new Customer(
                    "custo" + rnd.nextInt(666),//String firstName
                    "mer",//String lastName
                    rnd.nextInt(666) + "@XXX.com",//String email
                    String.valueOf(rnd.nextInt(111111,999999999)),//String password
                    null//ArrayList<Coupon> coupons
            ));
        }
    }


    private static void addNewCompanies(AdminFacade admin, int numOf) {
        Random rnd =new Random();
        Company[] companies =new Company[numOf];
        for (int i = 0; i < companies.length ; i++) {
            //String name, String email, String password, ArrayList<Coupon> coupons
            companies[i] = new Company("comp" +i,
                    "user" + i + "@comp" + i + ".co.il",
                    "" + rnd.nextInt(1000,99999999),
                    null);
        }
        for (Company company : companies) {
            try{
                admin.addCompany(company);
            } catch (CompanyException e) {
                System.out.println("Adding new companies - FAILED");
            }
        }
    }

    private static void addNewCoupons4Companies(LoginManager login,AdminFacade admin, int maxCup) throws CompanyException, LoginException {
        Random rnd =new Random();
        ArrayList<Company> cmps4cups =admin.getAllCompanies();

        for (Company cmp4cups : cmps4cups) {
            CompanyFacade comp = (CompanyFacade) login.login(cmp4cups.getEmail(),cmp4cups.getPassword() ,ClientTypeEnum.COMPANY);

            Coupon[] coupons =new Coupon[rnd.nextInt(1,++maxCup)];
            for (int i = 0; i < coupons.length ; i++) {
                //int companyId, CategoryEnum category, String title, String description, Date startDate, Date endDate, int amount, Double price, String image
                coupons[i] = new Coupon(
                        comp.getCompanyID(),//int companyId
                        CategoryEnum.fromId(rnd.nextInt(1,CategoryEnum.values().length)),// CategoryEnum category
                        "coupon " + i,// String title
                        "all coupons are the same",// String description
                        RandomGenerators.dateGenerator("2024-08-06","2024-12-31"),// Date startDate
                        RandomGenerators.dateGenerator("2025-08-06","2026-12-31"),// Date endDate
                        rnd.nextInt(1,100),// int amount
                        rnd.nextDouble(5.0,50.99),// Double price
                        "C:\\Users\\speedy\\Documents\\coupons"// String image
                );
            }
            for (Coupon coupon : coupons) {
                try{
                    comp.addCoupon(coupon);
                } catch (CouponException e) {
                    System.out.println("Adding new companies - FAILED");
                }
            }
        }

    }
    private static void purchasingCoupons(LoginManager login,AdminFacade admin, int numOf) throws LoginException, CompanyException, CustomerException {
        Random rnd =new Random();
        ArrayList<Customer> customers = admin.getAllCustomers();
        ArrayList<Company> cmps4cups = admin.getAllCompanies();
        for (Customer customer : customers) {
            CustomerFacade clintF = (CustomerFacade) login.login(customer.getEmail(),customer.getPassword(), ClientTypeEnum.CUSTOMER);
            for (int i = 0; i <rnd.nextInt(10) ; i++) {
                int bound =rnd.nextInt((cmps4cups.size()-1));
                ArrayList<Coupon> tempCompCups = cmps4cups.get(bound).getCoupons();
                bound =tempCompCups.size();
                //In case there is a single coupon
                if(bound > 0) {
                    bound -= ((bound == 1) ? 0 : 1);
                    Coupon cup = tempCompCups.get(bound);
                    try {
                        clintF.purchaseCoupons(cup);
                    } catch (CustomerException e) {
                        logger.info("purchaseCoupons" +e.getMessage());

                    }
                }
            }
        }
    }
}

