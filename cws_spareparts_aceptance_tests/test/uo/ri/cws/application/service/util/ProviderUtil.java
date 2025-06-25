package uo.ri.cws.application.service.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.util.sql.AddProviderSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindProviderByNifSqlUnitOfWork;
import uo.ri.util.exception.BusinessException;

public class ProviderUtil {

	private ProviderDto dto = createDefaultDto();

	public ProviderUtil unique() {
		dto.nif = RandomStringUtils.randomAlphanumeric( 9 );
		dto.name = RandomStringUtils.randomAlphabetic(4) + "-name";
		dto.email = dto.name + "@mail.com";
		return this;
	}

	public ProviderUtil withNif(String nif) {
		dto.nif = nif;
		return this;
	}

	public ProviderUtil withName(String name) {
		dto.name = name;
		return this;
	}

	public ProviderUtil withEmail(String email) {
		dto.email = email;
		return this;
	}

	public ProviderUtil withPhone(String phone) {
		dto.phone = phone;
		return this;
	}

	public ProviderUtil loadByNif(String nif) throws BusinessException {
		dto = findByNif(nif);
		return this;
	}

	public ProviderUtil register() {
		new AddProviderSqlUnitOfWork(dto).execute();
		return this;
	}

	public ProviderUtil registerIfNew() {
		ProviderDto p = findByNif( dto.nif );
		if ( p == null ) {
			register();
		}
		return this;
	}

	public ProviderDto get() {
		return dto;
	}

	private ProviderDto createDefaultDto() {
		ProviderDto dto = new ProviderDto();
		dto.id = UUID.randomUUID().toString();
		dto.email = "dummy@email.com";
		dto.name = "dummy-provider-name";
		dto.nif = "dummy-provider-nif";
		dto.phone = "dummy-provider-phone";
		return dto;
	}

	private ProviderDto findByNif(String nif) {
		return new FindProviderByNifSqlUnitOfWork(nif).execute().get();
	}

}
