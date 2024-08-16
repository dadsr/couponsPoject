package MAIN;

import BEANS.*;
import FACADE.AdminFacade;
import FACADE.CompanyFacade;
import FACADE.CustomerFacade;
import BEANS.ClientTypeEnum;
import BEANS.CouponException;
import PROGRAM.CouponExpirationDailyJob;
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
           System.out.println("-------------------------- Starting Coupon Expiration Daily Job ------------------------------------");
            CouponExpirationDailyJob expirationJob = new CouponExpirationDailyJob(600L);
            Thread expirationJobThread = new Thread(expirationJob);

            expirationJobThread.join();
            expirationJobThread.start();




            System.out.println(" ██████╗ ███████╗███╗   ██╗███████╗██████╗  █████╗ ████████╗██╗███╗   ██╗ ██████╗     ██████╗  █████╗ ████████╗ █████╗ \n" +
                    "██╔════╝ ██╔════╝████╗  ██║██╔════╝██╔══██╗██╔══██╗╚══██╔══╝██║████╗  ██║██╔════╝     ██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗\n" +
                    "██║  ███╗█████╗  ██╔██╗ ██║█████╗  ██████╔╝███████║   ██║   ██║██╔██╗ ██║██║  ███╗    ██║  ██║███████║   ██║   ███████║\n" +
                    "██║   ██║██╔══╝  ██║╚██╗██║██╔══╝  ██╔══██╗██╔══██║   ██║   ██║██║╚██╗██║██║   ██║    ██║  ██║██╔══██║   ██║   ██╔══██║\n" +
                    "╚██████╔╝███████╗██║ ╚████║███████╗██║  ██║██║  ██║   ██║   ██║██║ ╚████║╚██████╔╝    ██████╔╝██║  ██║   ██║   ██║  ██║\n" +
                    " ╚═════╝ ╚══════╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝  ╚═══╝ ╚═════╝     ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝\n" +
                    "    ███████╗ ██████╗ ██████╗     ████████╗███████╗███████╗████████╗██╗███╗   ██╗ ██████╗                               \n" +
                    "    ██╔════╝██╔═══██╗██╔══██╗    ╚══██╔══╝██╔════╝██╔════╝╚══██╔══╝██║████╗  ██║██╔════╝                               \n" +
                    "    █████╗  ██║   ██║██████╔╝       ██║   █████╗  ███████╗   ██║   ██║██╔██╗ ██║██║  ███╗                              \n" +
                    "    ██╔══╝  ██║   ██║██╔══██╗       ██║   ██╔══╝  ╚════██║   ██║   ██║██║╚██╗██║██║   ██║                              \n" +
                    "    ██║     ╚██████╔╝██║  ██║       ██║   ███████╗███████║   ██║   ██║██║ ╚████║╚██████╔╝                              \n" +
                    "    ╚═╝      ╚═════╝ ╚═╝  ╚═╝       ╚═╝   ╚══════╝╚══════╝   ╚═╝   ╚═╝╚═╝  ╚═══╝ ╚═════╝                               ");

           System.out.println("--------------------------1. Adding new companies ------------------------------------");
            //admin@admin.com & admin
            admin = (AdminFacade) login.login("admin@admin.com","admin", ClientTypeEnum.ADMINISTRATOR);

            addNewCompanies(admin,20);
            System.out.println("--------------------------2. Adding new coupons for company---------------------------");
            addNewCoupons4Companies(login,admin,20);
            System.out.println("--------------------------3. Adding new clients --------------------------------------");
            addNewClients(admin,20);
            System.out.println("--------------------------4. purchasing coupons --------------------------------------");
            purchasingCoupons(login,admin,20);


            System.out.println("███████╗██╗   ██╗ ██████╗ ██████╗███████╗███████╗███████╗\n" +
                    "██╔════╝██║   ██║██╔════╝██╔════╝██╔════╝██╔════╝██╔════╝\n" +
                    "███████╗██║   ██║██║     ██║     █████╗  ███████╗███████╗\n" +
                    "╚════██║██║   ██║██║     ██║     ██╔══╝  ╚════██║╚════██║\n" +
                    "███████║╚██████╔╝╚██████╗╚██████╗███████╗███████║███████║\n" +
                    "╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝╚══════╝╚══════╝╚══════╝");

            System.out.println("--------------------------5. ADMIN FACADE --------------------------------------");
          int testCompany1 = 1,testCompany2 = 2 ,testCustomer1 =1,testCustomer2 =2;


            //updateCompany + getOneCompany
            System.out.println("using admin getOneCompany");

            Company meinComp = admin.getOneCompany(testCompany1);
            meinComp.setEmail("meinComp@Juice.com");
            meinComp.setPassword("killAllJuice");
            System.out.println("using admin updateCompany");
            admin.updateCompany(meinComp);
            if(meinComp.compare(meinComp,admin.getOneCompany("meinComp@Juice.com","killAllJuice")) == 1)
                System.out.println("updateCompany + getOneCompany - OK");
            else
                System.out.println("updateCompany + getOneCompany - FAILURE ");

            //deleteCompany
            int compId =admin.getOneCompany("meinComp@Juice.com","killAllJuice").getId();
            System.out.println("using admin deleteCompany");
            admin.deleteCompany(compId);
            try {
                admin.getOneCompany(compId);
                System.out.println("deleteCompany - FAILURE");
            }catch (CompanyException e) {
                System.out.println("deleteCompany - OK");
            }

            //updateCustomer + getOneCustomer
            System.out.println("using admin getOneCustomer");
            Customer mcLovin  = admin.getOneCustomer(testCustomer1);
            mcLovin.setEmail("mcLovin@superbad.com");
            mcLovin.setPassword("mcLovinMcLovin");
            System.out.println("using admin updateCustomer");
            admin.updateCustomer(mcLovin);
            if(mcLovin.compare(mcLovin,admin.getOneCustomer("mcLovin@superbad.com","mcLovinMcLovin")) == 1)
                System.out.println("updateCustomer + getOneCustomer - OK");
            else
                System.out.println("updateCustomer + getOneCustomer - FAILURE ");
            //deleteCustomer
            int custId = admin.getOneCustomer("mcLovin@superbad.com","mcLovinMcLovin").getId();
            System.out.println("using admin deleteCustomer");
            admin.deleteCustomer(custId);
            try {
                admin.getOneCustomer(compId);
                System.out.println("deleteCustomer - FAILURE");
            }catch (CustomerException e) {
                System.out.println("deleteCustomer - OK");
            }

            System.out.println("--------------------------6. COMPANY FACADE --------------------------------------");

            Company company= admin.getOneCompany(testCompany2);
            CompanyFacade companyFacade = (CompanyFacade) login.login(company.getEmail(),company.getPassword() ,ClientTypeEnum.COMPANY);
            //updateCoupon + getCompanyCoupons
            System.out.println("using company getCompanyCoupons");
            ArrayList<Coupon> coupons = companyFacade.getCompanyCoupons();
            Coupon coupon = coupons.get(1);
            coupon.setDescription("DescriptionDescriptionDescriptionDescription");
            coupon.setCategory(CategoryEnum.ELECTRONICS);
            coupon.setEndDate( RandomGenerators.dateGenerator("2029-01-01","2029-12-31"));
            try {
                System.out.println("using company updateCoupon");
                companyFacade.updateCoupon(coupon);
                coupons = companyFacade.getCompanyCoupons();
                for (Coupon coupon1 : coupons) {
                    if (coupon.getId() == coupon1.getId()) {
                        if (coupon.compare(coupon, coupon1) == 1) {
                            System.out.println("updateCoupon + getCompanyCoupons - OK");
                            break;
                        }
                        System.out.println("updateCoupon + getCompanyCoupons - FAILURE ");
                    }
                }
            } catch (CouponException|CompanyException e) {
                System.out.println("updateCoupon + getCompanyCoupons - FAILURE ");
            }

            //deleteCoupon
            try {
                System.out.println("using company deleteCoupon");
                companyFacade.deleteCoupon(coupon.getId());
                coupons = companyFacade.getCompanyCoupons();
                int i = 0;
                for (; i < coupons.size(); i++)
                    if(coupon.getId()==coupons.get(i).getId())
                        break;
                System.out.println("deleteCoupon - " + ((i == coupons.size())?"OK":"FAILURE"));
            }catch (CouponException e) {
                System.out.println("deleteCoupon - FAILURE");
            }
            //getCompanyDetails
            System.out.println("using company getCompanyDetails");
            company = companyFacade.getCompanyDetails();
            System.out.println("getCompanyDetails - " + ((companyFacade.getCompanyID()==company.getId())?"OK":"FAILURE"));

            System.out.println("--------------------------7. CUSTOMER FACADE --------------------------------------");
            Customer customer =admin.getOneCustomer(testCustomer2);
            CustomerFacade customerFacade = (CustomerFacade) login.login(customer.getEmail(),customer.getPassword() ,ClientTypeEnum.CUSTOMER);
            //getCustomerID
            System.out.println("using customer getCustomerID");
            System.out.println("getCustomerID - " + ((customerFacade.getCustomerID() == customer.getId())?"OK":"FAILURE"));
            //getCustomerCoupons by max price
            company = companyFacade.getCompanyDetails();
            coupons = company.getCoupons();
            for (int i = 0; i <5 ; i++) {
                coupons.add(addCouponByPriceAndCategory(company.getId(),10.0 +i,CategoryEnum.FASHION));
            }
            company.setCoupons(coupons);
            System.out.println("using customer getCustomerCoupons by Max Price");
            ArrayList<Coupon> custMaxCoups= customerFacade.getCustomerCoupons(10.0);
            int i = 0;
            for (i = 0; i <custMaxCoups.size() ; i++)
                if(custMaxCoups.get(i).getPrice()>10.0)
                    break;
            System.out.println("getCustomerCoupons by Max Price - " + ((i == custMaxCoups.size())?"OK":"FAILURE"));
            //getCustomerCoupons by category
            for (int j = 0; j < 5 ; j++) {
                coupons.add(addCouponByPriceAndCategory(company.getId(),0.0,CategoryEnum.ELECTRONICS));
            }
            company.setCoupons(coupons);
            System.out.println("using customer getCustomerCoupons by Category");
            ArrayList<Coupon> custCategoryCoups= customerFacade.getCustomerCoupons(CategoryEnum.ELECTRONICS);

            for (i = 0; i <custCategoryCoups.size() ; i++)
                if(!CategoryEnum.ELECTRONICS.equals(custCategoryCoups.get(i).getCategory()))
                    break;
            System.out.println("getCustomerCoupons by Category - " + ((i == custCategoryCoups.size())?"OK":"FAILURE"));
            //getCustomerDetails
            customer = customerFacade.getCustomerDetails();
            System.out.println("getCustomerDetails - " + ((customerFacade.getCustomerID() == customer.getId())?"OK":"FAILURE"));
            System.out.println("-----------------------------------------------------------------------------------");

            System.out.println("--------------------------8. stopping Coupon Expiration Daily Job ------------------------------------");
            //using my method  to stop run() by using CouponExpirationDailyJob reference
            expirationJob.stop();


            System.out.println("██████╗ ███████╗██████╗ ███████╗███████╗ ██████╗████████╗    ██████╗ ██████╗  ██████╗      ██╗███████╗ ██████╗████████╗\n" +
                    "██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔════╝╚══██╔══╝    ██╔══██╗██╔══██╗██╔═══██╗     ██║██╔════╝██╔════╝╚══██╔══╝\n" +
                    "██████╔╝█████╗  ██████╔╝█████╗  █████╗  ██║        ██║       ██████╔╝██████╔╝██║   ██║     ██║█████╗  ██║        ██║   \n" +
                    "██╔═══╝ ██╔══╝  ██╔══██╗██╔══╝  ██╔══╝  ██║        ██║       ██╔═══╝ ██╔══██╗██║   ██║██   ██║██╔══╝  ██║        ██║   \n" +
                    "██║     ███████╗██║  ██║██║     ███████╗╚██████╗   ██║       ██║     ██║  ██║╚██████╔╝╚█████╔╝███████╗╚██████╗   ██║   \n" +
                    "╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝     ╚══════╝ ╚═════╝   ╚═╝       ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚════╝ ╚══════╝ ╚═════╝   ╚═╝   ");

        } catch (LoginException | CompanyException  e) {
            System.out.println(e.getMessage());
        } catch (CustomerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }



    private static void addNewClients(AdminFacade admin, int numOf) throws CustomerException {
        Random rnd =new Random();
        for (int i = 0; i < numOf; i++) {
            admin.addCustomer(new Customer(
                    "custo" + rnd.nextInt(666666),//String firstName
                    "mer",//String lastName
                    rnd.nextInt(666666) + "@XXX.com",//String email
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
                logger.info("addNewCompanies {} ", e.getMessage());
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
                    logger.info("addNewCoupons4Companies {} ", e.getMessage());
                    System.out.println("Adding new companies - FAILED");
                }
            }
        }

    }

    private static Coupon addCouponByPriceAndCategory(int companyId,double price, CategoryEnum categoryEnum) {
        Random rnd =new Random();
        return new Coupon(
                companyId,//int companyId
                CategoryEnum.fromId(categoryEnum.getId()),// CategoryEnum category
                "coupon " + price,// String title
                "all coupons are the same",// String description
                RandomGenerators.dateGenerator("2024-08-06","2024-12-31"),// Date startDate
                RandomGenerators.dateGenerator("2025-08-06","2026-12-31"),// Date endDate
                rnd.nextInt(1,100),// int amount
                price,// Double price
                null// String image
        );
    }

    private static void purchasingCoupons(LoginManager login,AdminFacade admin, int numOf) throws LoginException, CompanyException, CustomerException {
        Random rnd =new Random();
        ArrayList<Customer> customers = admin.getAllCustomers();
        ArrayList<Company> cmps4cups = admin.getAllCompanies();
        for (Customer customer : customers) {
            CustomerFacade clintF = (CustomerFacade) login.login(customer.getEmail(),customer.getPassword(), ClientTypeEnum.CUSTOMER);
            for (int i = 0; i <rnd.nextInt(numOf) ; i++) {
                int bound =rnd.nextInt((cmps4cups.size()));
                logger.info("purchasingCoupons cmps4cups {}", bound);
                ArrayList<Coupon> tempCompCups = cmps4cups.get(bound).getCoupons();
                bound =tempCompCups.size();
                logger.info("purchasingCoupons tempCompCups {}", bound);
                if(bound > 0) {
                    try {
                        Coupon cup = tempCompCups.get(--bound);
                        logger.info("adding  {}", cup.getId());
                        clintF.purchaseCoupons(cup);
                    } catch (CustomerException e) {
                        logger.info("purchaseCoupons{}", e.getMessage());
                    }
                }
            }
        }
    }
    /**/
}
