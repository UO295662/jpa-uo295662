package uo.ri.cws.application.service.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.sql.AddMechanicSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindMechanicByIdSqlUnitOfWork;

public class MechanicUtil {

	private MechanicDto dto = createDefaultMechanic();

	public MechanicUtil withId(String id) {
		dto.id = id;
		return this;
	}

	public MechanicUtil withNif(String nif) {
		dto.nif = nif;
		return this;
	}

	public MechanicUtil withName(String name) {
		dto.name = name;
		return this;
	}

	public MechanicUtil withSurname(String surname) {
		dto.surname = surname;
		return this;
	}
 
	public MechanicUtil register() {
		new AddMechanicSqlUnitOfWork(dto).execute();
		return this;
	}

	public MechanicUtil loadById(String id) {
		this.dto = new FindMechanicByIdSqlUnitOfWork(id).execute().get();
		return this;
	}

	public MechanicDto get() {
		return dto;
	}

	private MechanicDto createDefaultMechanic() {
		MechanicDto res = new MechanicDto();
		res.id = UUID.randomUUID().toString();
		res.version = 1;
		res.nif = RandomStringUtils.randomAlphanumeric(9);
		res.name = RandomStringUtils.randomAlphabetic(4) + " name";
		res.surname = RandomStringUtils.randomAlphabetic(6) + " surname";
		return res;
	}

}
