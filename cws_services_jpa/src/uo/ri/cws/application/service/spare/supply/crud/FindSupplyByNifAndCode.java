package uo.ri.cws.application.service.spare.supply.crud;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SupplyRepository;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.service.spare.supply.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindSupplyByNifAndCode implements Command<Optional<SupplyDto>> {

    private String nif;
    private String code;

    private SupplyRepository supRepo = Factories.repository.forSupply();

    /**
     * @param nif
     * @param code
     * @return the supply dto uniquely identified by the provider nif and spare
     *         part code or Optional.empty() if does not exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif or code are null
     */
    public FindSupplyByNifAndCode(String nif, String code) {
        ArgumentChecks.isNotNull(nif, "null nif");
        ArgumentChecks.isNotNull(code, "null code");
        this.nif = nif;
        this.code = code;
    }

    @Override
    public Optional<SupplyDto> execute() throws BusinessException {
        return supRepo.findByNifAndCode(nif, code)
            .map(m -> DtoAssembler.toDto(m));
    }

}
