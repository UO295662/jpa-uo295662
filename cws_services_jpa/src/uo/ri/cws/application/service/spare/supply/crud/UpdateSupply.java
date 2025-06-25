package uo.ri.cws.application.service.spare.supply.crud;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SupplyRepository;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Supply;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateSupply implements Command<Void> {

    private SupplyDto dto;

    private SupplyRepository supRepo = Factories.repository.forSupply();

    /**
     * Updates the supply with the new data from the incoming dto
     * 
     * @param dto
     * @throws BusinessException        in case of: - it does not exist a supply
     *                                  for the provider and the spare part -
     *                                  the delivery term or the price are
     *                                  negative - the version field of the
     *                                  incoming dto does not match the version
     *                                  of the persistent entity
     * @throws IllegalArgumentException in case of: - the dto is null - the
     *                                  provider or the spare part are null
     */
    public UpdateSupply(SupplyDto dto) {
        ArgumentChecks.isNotNull(dto, "Dto es null");
        ArgumentChecks.isNotNull(dto.provider, "provider es null");
        ArgumentChecks.isNotNull(dto.sparePart, "sparePart es null");

        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        businessChecks();
        Optional<Supply> os = supRepo.findById(dto.id);
        Supply s = os.get();
        s.setPrice(dto.price);
        s.setDeliveryTerm(dto.deliveryTerm);

        return null;
    }

    /*
     * @throws BusinessException in case of: - it does not exist a supply for
     * the provider and the spare part - the delivery term or the price are
     * negative - the version field of the incoming dto does not match the
     * version of the persistent entity
     */
    private void businessChecks() throws BusinessException {
        BusinessChecks.exists(
            supRepo.findByNifAndCode(dto.provider.nif, dto.sparePart.code));
        Supply s = supRepo.findById(dto.id).get();
        BusinessChecks.isTrue(dto.price >= 0,
            "el precio del supply es negativo");
        BusinessChecks.isTrue(dto.deliveryTerm >= 0,
            "el deliveryTerm es negativo");

        BusinessChecks.hasVersion(s.getVersion(), dto.version);
    }

}
