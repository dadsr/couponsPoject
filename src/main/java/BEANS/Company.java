package BEANS;

import java.util.ArrayList;
import java.util.Comparator;

public class Company implements Comparator<Company> {
    private int id;
    private String name;
    private String email;
    private String password;
    ArrayList<Coupon> coupons;

    public Company(int id, String name, String email, String password, ArrayList<Coupon> coupons) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }
    public Company(String name, String email, String password, ArrayList<Coupon> coupons) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }
    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }
    @Override
    public String toString() {
        return "BEANS.Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" + coupons +
                '}';
    }

    @Override
    public int compare(Company o1, Company o2) {
        return (o1.getId() == o2.getId() &&
                o1.getEmail().equals(o2.getEmail()) &&
                o1.getPassword().equals(o2.getPassword()))?1:0;
    }
}
