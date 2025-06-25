package uo.ri.cws.application.service.spare;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.util.exception.BusinessException;

/**
 * This service is intended to be used by the Manager It follows the ISP
 * principle (@see SOLID principles from RC Martin)
 */
public interface SparePartReportService {

    /**
     * Note: this operation is quite similar to
     * 
     * @see SparePartCrudService.findByCode, but this one returns
     * @see SparePartReportDto instead of
     * @see SparePartDto (that).
     * @param spare part code
     * @return the spare part identified by the code or Optional.empty() if does
     *         not exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the code is null
     */
    Optional<SparePartReportDto> findByCode(String code)
        throws BusinessException;

    /**
     * Returns a list (might be empty) of all the spare whose description
     * matches the pattern %<description>%. Thus, description may be a partial
     * description
     * 
     * @param a (partial) description of the spare part
     * @return a list with spare part reports or empty if there is no one with
     *         this name
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the description is null
     */
    List<SparePartReportDto> findByDescription(String description)
        throws BusinessException;

    /**
     * @return a list with spare part reports or empty if there is no one for
     *         all the spares whose stock is < min stock
     * @throws BusinessException DOES NOT
     */
    List<SparePartReportDto> findUnderStock() throws BusinessException;

    public class SparePartReportDto {
        public String id;
        public long version;

        public String code;
        public String description;
        public double price;
        public int stock;
        public int minStock;
        public int maxStock;
        public int totalUnitsSold;
    }
}
