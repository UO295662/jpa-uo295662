package uo.ri.cws.application.service.mechanic.crud.command;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;

public class AddMechanic {

	private MechanicDto dto;

	public AddMechanic(MechanicDto dto) {
		this.dto = dto;
	}

	public MechanicDto execute() throws BusinessException {

		return dto;
	}

}
