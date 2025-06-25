package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {

    private String mechanicId;
    private MechanicRepository mechanicRepo = Factories.repository
        .forMechanic();

    /**
     * Removes the mechanic from the system if the mechanic exists and has no
     * work orders assigned or interventions
     * 
     * @param idMechanic
     * @throws BusinessException        if: - the mechanic does not exist - the
     *                                  mechanic has workorders assigned - the
     *                                  mechanic has interventions done
     * @throws IllegalArgumentException if the id is null
     */
    public DeleteMechanic(String mechanicId) {
        ArgumentChecks.isNotNull(mechanicId, "Id invalido es null");

        this.mechanicId = mechanicId;
    }

    public Void execute() throws BusinessException {
        Optional<Mechanic> mo = null;
        mo = mechanicRepo.findById(mechanicId);
        BusinessChecks.exists(mo, "No existe el mecanico");

        Mechanic mechanic = mo.get();
        canBeDeleted(mechanic);
        mechanicRepo.remove(mechanic);

        return null;
    }

    private void canBeDeleted(Mechanic m) throws BusinessException {
        BusinessChecks.isTrue(m.getAssigned().isEmpty(),
            "El mecanico tiene workorders asignadas");
        BusinessChecks.isTrue(m.getInterventions().isEmpty(),
            "El mecanico tiene intervenciones");
    }

}
