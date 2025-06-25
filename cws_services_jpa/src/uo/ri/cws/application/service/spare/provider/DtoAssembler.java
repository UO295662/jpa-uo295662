package uo.ri.cws.application.service.spare.provider;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.domain.Provider;

public class DtoAssembler {

    public static ProviderDto toDto(Provider p) {
        ProviderDto dto = new ProviderDto();
        dto.id = p.getId();
        dto.version = p.getVersion();

        dto.nif = p.getNif();
        dto.name = p.getName();
        dto.email = p.getEmail();
        dto.phone = p.getPhone();

        return dto;
    }

    public static List<ProviderDto> toProvidersDtoList(List<Provider> list) {
        return list.stream().map(a -> toDto(a)).collect(Collectors.toList());
    }

}
