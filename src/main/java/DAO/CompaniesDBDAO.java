package DAO;

import BEANS.Company;
import BEANS.CompanyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompaniesDBDAO implements CompaniesDAO {
    private ConnectionPool connectionPool;
    protected static final Logger logger = LogManager.getLogger(CompaniesDBDAO.class.getName());

    public CompaniesDBDAO() throws SQLException {
        connectionPool = ConnectionPool.getInstance();
        logger.info("CompaniesDBDAO");

    }
    //used in updateCompany
    public int isCompanyExists(int id) throws CompanyException {
        logger.info("isCompanyExists2");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM companies WHERE id = ? ;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet resultSet =statement.executeQuery();
            if (resultSet.next())
                return resultSet.getInt(1);//company id
            return 0;//false

        } catch (InterruptedException | SQLException e) {
            logger.error("isCompanyExists2 {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    @Override
    public int isCompanyExists(String email, String password) throws CompanyException {
        logger.info("isCompanyExists1");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM companies WHERE email =? and password =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1,email);
            statement.setString(2,password);
            ResultSet resultSet =statement.executeQuery();
            if (resultSet.next())
                return resultSet.getInt(1);//company id
            return 0;//false

        } catch (InterruptedException | SQLException e) {
            logger.error("isCompanyExists1 {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void addCompany(Company company) throws CompanyException {
        logger.info("addCompany");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="INSERT INTO companies (`name`,`email`,`password`,`insert_date`) VALUES (?,?,?,current_time());";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1, company.getName());
            statement.setString(2, company.getEmail());
            statement.setString(3,company.getPassword());
            if(statement.execute()) {
                logger.error("addCompany - adding company failed");
                throw new CompanyException("addCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("addCompany {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            //true if the first result is a ResultSet object; false if the first result is an update count or there is no result
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void updateCompany(Company company)  throws CompanyException {
        logger.info("updateCompany");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="UPDATE companies SET email = ?,password = ? WHERE id = ?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1, company.getEmail());
            statement.setString(2,company.getPassword());
            statement.setInt(3,company.getId());

            if(statement.execute()) {
                logger.error("updateCompany - updating company failed");
                throw new CompanyException("updateCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("updateCompany {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public void deleteCompany(int companyID)  throws CompanyException {
        logger.info("deleteCompany");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="DELETE FROM companies WHERE id =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, companyID);
            if(statement.execute()) {
                logger.error("deleteCompany - deleting company failed");
                throw new CompanyException("deleteCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCompany {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    public void deleteCouponsByCompany(int companyID)  throws CompanyException {
        logger.info("deleteCouponsByCompany");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="DELETE FROM companies WHERE id =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, companyID);
            if(statement.execute()) {
                logger.error("deleteCouponsByCompany - deleting company failed");
                throw new CompanyException("deleteCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCouponsByCompany {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public ArrayList<Company> getAllCompanies() throws CompanyException {
        logger.info("getAllCompanies");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM companies;";
            PreparedStatement statement =connection.prepareStatement(query);
            ResultSet resultSet =statement.executeQuery();
            ArrayList<Company> companies =new ArrayList<>();
            while (resultSet.next()){
                companies.add(new Company(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        null));//getAllCoupons()));
            }
            return companies;
        } catch (InterruptedException | SQLException e) {
            logger.error("getAllCompanies {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public Company getSelectedCompany(int companyID) throws CompanyException {
        logger.info("getSelectedCompany1");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM companies WHERE id = ?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, companyID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new Company(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        null/*getAllCoupons()*/);
            }else {
                logger.error("getSelectedCompany1 - getting company failed");
                throw new CompanyException("getSelectedCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("getSelectedCompany1 {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    public Company getSelectedCompany(String email, String password) throws CompanyException {
        logger.info("getSelectedCompany2");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM companies WHERE email = ? AND password = ? ;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new Company(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        null/*getAllCoupons()*/);
            }else {
                logger.error("getSelectedCompany2 - getting company failed");
                throw new CompanyException("getSelectedCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("getSelectedCompany2 {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

}
