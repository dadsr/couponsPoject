package DAO.Tests;

import BEANS.CategoryEnum;
import BEANS.Coupon;
import BEANS.CouponException;
import BEANS.CustomerException;
import DAO.CouponsDBDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class CouponDbTest {
    public static void main(String[] args) throws CustomerException, SQLException, CouponException {


        Random rnd =new Random();
        CouponsDBDAO couponsDbDao = new CouponsDBDAO();


        System.out.println("////////////////////////////////coupons////////////////////////////////////////////");
        Coupon[] coupons =new Coupon[5];
        for (int i = 0; i <5 ; i++) {
            //(int companyId, BEANS.CategoryEnum category, String title, String description, Date startDate, Date endDate, int amount, Double price, String image)
            coupons[i] = new Coupon(rnd.nextInt(17,26), CategoryEnum.FASHION,"title"+i,"desc"+i, new Date(2002,8,1+i), new Date(2024,8,i+1),666,100.0,"");
        }

        System.out.println("**addCoupon**");
        for (Coupon coupon : coupons) {
            couponsDbDao.addCoupon(coupon);
        }

        System.out.println("**getSelectedCoupon**");

        Coupon tempCoupon =couponsDbDao.getSelectedCoupon(4);
        System.out.println("**deleteCoupon**");
        couponsDbDao.deleteCoupon(4);
        System.out.println("**updateCoupon**");
        couponsDbDao.updateCoupon(tempCoupon);
        System.out.println("**getSelectedCoupon**");
        couponsDbDao.getSelectedCoupon(3);
        System.out.println("**getAllCoupon**");
        ArrayList<Coupon> allCoupons = couponsDbDao.getAllCoupons();
        for (Coupon allCoupon : allCoupons) {
            System.out.println(allCoupon.toString());
        }

    }

}
