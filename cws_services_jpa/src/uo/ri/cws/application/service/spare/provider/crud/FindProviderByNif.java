package uo.ri.cws.application.service.spare.provider.crud;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProviderRepository;
import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.spare.provider.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindProviderByNif implements Command<Optional<ProviderDto>> {

    private String nif;
    private ProviderRepository proRep = Factories.repository.forProvider();

    /**
     * @param provider nif
     * @return the provider identified by the nif or Optional.empty() if does
     *         not exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif is null
     */
    public FindProviderByNif(String nif) {
        ArgumentChecks.isNotNull(nif, "nif null");

        this.nif = nif;
    }

    @Override
    public Optional<ProviderDto> execute() throws BusinessException {
        return proRep.findByNif(nif).map(m -> DtoAssembler.toDto(m));
    }

}
