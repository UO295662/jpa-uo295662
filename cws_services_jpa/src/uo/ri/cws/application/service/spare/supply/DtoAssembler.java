package uo.ri.cws.application.service.spare.supply;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.domain.Supply;

public class DtoAssembler {

    public static SupplyDto toDto(Supply s) {
        SupplyDto dto = new SupplyDto();
        dto.id = s.getId();
        dto.version = s.getVersion();

        dto.provider.id = s.getProvider().getId();
        dto.provider.nif = s.getProvider().getNif();
        dto.provider.name = s.getProvider().getName();

        dto.sparePart.id = s.getSparePart().getId();
        dto.sparePart.code = s.getSparePart().getCode();
        dto.sparePart.description = s.getSparePart().getDescription();

        dto.price = s.getPrice();
        dto.deliveryTerm = s.getDeliveryTerm();
        return dto;
    }

    public static List<SupplyDto> toSupplyDtoList(List<Supply> list) {
        return list.stream().map(a -> toDto(a)).collect(Collectors.toList());
    }

}
