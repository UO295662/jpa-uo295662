package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Provider;

public interface ProviderRepository extends Repository<Provider> {

    Optional<Provider> findByNif(String nif);

    List<Provider> findByNameMailPhone(String name, String email, String phone);

    List<Provider> findByName(String name);

    List<Provider> findBySparePartCode(String code);

}
