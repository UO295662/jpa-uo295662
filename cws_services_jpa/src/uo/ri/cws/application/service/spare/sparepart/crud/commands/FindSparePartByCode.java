package uo.ri.cws.application.service.spare.sparepart.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.spare.sparepart.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindSparePartByCode implements Command<Optional<SparePartDto>> {
    private String code;
    private SparePartRepository repo = Factories.repository.forSparePart();

    public FindSparePartByCode(String code) {
        ArgumentChecks.isNotBlank(code, "Código inválido");
        this.code = code;
    }

    @Override
    public Optional<SparePartDto> execute() throws BusinessException {
        return repo.findByCode(code).map(m -> DtoAssembler.toDto(m));
    }

}
