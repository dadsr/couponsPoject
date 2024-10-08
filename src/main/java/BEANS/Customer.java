package BEANS;

import java.util.ArrayList;
import java.util.Comparator;

public class Customer implements Comparator<Customer> {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private ArrayList<Coupon> coupons;

    public Customer(int id, String firstName, String lastName, String email, String password, ArrayList<Coupon> coupons) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }
    public Customer(String firstName, String lastName, String email, String password, ArrayList<Coupon> coupons) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }
    public int getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return "BEANS.Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" + coupons +
                '}';
    }

    @Override
    public int compare(Customer o1, Customer o2) {
        return (o1.getId() == o2.getId() &&
                o1.getEmail().equals(o2.getEmail()) &&
                o1.getPassword().equals(o2.getPassword()))?1:0;
    }
}
