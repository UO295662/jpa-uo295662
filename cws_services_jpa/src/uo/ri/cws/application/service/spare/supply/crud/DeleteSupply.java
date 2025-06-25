package uo.ri.cws.application.service.spare.supply.crud;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.SupplyRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Supply;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteSupply implements Command<Void> {

    private String nif;
    private String code;

    private SupplyRepository supRepo = Factories.repository.forSupply();

    /**
     * Removes from the system the supply uniquely indicated by the nif and code
     * (a supply can be removed with no other considerations)
     * 
     * @param providers nif
     * @param spare     part code
     * @throws BusinessException        if the indicated supply does not exist
     * @throws IllegalArgumentException if the nif or code are null
     */
    public DeleteSupply(String nif, String code) {
        ArgumentChecks.isNotNull(nif, "null nif");
        ArgumentChecks.isNotNull(code, "null code");

        this.nif = nif;
        this.code = code;
    }

    @Override
    public Void execute() throws BusinessException {
        BusinessChecks.isTrue(supRepo.findByProviderNif(nif).size() > 0,
            "no existe el actual supply");

        Supply s = supRepo.findByNifAndCode(nif, code).get();
        supRepo.remove(s);

        return null;
    }

}
