package com.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.application.domain.Ordem;

import com.application.repository.OrdemRepository;
import com.application.repository.search.OrdemSearchRepository;
import com.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Ordem.
 */
@RestController
@RequestMapping("/api")
public class OrdemResource {

    private final Logger log = LoggerFactory.getLogger(OrdemResource.class);

    private static final String ENTITY_NAME = "ordem";

    private final OrdemRepository ordemRepository;

    private final OrdemSearchRepository ordemSearchRepository;

    public OrdemResource(OrdemRepository ordemRepository, OrdemSearchRepository ordemSearchRepository) {
        this.ordemRepository = ordemRepository;
        this.ordemSearchRepository = ordemSearchRepository;
    }

    /**
     * POST  /ordems : Create a new ordem.
     *
     * @param ordem the ordem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ordem, or with status 400 (Bad Request) if the ordem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ordems")
    @Timed
    public ResponseEntity<Ordem> createOrdem(@RequestBody Ordem ordem) throws URISyntaxException {
        log.debug("REST request to save Ordem : {}", ordem);
        if (ordem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ordem cannot already have an ID")).body(null);
        }
        Ordem result = ordemRepository.save(ordem);
        ordemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ordems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ordems : Updates an existing ordem.
     *
     * @param ordem the ordem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ordem,
     * or with status 400 (Bad Request) if the ordem is not valid,
     * or with status 500 (Internal Server Error) if the ordem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ordems")
    @Timed
    public ResponseEntity<Ordem> updateOrdem(@RequestBody Ordem ordem) throws URISyntaxException {
        log.debug("REST request to update Ordem : {}", ordem);
        if (ordem.getId() == null) {
            return createOrdem(ordem);
        }
        Ordem result = ordemRepository.save(ordem);
        ordemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ordem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ordems : get all the ordems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ordems in body
     */
    @GetMapping("/ordems")
    @Timed
    public List<Ordem> getAllOrdems() {
        log.debug("REST request to get all Ordems");
        return ordemRepository.findAll();
        }

    /**
     * GET  /ordems/:id : get the "id" ordem.
     *
     * @param id the id of the ordem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ordem, or with status 404 (Not Found)
     */
    @GetMapping("/ordems/{id}")
    @Timed
    public ResponseEntity<Ordem> getOrdem(@PathVariable Long id) {
        log.debug("REST request to get Ordem : {}", id);
        Ordem ordem = ordemRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ordem));
    }

    /**
     * DELETE  /ordems/:id : delete the "id" ordem.
     *
     * @param id the id of the ordem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ordems/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrdem(@PathVariable Long id) {
        log.debug("REST request to delete Ordem : {}", id);
        ordemRepository.delete(id);
        ordemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ordems?query=:query : search for the ordem corresponding
     * to the query.
     *
     * @param query the query of the ordem search
     * @return the result of the search
     */
    @GetMapping("/_search/ordems")
    @Timed
    public List<Ordem> searchOrdems(@RequestParam String query) {
        log.debug("REST request to search Ordems for query {}", query);
        return StreamSupport
            .stream(ordemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
