package uo.ri.cws.application.service.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.util.sql.AddWorkOrderSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindWorkOrderByIdSqlUnitOfWork;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;

public class WorkOrderUtil {

	private WorkOrderDto dto = createDefaultWorkOrderDto();

	public WorkOrderUtil forVehicle(String id) {
		dto.vehicleId = id;
		return this;
	}

	public WorkOrderUtil forMechanic(String id) {
		dto.mechanicId = id;
		dto.state = "ASSIGNED";
		return this;
	}

	public WorkOrderUtil withState(String state) {
		dto.state = state;
		return this;
	}

	public WorkOrderUtil withAmount(double amount) {
		dto.amount = amount;
		return this;
	}

	public WorkOrderUtil register() {
		new AddWorkOrderSqlUnitOfWork(dto).execute();
		return this;
	}

	public WorkOrderDto get() {
		return dto;
	}

	private WorkOrderDto createDefaultWorkOrderDto() {
		WorkOrderDto res = new WorkOrderDto();

		res.id = UUID.randomUUID().toString();
		res.version = 1;
		res.date = LocalDateTime.now();
		res.description = RandomStringUtils.randomAlphabetic(10);
		res.state = "OPEN";
		res.amount = 0;

		return res;
	}
	
	public static List<WorkOrderDto> loadByIds(List<String> workordersIds) {
		List<WorkOrderDto> res = new ArrayList<>();
		for (String id : workordersIds) {
			WorkOrderDto dto = new FindWorkOrderByIdSqlUnitOfWork(id).execute().get();
			res.add( dto );
		}
		return res;
	}

}
