package uo.ri.cws.application.service.mechanic.crud.command;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {

    private MechanicDto dto;
    private MechanicRepository mechanicRepo = Factories.repository
        .forMechanic();

    /**
     * Add a new mechanic to the system with the data specified in the dto. The
     * id value will be ignored
     * 
     * @param mecanico dto
     * @return the dto with the id filed updated to the UUID generated
     * @throws BusinessException        if there already exist another mechanic
     *                                  with the same nif
     * @throws IllegalArgumentException if - the dto is null - any of the nif,
     *                                  name, surname fields is null or blank
     */
    public AddMechanic(MechanicDto dto) {
        validateArgument(dto);

        this.dto = dto;
    }

    private void validateArgument(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Invalid null dto");

        ArgumentChecks.isNotBlank(dto.name, "Invalid blank name");
        ArgumentChecks.isNotBlank(dto.nif, "Invalid blank nif");
        ArgumentChecks.isNotBlank(dto.surname, "Invalid blank surname");
    }

    public MechanicDto execute() throws BusinessException {
        notRepeatedMechanic(dto.nif);

        Mechanic m = new Mechanic(dto.nif, dto.name, dto.surname);
        mechanicRepo.add(m);

        dto.id = m.getId();
        dto.version = m.getVersion();

        return dto;
    }

    private void notRepeatedMechanic(String nif) throws BusinessException {
        BusinessChecks.doesNotExist(mechanicRepo.findByNif(nif),
            "Repeated mechanic");
    }

}
