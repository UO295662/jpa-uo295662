package uo.ri.cws.application.service.spare.sparepart.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.spare.sparepart.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.SparePart;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateSparePart implements Command<SparePartDto> {
    private SparePartDto dto;
    private SparePartRepository repo = Factories.repository.forSparePart();

    public UpdateSparePart(SparePartDto arg) throws BusinessException {
        checks(arg);
        this.dto = arg;
    }

    private void checks(SparePartDto arg) throws BusinessException {
        ArgumentChecks.isNotNull(arg, "Invalid dto");
        ArgumentChecks.isNotBlank(arg.code, "Invalid code");
        ArgumentChecks.isNotBlank(arg.description, "Invalid description");
        BusinessChecks.isTrue(arg.minStock >= 0, "Invalid min stock");
        BusinessChecks.isTrue(arg.maxStock >= 0, "Invalid max stock");
        BusinessChecks.isTrue(arg.minStock <= arg.maxStock,
            "Invalid min stock");
        BusinessChecks.isTrue(arg.stock >= 0, "Invalid stock");
        BusinessChecks.isTrue(arg.price >= 0, "Invalid price");
        BusinessChecks.isTrue(arg.version >= 0, "version inválida");
    }

    @Override
    public SparePartDto execute() throws BusinessException {
        Optional<SparePart> sm = repo.findByCode(dto.code);
        BusinessChecks.exists(sm, "sparePart does not exist");

        SparePart m = sm.get();
        BusinessChecks.hasVersion(m.getVersion(), dto.version);
        m.setDescription(dto.description);
        m.setPrice(dto.price);
        m.setStock(dto.stock);
        m.setMinStock(dto.minStock);
        m.setMaxStock(dto.maxStock);

        return DtoAssembler.toDto(m);
    }
}