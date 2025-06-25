package uo.ri.cws.application.service.spare.sparepart.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.SparePart;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddSparePart implements Command<SparePartDto> {
    private SparePartDto dto;
    private SparePartRepository repo = Factories.repository.forSparePart();

    public AddSparePart(SparePartDto arg) throws BusinessException {
        checks(arg);
        dto = new SparePartDto();
        dto.code = arg.code;
        dto.description = arg.description;
        dto.maxStock = arg.maxStock;
        dto.minStock = arg.minStock;
        dto.price = arg.price;
        dto.stock = arg.stock;
    }

    private void checks(SparePartDto arg) throws BusinessException {
        ArgumentChecks.isNotNull(arg, "Invalid dto");
        ArgumentChecks.isNotBlank(arg.code, "Invalid code");
        ArgumentChecks.isNotBlank(arg.description, "Invalid description");
        BusinessChecks.isTrue(arg.minStock >= 0, "Invalid min stock");
        BusinessChecks.isTrue(arg.maxStock >= 0, "Invalid min stock");
        BusinessChecks.isTrue(arg.minStock <= arg.maxStock,
            "Invalid min stock");
        BusinessChecks.isTrue(arg.stock >= 0, "Invalid min stock");
        BusinessChecks.isTrue(arg.price >= 0, "Invalid min stock");
    }

    @Override
    public SparePartDto execute() throws BusinessException {
        BusinessChecks.doesNotExist(repo.findByCode(dto.code),
            "Repeated mechanic");
        SparePart m = new SparePart(dto.code, dto.description, dto.price,
            dto.stock, dto.minStock, dto.maxStock);
        repo.add(m);
        dto.id = m.getId();
        dto.version = m.getVersion();
        return dto;
    }

}
