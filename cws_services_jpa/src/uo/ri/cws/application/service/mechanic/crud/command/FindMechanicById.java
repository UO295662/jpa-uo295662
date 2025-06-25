package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindMechanicById implements Command<Optional<MechanicDto>> {

    private String id;
    private MechanicRepository repo = Factories.repository.forMechanic();

    /**
     * @param id of the mechanic
     * @return an optional with the dto. Might be empty if the nif does not
     *         exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the id is null
     */
    public FindMechanicById(String id) {
        ArgumentChecks.isNotNull(id, "Id null");

        this.id = id;
    }

    public Optional<MechanicDto> execute() throws BusinessException {
        return repo.findById(id).map(m -> DtoAssembler.toDto(m));
    }

}
