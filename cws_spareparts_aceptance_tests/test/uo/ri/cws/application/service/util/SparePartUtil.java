package uo.ri.cws.application.service.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.util.sql.AddSparePartSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindSparePartByCodeSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.UpdateSparePartSqlUnitOfWork;

public class SparePartUtil {

	private SparePartDto dto = createDefaultDto();

	public SparePartUtil randomCode() {
		this.dto.code = RandomStringUtils.randomAlphanumeric(8);
		return this;
	}

	public SparePartUtil withCode(String sparePartCode) {
		this.dto.code = sparePartCode;
		return this;
	}

	public SparePartUtil withDescription(String description) {
		this.dto.description = description;
		return this;
	}

	public SparePartUtil withStock(int stock) {
		this.dto.stock = stock;
		return this;
	}

	public SparePartUtil withMinStock(int minStock) {
		dto.minStock = minStock;
		return this;
	}

	public SparePartUtil withMaxStock(int maxStock) {
		dto.maxStock = maxStock;
		return this;
	}

	public SparePartUtil withPrice(double price) {
		dto.price = price;
		return this;
	}

	public SparePartUtil loadByCode(String sparePartCode) {
		dto = findByCode( sparePartCode );
		return this;
	}

	public SparePartUtil register() {
		new AddSparePartSqlUnitOfWork(dto).execute();
		return this;
	}

	public SparePartUtil registerIfNew() {
		SparePartDto s = findByCode( dto.code );
		if ( s == null ) {
			register();
		}
		return this;
	}

	private SparePartDto findByCode(String code) {
		return new FindSparePartByCodeSqlUnitOfWork( code ).execute().get();
	}

	public SparePartUtil update() {
		new UpdateSparePartSqlUnitOfWork(dto).execute();
		return this;
	}

	public SparePartDto get() {
		return dto;
	}

	private SparePartDto createDefaultDto() {
		SparePartDto dto = new SparePartDto();
		dto.id = UUID.randomUUID().toString();
		dto.version = 0;
		dto.code = RandomStringUtils.randomAlphabetic(5);
		dto.description = "dummy-description for " + dto.code;
		dto.maxStock = 0;
		dto.minStock = 0;
		dto.price = 0;
		dto.stock = 0;
		return dto;
	}

}
