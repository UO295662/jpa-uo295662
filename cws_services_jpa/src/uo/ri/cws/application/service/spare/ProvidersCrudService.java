package uo.ri.cws.application.service.spare;

import java.util.List;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

public interface ProvidersCrudService {

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
    ProviderDto add(ProviderDto dto) throws BusinessException;

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
    void delete(String nif) throws BusinessException;

    /**
     * Updates all the field for the provider except the id, nif and version. Id
     * and nif will signal the provider to update and version will be internally
     * updated after committing.
     * 
     * @param dto
     * @throws BusinessException        in case of: - it does not exist any
     *                                  provider with the nif - there already
     *                                  exist another provider with different
     *                                  nif but same name, email and phone once
     *                                  the provided be updated - the version
     *                                  field of the incoming dto does not match
     *                                  the version of the persistent entity
     * @throws IllegalArgumentException in case of: - the dto is null - any of
     *                                  the four fields are null or empty - the
     *                                  email field does not, at least, contains
     *                                  an @ sign
     */
    void update(ProviderDto dto) throws BusinessException;

    /**
     * @param provider nif
     * @return the provider identified by the nif or Optional.empty() if does
     *         not exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif is null
     */
    Optional<ProviderDto> findByNif(String nif) throws BusinessException;

    /**
     * Remember there could be several providers with different nif but the same
     * name
     * 
     * @param name
     * @return a list with providers or empty if there is no one with this name
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the name is null
     */
    List<ProviderDto> findByName(String name) throws BusinessException;

    /**
     * @param code
     * @return a list with providers or empty if there is no one serving the
     *         spare part identified
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the code is null
     */
    List<ProviderDto> findBySparePartCode(String code) throws BusinessException;

    public static class ProviderDto {
        public String id;
        public long version;

        public String nif;
        public String name;
        public String email;
        public String phone;
    }

}
