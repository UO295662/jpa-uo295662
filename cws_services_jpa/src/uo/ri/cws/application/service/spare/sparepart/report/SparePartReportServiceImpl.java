package uo.ri.cws.application.service.spare.sparepart.report;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.spare.SparePartReportService;
import uo.ri.cws.application.service.spare.sparepart.report.commands.FindByCode;
import uo.ri.cws.application.service.spare.sparepart.report.commands.FindByDescription;
import uo.ri.cws.application.service.spare.sparepart.report.commands.FindUnderStock;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class SparePartReportServiceImpl implements SparePartReportService {
    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public Optional<SparePartReportDto> findByCode(String code)
        throws BusinessException {
        return executor.execute(new FindByCode(code));
    }

    @Override
    public List<SparePartReportDto> findByDescription(String description)
        throws BusinessException {
        return executor.execute(new FindByDescription(description));
    }

    @Override
    public List<SparePartReportDto> findUnderStock() throws BusinessException {
        return executor.execute(new FindUnderStock());
    }

}
