package uo.ri.cws.application.service.spare.provider.crud;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProviderRepository;
import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.spare.provider.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindProviderBySparePartCode implements Command<List<ProviderDto>> {

    private String code;

    private ProviderRepository proRep = Factories.repository.forProvider();

    /**
     * @param code
     * @return a list with providers or empty if there is no one serving the
     *         spare part identified
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the code is null
     */
    public FindProviderBySparePartCode(String code) {
        ArgumentChecks.isNotNull(code, "invalid code");
        this.code = code;

    }

    @Override
    public List<ProviderDto> execute() throws BusinessException {
        return DtoAssembler
            .toProvidersDtoList(proRep.findBySparePartCode(code));
    }

}
