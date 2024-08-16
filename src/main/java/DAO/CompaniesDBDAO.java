package DAO;

import BEANS.*;
import BEANS.CouponException;
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
        logger.info("CompaniesDBDAO constructor");

    }
    /**
     * Checks whether a company with the specified ID exists in the database.
     * <p>
     * This method queries the `companies` table to determine if a company with the given ID exists.
     * It uses a `PreparedStatement` to execute the SQL query and returns the company ID if found.
     * If no company with the specified ID is found, it returns `0`.
     * used in updateCompany
     * <p>
     * The method logs the process of checking the existence of the company and any exceptions that
     * occur. It also ensures that the database connection is properly closed and returned to the pool
     * even if an exception is thrown.
     *
     * @param id the ID of the company to check for existence.
     * @return the ID of the company if it exists, or `0` if no such company is found.
     * @throws CompanyException if an error occurs during the database query process.
     */
    public int isCompanyExists(int id) throws CompanyException {
        logger.info("isCompanyExists - Checks whether the requested company exists by company id:{}", id);
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
            logger.error("isCompanyExists threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Checks whether a company with the specified email and password exists in the database.
     * <p>
     * This method queries the `companies` table to determine if a company with the given email
     * and password exists. It uses a `PreparedStatement` to execute the SQL query and returns
     * the company ID if a match is found. If no such company exists, it returns `0`.
     * <p>
     * The method logs the process of checking the company's existence and any exceptions that occur.
     * It also ensures that the database connection is properly closed and returned to the pool,
     * even if an exception is thrown.
     *
     * @param email the email address of the company to check.
     * @param password the password of the company to check.
     * @return the ID of the company if it exists and the credentials match, or `0` if no such company is found.
     * @throws CompanyException if an error occurs during the database query process.
     */
    @Override
    public int isCompanyExists(String email, String password) throws CompanyException {
        logger.info("isCompanyExists - Checks whether the requested company exists by email:{} and password:{}", email,password);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM companies WHERE email =? and password =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1,email);
            statement.setString(2,password);
            ResultSet resultSet =statement.executeQuery();
            if (resultSet.next()) {
                logger.info("isCompanyExists - returns company id:{}", resultSet.getInt(1));
                return resultSet.getInt(1);//company id
            }else
                return 0;//false
        } catch (InterruptedException | SQLException e) {
            logger.error("isCompanyExists threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Adds a new company to the database.
     * <p>
     * This method inserts a new record into the `companies` table with the company's email, password,
     * and name. It uses a `PreparedStatement` to execute the SQL query and handles any SQL or connection
     * errors that occur during the process.
     * <p>
     * The method logs the process of attempting to add the company and any exceptions that occur. If the
     * execution of the statement returns false, indicating that the company was not added successfully,
     * it throws a `CompanyException`.
     * <p>
     * The method also ensures that the database connection is properly closed and returned to the pool,
     * even if an exception is thrown.
     *
     * @param company the company to be added to the database.
     * @throws CompanyException if an error occurs during the database insertion process or if the insertion fails.
     */
    @Override
    public void addCompany(Company company) throws CompanyException {
        logger.info("addCompany - trying to add new company email:{} and password:{}", company.getEmail(),company.getPassword());
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="INSERT INTO companies (`email`,`password`,`name`,`insert_date`) VALUES (?,?,?,current_time());";
            PreparedStatement statement =connection.prepareStatement(query);
            companyToStatement(company,statement);
            if(statement.execute()) {
                logger.error("addCompany threw exception - adding company failed company email:{} and password:{}", company.getEmail(),company.getPassword());
                throw new CompanyException("addCompany failed ,execute() returned false");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("addCompany threw exception - {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Updates the details of an existing company in the database.
     * <p>
     * This method updates the email and password of a company in the `companies` table based on the
     * provided company ID. It uses a `PreparedStatement` to execute the SQL update query and handles
     * any SQL or connection errors that occur during the process.
     * <p>
     * The method logs the process of attempting to update the company and any exceptions that occur. If
     * the execution of the statement returns false, indicating that the update was not successful, it throws
     * a `CompanyException`.
     * <p>
     * The method also ensures that the database connection is properly closed and returned to the pool,
     * even if an exception is thrown.
     *
     * @param company the company object containing the updated details and ID of the company to be updated.
     * @throws CompanyException if an error occurs during the database update process or if the update fails.
     */
    @Override
    public void updateCompany(Company company)  throws CompanyException {
        logger.info("updateCompany - trying to update company {}",company.getId());
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="UPDATE companies SET email = ?,password = ? WHERE id = ?;";
            PreparedStatement statement =connection.prepareStatement(query);
            companyToStatement(company,statement);
            if(statement.execute()) {
                logger.error("updateCompany -  threw exception updating companyid:{} failed" + company.getId());
                throw new CompanyException("updateCompany failed ,execute() returned false");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("updateCompany threw exception {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Deletes a company from the database based on its ID.
     * <p>
     * This method removes a company record from the `companies` table using the provided company ID.
     * It uses a `PreparedStatement` to execute the SQL delete query and handles any SQL or connection
     * errors that occur during the process.
     * <p>
     * The method logs the process of attempting to delete the company and any exceptions that occur. If
     * the execution of the statement returns false, indicating that the deletion was not successful, it throws
     * a `CompanyException`.
     * <p>
     * The method also ensures that the database connection is properly closed and returned to the pool,
     * even if an exception is thrown.
     *
     * @param companyID the ID of the company to be deleted from the database.
     * @throws CompanyException if an error occurs during the database deletion process or if the deletion fails.
     */
    @Override
    public void deleteCompany(int companyID)  throws CompanyException {
        logger.info("deleteCompany - trying to delete company by company id:{}", companyID);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="DELETE FROM companies WHERE id =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, companyID);
            if(statement.execute()) {
                logger.error("deleteCompany - deleting company failed company id:{}", companyID);
                throw new CompanyException("deleteCompany failed ,execute() returned false");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCompany threw exception {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /**
     * Retrieves a list of all companies from the database.
     * <p>
     * This method queries the `companies` table to fetch all records. It processes the results using
     * a `ResultSet` and converts each record into a `Company` object. All `Company` objects are collected
     * into an `ArrayList`, which is then returned to the caller.
     * <p>
     * The method logs the retrieval attempt and any exceptions that occur. If a database error or connection
     * issue arises, a `CompanyException` is thrown. It ensures that the database connection is properly closed
     * and returned to the connection pool even if an exception is thrown.
     *
     * @return an `ArrayList` of `Company` objects representing all companies in the database.
     * @throws CompanyException if an error occurs during the database operation or while processing results.
     */
    @Override
    public ArrayList<Company> getAllCompanies() throws CompanyException {
        logger.info("getAllCompanies - getting all companies");
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM companies;";
            PreparedStatement statement =connection.prepareStatement(query);
            ResultSet resultSet =statement.executeQuery();
            ArrayList<Company> companies =new ArrayList<>();
            while (resultSet.next()){
                companies.add(resultSetToCompany(resultSet));
            }
            return companies;
        } catch (InterruptedException | SQLException  e) {
            logger.error("getAllCompanies threw exception {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /**
     * Retrieves a company from the database based on the provided company ID.
     * <p>
     * This method queries the `companies` table for a company with the specified ID. If a matching record is found,
     * it is converted into a `Company` object and returned. If no record is found, an error is logged, and a
     * `CompanyException` is thrown.
     * <p>
     * The method logs the retrieval attempt and any exceptions that occur. If a database error, connection issue,
     * or any other exception arises, a `CompanyException` is thrown. It ensures that the database connection is
     * properly closed and returned to the connection pool even if an exception is thrown.
     *
     * @param companyID the ID of the company to retrieve.
     * @return a `Company` object representing the company with the specified ID.
     * @throws CompanyException if an error occurs during the database operation, if the company does not exist,
     *                           or if there is an issue processing the results.
     */
    @Override
    public Company getSelectedCompany(int companyID) throws CompanyException {
        logger.info("getSelectedCompany - trying to get company by company id: {}", companyID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            String query = "SELECT * FROM companies WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, companyID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSetToCompany(resultSet);
            } else {
                logger.error("getSelectedCompany - getting company failed -company id:{}", companyID);
                throw new CompanyException("getSelectedCompany failed");
            }
        } catch (InterruptedException | SQLException | CompanyException e) {
            logger.error("getSelectedCompany threw exception {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Retrieves a company from the database based on the provided email and password.
     * <p>
     * This method queries the `companies` table for a company that matches the specified email and password. If a
     * matching record is found, it is converted into a `Company` object and returned. If no record is found, an error
     * is logged, and a `CompanyException` is thrown.
     * <p>
     * The method logs the attempt to retrieve the company and any exceptions that occur. If a database error, connection
     * issue, or any other exception arises, a `CompanyException` is thrown. It ensures that the database connection
     * is properly closed and returned to the connection pool even if an exception is thrown.
     *
     * @param email the email of the company to retrieve.
     * @param password the password of the company to retrieve.
     * @return a `Company` object representing the company with the specified email and password.
     * @throws CompanyException if an error occurs during the database operation, if the company does not exist, or
     *                           if there is an issue processing the results.
     */
    public Company getSelectedCompany(String email, String password) throws CompanyException {
        logger.info("getSelectedCompany- trying to get company by company email:{} and company password:{}", email,password);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="SELECT * FROM companies WHERE email = ? AND password = ? ;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSetToCompany(resultSet);
            }else {
                logger.error("getSelectedCompany - getting company failed - company email:{} and company password:{}", email,password);
                throw new CompanyException("getSelectedCompany failed");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("getSelectedCompany threw exception {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }
    /**
     * Deletes all coupons associated with a specific company from the database.
     * <p>
     * This method removes all coupon records from the `coupons` table where the `company_id` matches
     * the provided company ID. It uses a `PreparedStatement` to execute the SQL delete query and handles
     * any potential SQL or connection errors that may arise.
     * <p>
     * The method logs the process of attempting to delete the coupons and any exceptions that occur. If
     * the execution of the statement returns false, indicating that the deletion was not successful, it
     * throws a `CompanyException`.
     * <p>
     * The method ensures that the database connection is properly closed and returned to the connection pool,
     * even if an exception is thrown during the process.
     *
     * @param companyID the ID of the company whose associated coupons are to be deleted.
     * @throws CompanyException if an error occurs during the database deletion process or if the deletion fails.
     */
    public void deleteCouponsByCompany(int companyID)  throws CompanyException {
        logger.info("deleteCouponsByCompany - trying to delete coupons by company id:{}", companyID);
        Connection connection =null;
        try {
            connection = connectionPool.getConnection();
            String query ="DELETE FROM coupons WHERE company_id =?;";
            PreparedStatement statement =connection.prepareStatement(query);
            statement.setInt(1, companyID);
            if(statement.execute()) {
                logger.error("deleteCouponsByCompany - deleting coupons failed - company id:{}", companyID);
                throw new CompanyException("deleteCouponsByCompany failed,execute() returned false");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("deleteCouponsByCompany threw exception {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        } finally {
            if (connection != null)
                connectionPool.restoreConnection(connection);
        }
    }

    /************************************ resultSetTo methods ***************************************************/

    /**
     * Converts a `Company` object into a `PreparedStatement` by setting its attributes.
     * <p>
     * This method populates the `PreparedStatement` with values from the provided `Company` object. It sets the
     * company email and password, and then either sets the company name (for insert operations) or the company ID
     * (for update operations) depending on whether the company ID is zero.
     * <p>
     * The method logs the action of converting the `Company` object into the `PreparedStatement` for debugging purposes.
     * If the `Company` object has an ID of zero, it is treated as a new company to be inserted; otherwise, it is treated
     * as an existing company to be updated.
     *
     * @param company the `Company` object containing the details to be set in the `PreparedStatement`.
     * @param statement the `PreparedStatement` to be populated with the `Company` object's values.
     * @throws SQLException if an error occurs while setting the parameters in the `PreparedStatement`.
     */
    public void companyToStatement(Company company, PreparedStatement statement) throws SQLException {
        logger.info("companyToStatement - converting Company object into statement - company name:{}",company.getName());
        statement.setString(1, company.getEmail());
        statement.setString(2, company.getPassword());
        if(company.getId()==0)
            statement.setString(3, company.getName());//insert
        else
            statement.setInt(3, company.getId());//update
    }
    /**
     * Converts a `ResultSet` object into a `Company` object.
     * <p>
     * This method extracts data from the provided `ResultSet` and uses it to create a `Company` object.
     * The method retrieves the company ID, name, email, and password from the `ResultSet`, and then uses the company
     * ID to fetch associated coupons from the `CouponsDBDAO`. The fetched coupons are added to the `Company` object.
     * <p>
     * The method logs the process of converting the `ResultSet` into a `Company` object for debugging purposes.
     * If any errors occur while accessing the `ResultSet` or fetching coupons, the method logs the exception and
     * throws a `CompanyException`.
     *
     * @param resultSet the `ResultSet` containing the company data to be converted into a `Company` object.
     * @return a `Company` object populated with data from the `ResultSet`.
     * @throws CompanyException if an error occurs while retrieving data from the `ResultSet` or fetching associated coupons.
     */
    public Company resultSetToCompany(ResultSet resultSet) throws CompanyException {
        int companyId = 0;
        String name,email,password;
        ArrayList<Coupon> coupons;
        try{
            companyId = resultSet.getInt(1);
            logger.info("resultSetToCompany - converting resultSet object into company - company id:{}",companyId);
            name = resultSet.getString(2);
            email = resultSet.getString(3);
            password = resultSet.getString(4);
            return new Company(companyId, name, email, password, new CouponsDBDAO().allCouponsByCompany(companyId));
        } catch (CouponException | SQLException e) {
            logger.error("resultSetToCompany threw exception {}", e.getMessage());
            throw new CompanyException(e.getMessage());
        }
    }
//
}





