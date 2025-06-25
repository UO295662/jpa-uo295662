package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindMechanicByNif implements Command<Optional<MechanicDto>> {

    private String nif;
    private MechanicRepository repo = Factories.repository.forMechanic();

    /**
     * @param nif of the mechanic
     * @return an optional with the dto. Might be empty if the nif does not
     *         exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif is null
     */
    public FindMechanicByNif(String nif) {
        ArgumentChecks.isNotEmpty(nif);

        this.nif = nif;
    }

    @Override
    public Optional<MechanicDto> execute() throws BusinessException {
        return repo.findByNif(nif).map(m -> DtoAssembler.toDto(m));
    }

}
