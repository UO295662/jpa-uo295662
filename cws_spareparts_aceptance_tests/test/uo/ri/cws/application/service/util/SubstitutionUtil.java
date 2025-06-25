package uo.ri.cws.application.service.util;

import java.util.UUID;

import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.util.sql.AddSubstitutionSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindSparePartByCodeSqlUnitOfWork;

public class SubstitutionUtil {

	private SubstitutionDto dto = createDefaultDto();

	public SubstitutionUtil forSparePartCode(String code) {
		dto.sparePartCode = code;
		dto.sparePartId = findSparePartIdByCode(code).id;
		return this;
	}

	public SubstitutionUtil forIntervention(String interventionId) {
		dto.interventionId = interventionId;
		return this;
	}

	public SubstitutionUtil withQuantity(int quantity) {
		this.dto.quantity = quantity;
		return this;
	}

	public SubstitutionUtil register() {
		new AddSubstitutionSqlUnitOfWork( dto ).execute();
		return this;
	}

	public SubstitutionDto get() {
		return dto;
	}

	public static class SubstitutionDto {
		public String id;
		public long version;

		public String sparePartCode;
		public String sparePartId;
		public String interventionId;
		public int quantity;
	}

	private SubstitutionDto createDefaultDto() {
		 SubstitutionDto res = new SubstitutionDto();
		 res.id = UUID.randomUUID().toString();
		 res.version = 1;
		 res.quantity = 0;
		return res;
	}

	private SparePartDto findSparePartIdByCode(String code) {
		return new FindSparePartByCodeSqlUnitOfWork( code ).execute().get();
	}

}
