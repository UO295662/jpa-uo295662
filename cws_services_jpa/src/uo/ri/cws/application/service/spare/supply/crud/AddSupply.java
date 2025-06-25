package uo.ri.cws.application.service.spare.supply.crud;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProviderRepository;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.repository.SupplyRepository;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Provider;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Supply;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddSupply implements Command<SupplyDto> {

    private SupplyDto dto;
    private SupplyRepository supRepo = Factories.repository.forSupply();
    private ProviderRepository proRepo = Factories.repository.forProvider();
    private SparePartRepository spRepo = Factories.repository.forSparePart();

    /**
     * Registers a new supply in the system for the indicated provider and spare
     * part
     * 
     * @param dto
     * @return the id of the new registered supply
     * @throws BusinessException        in case of: - there already exist a
     *                                  supply for the provider and the spare
     *                                  part - the delivery term or the price
     *                                  are negative - does not exist the
     *                                  provider - does not exist the spare part
     * @throws IllegalArgumentException in case of: - the dto is null - the
     *                                  provider or the spare part are null
     */
    public AddSupply(SupplyDto dto) {
        ArgumentChecks.isNotNull(dto, "Invalid dto");
        ArgumentChecks.isNotNull(dto.provider, "Invalid provider");
        ArgumentChecks.isNotNull(dto.sparePart, "Invalid sparePart");
        this.dto = dto;
    }

    /*
     * @throws BusinessException in case of: - there already exist a supply for
     * the provider and the spare part - the delivery term or the price are
     * negative - does not exist the provider - does not exist the spare part
     */
    @Override
    public SupplyDto execute() throws BusinessException {
        checks();
        Provider p = proRepo.findById(dto.provider.id).get();
        SparePart sp = spRepo.findById(dto.sparePart.id).get();
        Supply s = new Supply(p, sp, dto.price, dto.deliveryTerm);
        supRepo.add(s);
        return dto;
    }

    private void checks() throws BusinessException {
        BusinessChecks.exists(supRepo.findById(dto.id));

        BusinessChecks.exists(
            supRepo.findByNifAndCode(dto.provider.nif, dto.sparePart.code));
        Supply s = supRepo.findById(dto.id).get();
        BusinessChecks.isTrue(s.getPrice() >= 0, "negative supply price");
        BusinessChecks.isTrue(s.getDeliveryTerm() >= 0,
            "negative deliveryTerm");
        BusinessChecks.exists(proRepo.findById(dto.provider.id));
        BusinessChecks.exists(spRepo.findById(dto.sparePart.id));
    }

}
