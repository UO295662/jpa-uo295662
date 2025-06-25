package uo.ri.cws.application.service.spare.supply.crud;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SupplyRepository;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.service.spare.supply.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindSupplyBySparePartCode implements Command<List<SupplyDto>> {

    private String code;

    private SupplyRepository supRepo = Factories.repository.forSupply();

    /**
     * @param spare part code
     * @return the list (might be empty) of supply dto identified by the spare
     *         part code
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the code is null
     */
    public FindSupplyBySparePartCode(String code) {
        ArgumentChecks.isNotNull(code, "null code");

        this.code = code;
    }

    @Override
    public List<SupplyDto> execute() throws BusinessException {
        return DtoAssembler.toSupplyDtoList(supRepo.findBySparePartCode(code));
    }

}
