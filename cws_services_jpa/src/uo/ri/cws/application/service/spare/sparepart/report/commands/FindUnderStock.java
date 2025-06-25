package uo.ri.cws.application.service.spare.sparepart.report.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.service.spare.SparePartReportService.SparePartReportDto;
import uo.ri.cws.application.service.spare.sparepart.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.exception.BusinessException;

public class FindUnderStock implements Command<List<SparePartReportDto>> {
    private SparePartRepository repo = Factories.repository.forSparePart();

    @Override
    public List<SparePartReportDto> execute() throws BusinessException {
        return DtoAssembler.toSparePartRepoDtoList(repo.findUnderStock());
    }

}
