package DAO;

import BEANS.Coupon;
import BEANS.CouponException;

import java.util.ArrayList;

public interface CouponsDAO {
    public void addCoupon (Coupon coupon) throws CouponException;
    public void updateCoupon (Coupon coupon) throws CouponException;
    public void deleteCoupon (int couponID) throws CouponException;
    public ArrayList<Coupon> getAllCoupons() throws CouponException;
    public Coupon getSelectedCoupon(int couponID) throws CouponException;
    public void addCouponPurchase (int customerId,int couponID) throws CouponException;
    public void deleteCouponPurchase (int customerId,int couponID) throws CouponException;
}
