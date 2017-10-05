package com.application.repository;

import com.application.domain.Custodia;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Custodia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustodiaRepository extends JpaRepository<Custodia, Long> {

}
