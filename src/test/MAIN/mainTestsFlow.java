package MAIN;

import BEANS.*;
import FACADE.AdminFacade;
import FACADE.ClientFacade;
import FACADE.CompanyFacade;
import PROGRAM.ClientTypeEnum;
import PROGRAM.LoginManager;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class mainTestsFlow {
    public static void main(String[] args) {
        Random rnd =new Random();
        LoginManager loginn;
        AdminFacade admin;
        CompanyFacade comp;
        ClientFacade client;
        try {
            //todo - add 20 companies
            //admin@admin.com & admin
            admin = (AdminFacade) loginn.login("admin@admin.com","admin", ClientTypeEnum.ADMINISTRATOR);

            System.out.print("-------------------------- Adding new companies -------------------------------------");
            Company[] companies =new Company[20];
            for (int i = 0; i <= companies.length ; i++) {
                //String name, String email, String password, ArrayList<Coupon> coupons
                companies[i] = new Company("comp" +i,
                        "user" + i + "@comp" + i + ".co.il",
                        "" + rnd.nextInt(1000,99999999),
                        null);
            }

            for (Company company : companies) {
                try{
                    admin.addCompany(company);
                    System.out.println("Adding new companies - SUCCESS");
                } catch (CompanyException e) {
                    System.out.println("Adding new companies - FAILED");
                }
            }

            //todo - add  coupons
            System.out.print("-------------------------- Adding new coupons for company-------------------------------------");
            ArrayList<Company> cmps4cups =admin.getAllCompanies();

            for (Company cmp4cups : cmps4cups) {
                comp = (CompanyFacade) loginn.login(cmp4cups.getEmail(),cmp4cups.getPassword() ,ClientTypeEnum.COMPANY);

                Coupon[] coupons =new Coupon[rnd.nextInt(1,50)];
                for (int i = 0; i <= coupons.length ; i++) {
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
                            "C:\Users\speedy\Documents\coupons"// String image
                    );
                }
                for (Coupon coupon : coupons) {
                    try{
                        comp.addCoupon(coupon);
                        System.out.println("Adding new companies - SUCCESS");
                    } catch (CouponException e) {
                        System.out.println("Adding new companies - FAILED");
                    }
                }
            }


            //todo - add 30 clients
            System.out.print("-------------------------- Adding new clients -------------------------------------");
            fori
            admin.addCustomer();


            //todo - clients by  coupons



        } catch (SQLException | LoginException e) {
            throw new RuntimeException(e);
        } catch (CouponException e) {
            throw new RuntimeException(e);
        } catch (CompanyException e) {
            throw new RuntimeException(e);
        }

        client = (ClientFacade) loginn.login("admin@admin.com","admin", ClientTypeEnum.CUSTOMER);
    }
}

