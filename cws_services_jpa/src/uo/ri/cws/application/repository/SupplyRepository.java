package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Supply;

public interface SupplyRepository extends Repository<Supply> {

    Optional<Supply> findByNifAndCode(String nif, String code);

    List<Supply> findByProviderNif(String nif);

    List<Supply> findBySparePartCode(String code);

}
