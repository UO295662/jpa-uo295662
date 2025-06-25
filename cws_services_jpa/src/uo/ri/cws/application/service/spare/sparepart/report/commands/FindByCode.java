package uo.ri.cws.application.service.spare.sparepart.report.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.service.spare.SparePartReportService.SparePartReportDto;
import uo.ri.cws.application.service.spare.sparepart.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindByCode implements Command<Optional<SparePartReportDto>> {
    private String code;
    private SparePartRepository repo = Factories.repository.forSparePart();

    public FindByCode(String code) {
        ArgumentChecks.isNotBlank(code, "Código inválido");
        this.code = code;
    }

    @Override
    public Optional<SparePartReportDto> execute() throws BusinessException {
        return repo.findByCode(code).map(m -> DtoAssembler.toSpareReportDto(m));
    }

}
