package com.application.repository;

import com.application.domain.Empresa;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Empresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
