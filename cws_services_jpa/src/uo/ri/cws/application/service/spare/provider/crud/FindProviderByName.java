package uo.ri.cws.application.service.spare.provider.crud;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProviderRepository;
import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.spare.provider.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Provider;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindProviderByName implements Command<List<ProviderDto>> {

    private String name;
    private ProviderRepository proRep = Factories.repository.forProvider();

    /**
     * Remember there could be several providers with different nif but the same
     * name
     * 
     * @param name
     * @return a list with providers or empty if there is no one with this name
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the name is null
     */
    public FindProviderByName(String name) {
        ArgumentChecks.isNotNull(name, "Nombre null");
        this.name = name;
    }

    @Override
    public List<ProviderDto> execute() throws BusinessException {
        List<Provider> list = proRep.findByName(name);
        return DtoAssembler.toProvidersDtoList(list);
    }

}
