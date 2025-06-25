package uo.ri.cws.application.service.spare.sparepart;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.spare.SparePartReportService.SparePartReportDto;
import uo.ri.cws.domain.SparePart;

public class DtoAssembler {

    public static SparePartDto toDto(SparePart sp) {
        SparePartDto dto = new SparePartDto();
        dto.id = sp.getId();
        dto.version = sp.getVersion();

        dto.code = sp.getCode();
        dto.description = sp.getDescription();
        dto.price = sp.getPrice();
        dto.stock = sp.getStock();
        dto.minStock = sp.getMinStock();
        dto.maxStock = sp.getMaxStock();

        return dto;
    }

    public static SparePartReportDto toSpareReportDto(SparePart sp) {
        SparePartReportDto dto = new SparePartReportDto();
        dto.id = sp.getId();
        dto.version = sp.getVersion();

        dto.code = sp.getCode();
        dto.description = sp.getDescription();
        dto.price = sp.getPrice();
        dto.stock = sp.getStock();
        dto.minStock = sp.getMinStock();
        dto.maxStock = sp.getMaxStock();

        dto.totalUnitsSold = sp.getTotalUnitsSold();

        return dto;
    }

    public static List<SparePartReportDto> toSparePartRepoDtoList(
        List<SparePart> list) {
        return list.stream().map(a -> toSpareReportDto(a))
            .collect(Collectors.toList());
    }

}
