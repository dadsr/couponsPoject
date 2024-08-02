package DAO;

import BEANS.Company;
import BEANS.CompanyException;

import java.util.ArrayList;

public interface CompaniesDAO {
    public int isCompanyExists (String email, String password) throws CompanyException;
    public void addCompany (Company company) throws CompanyException;
    public void updateCompany (Company company) throws CompanyException;
    public void deleteCompany (int companyID) throws CompanyException;
    public ArrayList<Company> getAllCompanies() throws CompanyException;
    public Company getSelectedCompany(int companyID) throws CompanyException;
}
