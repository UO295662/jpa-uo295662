package uo.ri.cws.application.service.spare.provider.crud;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.OrderRepository;
import uo.ri.cws.application.repository.ProviderRepository;
import uo.ri.cws.application.repository.SupplyRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Provider;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteProvider implements Command<Void> {

    private String nif;

    private ProviderRepository proRep = Factories.repository.forProvider();
    private OrderRepository orRep = Factories.repository.forOrder();
    private SupplyRepository supRep = Factories.repository.forSupply();

    /**
     * Removes the provider indicated by its nif from the system. It can only be
     * removed if the provider has no supplies or orders attached to it.
     * 
     * @param nif
     * @throws BusinessException        in case of: - there is no provider with
     *                                  that nif - the provider has supplies -
     *                                  the provider has orders
     * @throws IllegalArgumentException if the nif is null
     */
    public DeleteProvider(String nif) {
        ArgumentChecks.isNotNull(nif, "Invalid provider nif");
        this.nif = nif;
    }

    @Override
    public Void execute() throws BusinessException {
        BusinessChecks.exists(proRep.findByNif(nif),
            "Existe un provider con el mismo nif");
        BusinessChecks.isTrue(orRep.findByProviderNif(nif).isEmpty(),
            "Existen order lines para en el provider");
        BusinessChecks.isTrue(supRep.findByProviderNif(nif).isEmpty(),
            "Existen supplies para en el provider");
        Provider provider = proRep.findByNif(nif).get();
        proRep.remove(provider);

        return null;
    }

}
