package FACADE;
package DAO;


import BEANS.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @Mock
    private CompaniesDbDao companiesDbDao;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCompany_successful() throws Exception {
        Company company = new Company("company@example.com", "Company Name");

        when(companiesDbDao.isCompanyExists(company.getEmail(), company.getName())).thenReturn(false);

        companyService.addCompany(company);

        verify(companiesDbDao, times(1)).addCompany(company);
    }

    @Test
    void testAddCompany_alreadyExists() throws Exception {
        Company company = new Company("company@example.com", "Company Name");

        when(companiesDbDao.isCompanyExists(company.getEmail(), company.getName())).thenReturn(true);

        assertThrows(CompanyException.class, () -> companyService.addCompany(company));

        verify(companiesDbDao, never()).addCompany(company);
    }
}
