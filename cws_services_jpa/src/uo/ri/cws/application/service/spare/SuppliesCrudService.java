package uo.ri.cws.application.service.spare;

import java.util.List;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

public interface SuppliesCrudService {

    /**
     * Registers a new supply in the system for the indicated provider and spare
     * part
     * 
     * @param dto
     * @return the id of the new registered supply
     * @throws BusinessException        in case of: - there already exist a
     *                                  supply for the provider and the spare
     *                                  part - the delivery term or the price
     *                                  are negative - does not exist the
     *                                  provider - does not exist the spare part
     * @throws IllegalArgumentException in case of: - the dto is null - the
     *                                  provider or the spare part are null
     */
    SupplyDto add(SupplyDto dto) throws BusinessException;

    /**
     * Removes from the system the supply uniquely indicated by the nif and code
     * (a supply can be removed with no other considerations)
     * 
     * @param providers nif
     * @param spare     part code
     * @throws BusinessException        if the indicated supply does not exist
     * @throws IllegalArgumentException if the nif or code are null
     */
    void delete(String nif, String code) throws BusinessException;

    /**
     * Updates the supply with the new data from the incoming dto
     * 
     * @param dto
     * @throws BusinessException        in case of: - it does not exist a supply
     *                                  for the provider and the spare part -
     *                                  the delivery term or the price are
     *                                  negative - the version field of the
     *                                  incoming dto does not match the version
     *                                  of the persistent entity
     * @throws IllegalArgumentException in case of: - the dto is null - the
     *                                  provider or the spare part are null
     */
    void update(SupplyDto dto) throws BusinessException;

    /**
     * @param nif
     * @param code
     * @return the supply dto uniquely identified by the provider nif and spare
     *         part code or Optional.empty() if does not exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif or code are null
     */
    Optional<SupplyDto> findByNifAndCode(String nif, String code)
        throws BusinessException;

    /**
     * @param provider nif
     * @return the list (might be empty) of supply dto served by the provider
     *         identified by its nif
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif is null
     */
    List<SupplyDto> findByProviderNif(String nif) throws BusinessException;

    /**
     * @param spare part code
     * @return the list (might be empty) of supply dto identified by the spare
     *         part code
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the code is null
     */
    List<SupplyDto> findBySparePartCode(String code) throws BusinessException;

    public static class SupplyDto {
        public String id;
        public long version;

        public SupplierProviderDto provider = new SupplierProviderDto();
        public SuppliedSparePartDto sparePart = new SuppliedSparePartDto();

        public double price;
        public int deliveryTerm;

        public static class SupplierProviderDto {
            public String id;
            public String nif;
            public String name;
        }

        public static class SuppliedSparePartDto {
            public String id;
            public String code;
            public String description;
        }
    }

}
