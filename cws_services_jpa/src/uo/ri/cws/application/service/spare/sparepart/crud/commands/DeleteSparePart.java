package uo.ri.cws.application.service.spare.sparepart.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.OrderRepository;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.SparePart;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteSparePart implements Command<Void> {
    private String code;
    private SparePartRepository repo = Factories.repository.forSparePart();
    private OrderRepository orderRepo = Factories.repository.forOrder();

    public DeleteSparePart(String code) {
        ArgumentChecks.isNotBlank(code, "Invalid code");
        ;
        this.code = code;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<SparePart> os;
        os = repo.findByCode(code);
        BusinessChecks.exists(os);
        SparePart s = os.get();
        canBeDeleted(s);
        repo.remove(s);
        return null;
    }

    private void canBeDeleted(SparePart s) throws BusinessException {
        BusinessChecks.isTrue(s.getSupplies().isEmpty(),
            "This spare part has suppliers");
        boolean hasOrderLines = !orderRepo.findBySparePartCode(code).isEmpty();
        BusinessChecks.isTrue(!hasOrderLines,
            "This spare part has order lines and cannot be deleted");
    }

}
