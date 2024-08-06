package FACADE;

import BEANS.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CompanyFacadeTest {

    //   void login()  - tested on LoginManagerTest


    @Test
    void addCoupon() {
        System.out.print("add new coupon ");
        Coupon newCoupon = new Coupon(19, CategoryEnum.FASHION,"newCoupon","newCouponDesc", Date.valueOf("01/01/2024"),Date.valueOf("01/01/2025"),666,666.0,null);
        try {
            new CompanyFacade().addCoupon(newCoupon);
            System.out.println(" - success");
        } catch (SQLException|CouponException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void updateCoupon() {
        System.out.print("update selected coupon ");
        try {
            CompanyFacade comp = new CompanyFacade(19);
            ArrayList<Coupon> coupons = comp.getCompanyCoupons();
            coupons.get(0).setAmount(999);
            coupons.get(0).setCategory(CategoryEnum.TRAVEL);
            coupons.get(0).setEndDate(Date.valueOf("2024-08-01"));
            comp.updateCoupon(coupons.get(0));
            System.out.println(" - success updating coupon number " + coupons.get(0).getId());
        } catch (SQLException|CompanyException|CouponException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    void deleteCoupon() {
        System.out.print("delete non exist coupon ");
        try {
            CompanyFacade comp = new CompanyFacade();
            comp.deleteCoupon(666);
            System.out.println(" - success");
        } catch (SQLException | CouponException e) {
            System.out.println(" - success");
        }
        System.out.print("delete existing coupon ");
        try {
            CompanyFacade comp = new CompanyFacade();
            comp.deleteCoupon(32);
            System.out.println(" - success");
        } catch (SQLException | CouponException e) {
            System.out.println(" - failed");
        }
    }
    @Test
    void testGetCompanyCoupons() {
        System.out.print("getting all coupons of a company");
        try {
            if(new CompanyFacade(19).getCompanyCoupons() != null)
                System.out.println(" - success");
            else
                System.out.println(" - failed");
        } catch (SQLException | CompanyException e) {
            System.out.println(" - failed");
        }
    }
    @Test
    void testGetCompanyCoupons1() {
        System.out.print("getting all coupons of a company by category");
        try {
            if(new CompanyFacade(19).getCompanyCoupons(CategoryEnum.FASHION) != null)
                System.out.println(" - success");
            else
                System.out.println(" - failed");
        } catch (SQLException|CompanyException e) {
            System.out.println(" - failed");
        }
    }
    @Test
    void getCompanyDetails() {
        System.out.print("getting company details");
        try {
            if(new CompanyFacade(19).getCompanyDetails() != null)
                System.out.println(" - success");
            else
                System.out.println(" - failed");
        } catch (SQLException|CompanyException e) {
            System.out.println(" - failed");
        }
    }
}