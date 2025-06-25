package uo.ri.cws.application.service.spare.provider.crud;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProviderRepository;
import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Provider;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddProvider implements Command<ProviderDto> {

    private ProviderDto dto;
    private ProviderRepository proRep = Factories.repository.forProvider();

    /**
     * Adds a new provider to the system
     * 
     * @param dto
     * @return the ID for the new provided registered in the system
     * @throws BusinessException        in case of: - there already exist
     *                                  another provider with the same nif -
     *                                  there already exist another provider
     *                                  with different nif but same name, email
     *                                  and phone
     * @throws IllegalArgumentException in case of: - the dto is null - any of
     *                                  the nif, name, email or phone fields is
     *                                  null or empty - the email field does
     *                                  not, at least, contains an @ sign
     */
    public AddProvider(ProviderDto dto) {
        constructorChecks(dto);

        this.dto = dto;
    }

    private void constructorChecks(ProviderDto dto) {
        ArgumentChecks.isNotNull(dto, "Invalid dto");
        ArgumentChecks.isNotNull(dto.nif, "Null nif");
        ArgumentChecks.isNotNull(dto.name, "Null name");
        ArgumentChecks.isNotNull(dto.phone, "Null phone");
        ArgumentChecks.isNotNull(dto.email, "Null email");
        ArgumentChecks.isNotBlank(dto.name, "Invalid name");
        ArgumentChecks.isNotBlank(dto.phone, "Invalid phone");
        ArgumentChecks.isNotBlank(dto.email, "Invalid email");
        ArgumentChecks.isNotBlank(dto.nif, "Invalid nif");

        ArgumentChecks.isTrue(dto.email.contains("@"),
            "Invalid email: must contain '@'");
    }

    /*
     * @throws BusinessException in case of: - there already exist another
     * provider with the same nif - there already exist another provider with
     * different nif but same name, email and phone
     */
    @Override
    public ProviderDto execute() throws BusinessException {
        return insertProvider();
    }

    private ProviderDto insertProvider() throws BusinessException {
        checkDiferentNifSamePhoneNameEmail();
        BusinessChecks.isTrue(proRep.findByNif(dto.nif).isEmpty());

        proRep.add(new Provider(dto.nif, dto.name, dto.email, dto.phone));

        return dto;
    }

    private void checkDiferentNifSamePhoneNameEmail() throws BusinessException {
        for (Provider pr : proRep.findByName(dto.name)) {
            String email = pr.getEmail();
            String nif = pr.getNif();
            String telefono = pr.getPhone();
            String name = pr.getName();

            boolean telefonoValido = telefono.equals(dto.phone);
            boolean emailValido = email.equals(dto.email);
            boolean nifValido = !nif.equals(dto.nif);
            boolean nameValido = name.equals(dto.name);

            BusinessChecks.isFalse(
                telefonoValido && emailValido && nifValido && nameValido,
                "Existing provider with the name, phone, email and distinct nif");
        }
    }

}
