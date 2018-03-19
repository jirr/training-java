package com.excilys.formation.cdb.service;

import java.time.LocalDate;
import com.excilys.formation.cdb.model.*;
import com.excilys.formation.cdb.persistence.*;

public enum Validator {
	INSTANCE;

	protected void datesValidation(LocalDate d1, LocalDate d2) throws Exception {
		if (!(d1 == null && d2 == null)) {
			if (d2.compareTo(d1) >0) {
				throw new Exception("Incompatibility of dates.");
			}
		}
	}

	protected void nameValidation(String name) throws Exception {
		if (name.compareTo("") == 0) {
			throw new Exception("Name is required.");
		}
	}

	protected Computer computerIdValidation(int id) throws Exception {
		if (ComputerDB.INSTANCE.selectOne(id).isPresent()) {
			return ComputerDB.INSTANCE.selectOne(id).get();
		} else {
			throw new Exception("Computer ID does not exist.");
		}
	}

	protected Company manufactorValidation(int id) throws Exception {
		if (CompanyDB.INSTANCE.selectOne(id).isPresent()) {
			return CompanyDB.INSTANCE.selectOne(id).get();
		} else {
			throw new Exception("Company ID does not exist.");
		}
	}
}