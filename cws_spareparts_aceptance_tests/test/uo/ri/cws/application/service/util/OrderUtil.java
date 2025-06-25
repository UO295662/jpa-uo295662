package uo.ri.cws.application.service.util;

import java.time.LocalDate;
import java.util.UUID;

import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto.OrderLineDto;
import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.util.sql.AddOrderSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindOrderByCodeSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindProviderByNifSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindSparePartByCodeSqlUnitOfWork;
import uo.ri.util.random.Random;

public class OrderUtil {

	private OrderDto dto = createDefaultDto();

	public OrderUtil forProviderId(String id) {
		dto.provider.id = id;
		return this;
	}

	public OrderUtil forProviderNif(String nif) {
		dto.provider.nif = nif;
		dto.provider.id = findProviderByNif( nif ).id;
		return this;
	}

	public OrderUtil loadByCode(String code) {
		dto = new FindOrderByCodeSqlUnitOfWork(code).execute().get();
		return this;
	}

	public OrderUtil withCode(String code) {
		dto.code = code;
		return this;
	}

	public OrderUtil withRandomCode() {
		dto.code = Random.string(5);
		return this;
	}

	public OrderUtil withAmount(double amount) {
		dto.amount = amount;
		return this;
	}

	public OrderUtil addOrderLine(String sparePartCode, int quantity, double price) {
		OrderLineDto line = new OrderLineDto();
		line.quantity = quantity;
		line.price = price;
		line.sparePart.code = sparePartCode;
		line.sparePart.id = findSparePartByCode( sparePartCode ).id;
		dto.lines.add( line );
		return this;
	}

	public OrderDto get() {
		return dto;
	}

	private OrderDto createDefaultDto() {
		OrderDto dto = new OrderDto();
		dto.id = UUID.randomUUID().toString();
		dto.version = 0;
		dto.code = "dummy-order-code";
		dto.orderedDate = LocalDate.now();
		dto.state = "PENDING";
		dto.amount = 0;
		return dto;
	}

	public OrderUtil received() {
		dto.state = "RECEIVED";
		return this;
	}

	public OrderUtil register() {
		new AddOrderSqlUnitOfWork(dto).execute();
		return this;
	}

	private ProviderDto findProviderByNif(String providerNif) {
		return new FindProviderByNifSqlUnitOfWork(providerNif).execute().get();
	}

	private SparePartDto findSparePartByCode(String sparePartCode) {
		return new FindSparePartByCodeSqlUnitOfWork(sparePartCode).execute().get();
	}

}
