package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {

    private MechanicDto dto;
    private MechanicRepository repo = Factories.repository.forMechanic();

    /**
     * Updates values for the mechanic specified by the id field, just name and
     * surname will be updated
     * 
     * @param mechanic dto, the id field, name and surname cannot be null nor
     *                 blank
     * @throws BusinessException        if the mechanic does not exist
     * @throws IllegalArgumentException if - the dto is null - any of the id,
     *                                  name, surname fields is null or blank
     */
    public UpdateMechanic(MechanicDto dto) {
        argumentChecks(dto);

        this.dto = dto;
    }

    private void argumentChecks(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Null dto");

        ArgumentChecks.isNotNull(dto.id, "Null id");
        ArgumentChecks.isNotNull(dto.name, "Null name");
        ArgumentChecks.isNotNull(dto.surname, "Null surname");
        ArgumentChecks.isNotNull(dto.nif, "Null nif");

        ArgumentChecks.isNotBlank(dto.id, "Blank id");
        ArgumentChecks.isNotBlank(dto.name, "Blank name");
        ArgumentChecks.isNotBlank(dto.surname, "Blank surname");
    }

    public Void execute() throws BusinessException {
        Optional<Mechanic> om = repo.findById(dto.id);
        BusinessChecks.exists(om, "El mecanico no existe");

        Mechanic m = om.get();
        BusinessChecks.hasVersion(m.getVersion(), dto.version);
        m.setName(dto.name);
        m.setSurname(dto.surname);

        return null;
    }

}
