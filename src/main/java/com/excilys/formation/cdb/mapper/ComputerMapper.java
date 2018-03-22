package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import com.excilys.formation.cdb.dto.ComputerDTO;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;

/**
 * @author jirr
 *
 */
public enum ComputerMapper {
    INSTANCE;
    /**
     * @param resultSet ResultSet of a request
     * @return Computer the BD object convert in java object
     * @throws SQLException if res is null
     */
    public Computer resToComputer(ResultSet resultSet) throws SQLException {
        int idComputer = resultSet.getInt("cuId");
        String nameComputer = resultSet.getString("cuName");
        Date intro = resultSet.getDate("introduced");
        Date disco = resultSet.getDate("discontinued");
        LocalDate introducedComputer = intro == null ? null : resultSet.getDate("introduced").toLocalDate();
        LocalDate discontinuedComputer = disco == null ? null : resultSet.getDate("discontinued").toLocalDate();
        Company manufactor = CompanyMapper.INSTANCE.resToCompany(resultSet);
        return new Computer(idComputer, nameComputer, introducedComputer, discontinuedComputer, manufactor);
    }
    
    /**
     * @param computerDTO DTO object to map 
     * @return Computer the BD object convert in java object
     * @throws Exception if the companyId is wrong
     */
    public Computer dtoToComputer(ComputerDTO computerDTO) throws Exception {
        String nameComputer = computerDTO.getName();
        LocalDate introduced = LocalDate.parse(computerDTO.getDateIntroduced());
        LocalDate discocontinued = LocalDate.parse(computerDTO.getDateDiscontinued());
        int manufactor = computerDTO.getManufactorId();
        return new Computer(nameComputer, introduced, discocontinued, CompanyService.INSTANCE.getCompany(manufactor));
    }
    

    /**
     * @param computer The object to map
     * @return ComputerDTO The object mapped to DTO version
     */
    public ComputerDTO computerToDTO(Computer computer) {
        return new ComputerDTO( computer.getId(),
                                computer.getName(),
                                optionalDateToString(computer.getDateIntroduced()),
                                optionalDateToString(computer.getDateDiscontinued()),
                                computer.getManufactor().getId(),
                                computer.getManufactor().getName());           
    }

    /**
     * @param Optional<LocalDate> The optional to check and convert to string
     * @return String The string version of LocalDate
     */
    public String optionalDateToString (Optional<LocalDate> date) {
        if (date.isPresent()) {
            return date.get().toString();
        } else {
            return "";
        }
    }
}
