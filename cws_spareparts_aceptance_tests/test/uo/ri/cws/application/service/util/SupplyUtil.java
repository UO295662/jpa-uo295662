package uo.ri.cws.application.service.util;

import java.util.UUID;

import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto.SuppliedSparePartDto;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto.SupplierProviderDto;
import uo.ri.cws.application.service.util.sql.AddSupplySqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindProviderByNifSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindSparePartByCodeSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindSuppyByNifAndCodeSqlUnitOfWork;

public class SupplyUtil {

	private SupplyDto dto = createDefaultSupplyDto();

	public SupplyUtil forProvider(SupplierProviderDto provider) {
		dto.provider = provider;
		return this;
	}

	public SupplyUtil forSparePart(SuppliedSparePartDto spare) {
		dto.sparePart = spare;
		return this;
	}

	public SupplyUtil forProviderId(String id) {
		dto.provider.id = id;
		return this;
	}

	public SupplyUtil forSparePartId(String id) {
		dto.sparePart.id = id;
		return this;
	}

	public SupplyUtil forProviderNif(String providerNif) {
		dto.provider.nif = providerNif;
		dto.provider.id = findProviderByNif(providerNif).id;
		return this;
	}

	public SupplyUtil forSparePartCode(String sparePartCode) {
		dto.sparePart.code = sparePartCode;
		dto.sparePart.id = findSparePartByCode(sparePartCode).id;
		return this;
	}

	public SupplyUtil withPrice(double price) {
		dto.price = price;
		return this;
	}

	public SupplyUtil withDeliveryTerm(int days) {
		dto.deliveryTerm = days;
		return this;
	}

	public SupplyUtil loadByNifAndCode(String nif, String code) {
		dto = new FindSuppyByNifAndCodeSqlUnitOfWork(nif, code).execute().get();
		return this;
	}

	public SupplyUtil register() {
		new AddSupplySqlUnitOfWork(dto).execute();
		return this;
	}

	public SupplyDto get() {
		return dto;
	}

	private SupplyDto createDefaultSupplyDto() {
		SupplyDto dto = new SupplyDto();
		dto.id = UUID.randomUUID().toString();
		dto.sparePart = new SuppliedSparePartDto();
		dto.provider = new SupplierProviderDto();
		dto.price = 0;
		dto.deliveryTerm = 0;
		return dto;
	}

	private ProviderDto findProviderByNif(String providerNif) {
		return new FindProviderByNifSqlUnitOfWork(providerNif).execute().get();
	}

	private SparePartDto findSparePartByCode(String sparePartCode) {
		return new FindSparePartByCodeSqlUnitOfWork(sparePartCode).execute().get();
	}

}
