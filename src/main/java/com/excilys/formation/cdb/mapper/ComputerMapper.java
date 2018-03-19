package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.model.Company;

/**
 * @author jirr
 *
 */
public enum ComputerMapper {
	INSTANCE;
	/**
	 * @param res ResultSet of a request
	 * @return	
	 * @throws SQLException
	 */
	public Computer resToComputer(ResultSet res) throws SQLException {
	    int idComputer = res.getInt("cuId");
	    String nameComputer = res.getString("cuName");
	    Date intro = res.getDate("introduced");
	    Date disco = res.getDate("discontinued");
	    LocalDate introducedComputer = intro == null ? null : res.getDate("introduced").toLocalDate();
	    LocalDate discontinuedComputer = disco == null ? null : res.getDate("discontinued").toLocalDate();
	    Company manufactor = CompanyMapper.INSTANCE.resToCompany(res);
	    return new Computer(idComputer, nameComputer, introducedComputer, discontinuedComputer, manufactor);
	}
}
