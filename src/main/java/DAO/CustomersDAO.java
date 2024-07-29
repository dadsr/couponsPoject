package DAO;

import BEANS.Customer;
import BEANS.CustomerException;

import java.util.ArrayList;

public interface CustomersDAO {
        public int isCustomerExists (String email,String password) throws CustomerException;
        public void addCustomer (Customer customer) throws CustomerException;
        public void updateCustomer (Customer customer) throws CustomerException;
        public void deleteCustomer (int customerID) throws CustomerException;
        public ArrayList<Customer> getAllCustomers() throws CustomerException;
        public Customer getSelectedCustomer(int customerID) throws CustomerException;
}
