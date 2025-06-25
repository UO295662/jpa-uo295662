package uo.ri.cws.application.service.spare.provider;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.spare.ProvidersCrudService;
import uo.ri.cws.application.service.spare.provider.crud.AddProvider;
import uo.ri.cws.application.service.spare.provider.crud.DeleteProvider;
import uo.ri.cws.application.service.spare.provider.crud.FindProviderByName;
import uo.ri.cws.application.service.spare.provider.crud.FindProviderByNif;
import uo.ri.cws.application.service.spare.provider.crud.FindProviderBySparePartCode;
import uo.ri.cws.application.service.spare.provider.crud.UpdateProvider;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class ProvidersCrudServiceImpl implements ProvidersCrudService {

    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public ProviderDto add(ProviderDto dto) throws BusinessException {
        return executor.execute(new AddProvider(dto));
    }

    @Override
    public void delete(String nif) throws BusinessException {
        executor.execute(new DeleteProvider(nif));
    }

    @Override
    public void update(ProviderDto dto) throws BusinessException {
        executor.execute(new UpdateProvider(dto));
    }

    @Override
    public Optional<ProviderDto> findByNif(String nif)
        throws BusinessException {
        return executor.execute(new FindProviderByNif(nif));
    }

    @Override
    public List<ProviderDto> findByName(String name) throws BusinessException {
        return executor.execute(new FindProviderByName(name));
    }

    @Override
    public List<ProviderDto> findBySparePartCode(String code)
        throws BusinessException {
        return executor.execute(new FindProviderBySparePartCode(code));
    }

}
