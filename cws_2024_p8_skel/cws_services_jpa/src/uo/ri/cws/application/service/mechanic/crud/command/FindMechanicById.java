package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;

public class FindMechanicById {

	private String id;

	public FindMechanicById(String id) {
		this.id = id;
	}

	public Optional<MechanicDto> execute() throws BusinessException {

		return Optional.empty();
	}

}
