package uo.ri.cws.application.service.spare.supply;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.spare.SuppliesCrudService;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;
import uo.ri.cws.application.service.spare.supply.crud.*;

public class SuppliesCrudServiceImpl implements SuppliesCrudService {

    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public SupplyDto add(SupplyDto dto) throws BusinessException {
        return executor.execute(new AddSupply(dto));
    }

    @Override
    public void delete(String nif, String code) throws BusinessException {
        executor.execute(new DeleteSupply(nif, code));
    }

    @Override
    public void update(SupplyDto dto) throws BusinessException {
        executor.execute(new UpdateSupply(dto));
    }

    @Override
    public Optional<SupplyDto> findByNifAndCode(String nif, String code)
        throws BusinessException {
        return executor.execute(new FindSupplyByNifAndCode(nif, code));
    }

    @Override
    public List<SupplyDto> findByProviderNif(String nif)
        throws BusinessException {
        return executor.execute(new FindSupplyByProviderNif(nif));
    }

    @Override
    public List<SupplyDto> findBySparePartCode(String code)
        throws BusinessException {
        return executor.execute(new FindSupplyBySparePartCode(code));
    }

}
