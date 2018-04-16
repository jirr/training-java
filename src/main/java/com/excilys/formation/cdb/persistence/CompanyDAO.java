package com.excilys.formation.cdb.persistence;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;

/**
 * @author jirr
 *
 */
@Repository
@EnableTransactionManagement
public class CompanyDAO {

    private final String SELECT_ALL_COMPANIES = "SELECT ca.id as caId, ca.name as caName FROM company ca";
    private final String COUNT_ALL_COMPANIES = "SELECT COUNT(computer.id) FROM computer LEFT JOIN company ON company.id = computer.company_id";
    private final String SELECT_SOME_COMPANIES = "SELECT ca.id as caId, ca.name as caName FROM company ca LIMIT ? OFFSET ?;";
    private final String SELECT_ONE_COMPANY = "SELECT ca.id as caId, ca.name as caName FROM company ca WHERE ca.id = ?;";
    private final String DELETE_ONE_COMPANY = "DELETE FROM computer WHERE id=?;";
    private final String SELECT_COMPUTERS_WITH_COMPANY = "SELECT cu.id FROM computer as cu LEFT JOIN company as ca ON cu.id = ca.id WHERE cu.company_id = ?;";

    private ComputerDAO computerDAO;
    private JdbcTemplate jdbcTemplate;
    private CompanyMapper companyMapper;
    
    @Autowired
    public CompanyDAO(ComputerDAO computerDAO, DataSource dataSource, CompanyMapper companyMapper) {
        this.computerDAO = computerDAO;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.companyMapper = companyMapper;
    }
    
    /**
     * @param id of Company that should be in the DB
     * @return Optional<Company> contains the company, could be empty if the id does not exist
     * @throws DBException if can't reach the database
     */
    public Optional<Company> selectOne(int id) {
        Company company = (Company) this.jdbcTemplate.queryForList(SELECT_ONE_COMPANY, id, Company.class);
        return Optional.ofNullable(company);
    }
    
    /**
     * @return List<Company>
     * @throws DBException if can't reach the database
     */
    public List<Company> list() {
        return this.jdbcTemplate.query(SELECT_ALL_COMPANIES, (resultSet, row) -> companyMapper.resToCompany(resultSet));
    }
    
    /**
     * @param keywords The keywords of the search, can be empty
     * @return int number of computers
     * @throws DBException if can't reach the database
     */
    public int countAllCompany() {
        return this.jdbcTemplate.queryForObject(COUNT_ALL_COMPANIES, Integer.class);
    }

    /**
     * @param limit index du dernier element
     * @param offset index du premier element
     * @return List<Company> The sublist of Company object from the DB
     * @throws DBException if can't reach the database
     */
    public List<Company> subList(int offset, int limit) {
        return this.jdbcTemplate.queryForList(SELECT_SOME_COMPANIES, new Object[] {limit, offset}, Company.class);
    }
    
    /**
     * @param id the ID of computer to delete from the DB
     * @throws DBException if can't reach the database
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCompany(int id) {
        List<Integer> ids = this.jdbcTemplate.queryForList(SELECT_COMPUTERS_WITH_COMPANY, int.class);
        computerDAO.deleteComputer(ids.stream().mapToInt(i->i).toArray());
        this.jdbcTemplate.update(DELETE_ONE_COMPANY, new Object[] {id});
    }
}
