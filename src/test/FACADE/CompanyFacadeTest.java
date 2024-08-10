package FACADE;

import BEANS.*;
import BEANS.CouponException;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

class CompanyFacadeTest {
    int companyId = 2411;//addCoupon + updateCoupon +testGetCompanyCoupons+testGetCompanyCoupons1+getCompanyDetails
    int couponId = 457053;//deleteCoupon

    //   void login()  - tested on LoginManagerTest


    @Test
    void addCoupon() {
        System.out.print("add new coupon ");
        Coupon newCoupon = new Coupon(companyId, CategoryEnum.FASHION,"newCoupon","newCouponDesc", Date.valueOf("2024-01-01"),Date.valueOf("2025-01-01"),666,666.0,null);
        try {
            new CompanyFacade().addCoupon(newCoupon);
            System.out.println(" - success");
        } catch (SQLException | CouponException e) {
            System.out.println(" - failed");
        }
    }

    @Test
    void updateCoupon() {
        System.out.print("update selected coupon ");
        try {
            CompanyFacade comp = new CompanyFacade(companyId);
            ArrayList<Coupon> coupons = comp.getCompanyCoupons();
            coupons.get(0).setAmount(999);
            coupons.get(0).setCategory(CategoryEnum.TRAVEL);
            coupons.get(0).setEndDate(Date.valueOf("2024-07-01"));
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
            comp.deleteCoupon(couponId);
            System.out.println(" - success");
        } catch (SQLException | CouponException e) {
            System.out.println(" - failed");
        }
    }
    @Test
    void testGetCompanyCoupons() {
        System.out.print("getting all coupons of a company");
        try {
            if(new CompanyFacade(companyId).getCompanyCoupons() != null)
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
            if(new CompanyFacade(companyId).getCompanyCoupons(CategoryEnum.FASHION) != null)
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
            if(new CompanyFacade(companyId).getCompanyDetails() != null)
                System.out.println(" - success");
            else
                System.out.println(" - failed");
        } catch (SQLException|CompanyException e) {
            System.out.println(" - failed");
        }
    }
}