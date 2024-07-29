package DAO.Tests;

import BEANS.Company;
import BEANS.CompanyException;
import DAO.CompaniesDBDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


public class CompaniesDbTest {
    public static void main(String[] args) throws SQLException, CompanyException {
        Random rnd =new Random();
        CompaniesDBDAO companiesDbDao = new CompaniesDBDAO();

        System.out.println("////////////////////////////////companies////////////////////////////////////////////");
        Company[] companies =new Company[5];
        for (int i = 0; i <5 ; i++) {
            //(String name, String email, String password, ArrayList<BEANS.Coupon> coupons)
            companies[i] = new Company("comp"+i,
                    i+"@y.co.il",
                    "" + rnd.nextInt(1000,999999),
                    null);
        }

        for (Company comp : companies) {
            companiesDbDao.addCompany(comp);
        }
        System.out.println("**getSelectedCompany**");
        Company tempComp = companiesDbDao.getSelectedCompany(5);
        tempComp.setId(6);
        System.out.println("**deleteCompany**");
        companiesDbDao.deleteCompany(6);
        System.out.println("**updateCompany**");
        companiesDbDao.updateCompany(tempComp);
        System.out.println("**getSelectedCompany**");
        Company tempComp2 = companiesDbDao.getSelectedCompany(4);
        System.out.println(tempComp.toString());
        System.out.println(tempComp2.toString());
        System.out.println("**getAllCompanies**");
        ArrayList<Company> tempCompanies = companiesDbDao.getAllCompanies();
        for (Company tempCompany : tempCompanies) {
            System.out.println(tempCompany.toString());

        }


    }
}