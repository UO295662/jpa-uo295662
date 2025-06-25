package uo.ri.cws.application.service.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.util.sql.AddVehicleTypeSqlUnitOfWork;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService.VehicleTypeDto;

public class VehicleTypeUtil {

	private VehicleTypeDto dto = createDefaultDto();

	public VehicleTypeUtil randomName() {
		dto.name = RandomStringUtils.randomAlphabetic(5) + " type";
		return this;
	}

	public VehicleTypeUtil register() {
		new AddVehicleTypeSqlUnitOfWork(dto).execute();
		return this;
	}

	public VehicleTypeDto get() {
		return dto;
	}

	private VehicleTypeDto createDefaultDto() {
		VehicleTypeDto res = new VehicleTypeDto();

		res.id = UUID.randomUUID().toString();
		res.version = 1;

		res.name = RandomStringUtils.randomAlphabetic(5) + " type";
		res.pricePerHour = 1.0;

		return res;
	}

}
