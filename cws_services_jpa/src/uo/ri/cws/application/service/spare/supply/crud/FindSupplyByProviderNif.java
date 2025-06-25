package uo.ri.cws.application.service.spare.supply.crud;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SupplyRepository;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.service.spare.supply.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindSupplyByProviderNif implements Command<List<SupplyDto>> {

    private String nif;

    private SupplyRepository supRepo = Factories.repository.forSupply();

    /**
     * @param provider nif
     * @return the list (might be empty) of supply dto served by the provider
     *         identified by its nif
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif is null
     */
    public FindSupplyByProviderNif(String nif) {
        ArgumentChecks.isNotNull(nif, "null nif");

        this.nif = nif;
    }

    @Override
    public List<SupplyDto> execute() throws BusinessException {
        return DtoAssembler.toSupplyDtoList(supRepo.findByProviderNif(nif));
    }

}
