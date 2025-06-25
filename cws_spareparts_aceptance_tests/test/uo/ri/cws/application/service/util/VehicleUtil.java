package uo.ri.cws.application.service.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.util.sql.AddVehicleSqlUnitOfWork;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;

public class VehicleUtil {

	private VehicleDto dto = createDefaultVehicle();

	public VehicleUtil forClient(String cId) {
		this.dto.clientId = cId;
		return this;
	}

	public VehicleUtil forVehicleType(String vtId) {
		this.dto.vehicleTypeId = vtId;
		return this;
	}

	public VehicleUtil randomPlate() {
		this.dto.plate = RandomStringUtils.randomAlphanumeric(7);
		return this;
	}

	public VehicleUtil register() {
		new AddVehicleSqlUnitOfWork( dto ).execute();
		return this;
	}

	private VehicleDto createDefaultVehicle() {
		VehicleDto res = new VehicleDto();

		res.id = UUID.randomUUID().toString();
		res.version = 1;

		res.plate = RandomStringUtils.randomAlphanumeric(7);
		res.make = RandomStringUtils.randomAlphanumeric(5) + " cars";
		res.model = RandomStringUtils.randomAlphanumeric(5) + " model";

		return res;
	}

	public VehicleDto get() {
		return dto;
	}

}
