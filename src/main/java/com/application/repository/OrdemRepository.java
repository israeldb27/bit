package com.application.repository;

import com.application.domain.Ordem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Ordem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdemRepository extends JpaRepository<Ordem, Long> {

}
