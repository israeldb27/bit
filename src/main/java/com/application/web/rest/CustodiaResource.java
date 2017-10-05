package com.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.application.domain.Custodia;

import com.application.repository.CustodiaRepository;
import com.application.repository.search.CustodiaSearchRepository;
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
 * REST controller for managing Custodia.
 */
@RestController
@RequestMapping("/api")
public class CustodiaResource {

    private final Logger log = LoggerFactory.getLogger(CustodiaResource.class);

    private static final String ENTITY_NAME = "custodia";

    private final CustodiaRepository custodiaRepository;

    private final CustodiaSearchRepository custodiaSearchRepository;

    public CustodiaResource(CustodiaRepository custodiaRepository, CustodiaSearchRepository custodiaSearchRepository) {
        this.custodiaRepository = custodiaRepository;
        this.custodiaSearchRepository = custodiaSearchRepository;
    }

    /**
     * POST  /custodias : Create a new custodia.
     *
     * @param custodia the custodia to create
     * @return the ResponseEntity with status 201 (Created) and with body the new custodia, or with status 400 (Bad Request) if the custodia has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/custodias")
    @Timed
    public ResponseEntity<Custodia> createCustodia(@RequestBody Custodia custodia) throws URISyntaxException {
        log.debug("REST request to save Custodia : {}", custodia);
        if (custodia.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new custodia cannot already have an ID")).body(null);
        }
        Custodia result = custodiaRepository.save(custodia);
        custodiaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/custodias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /custodias : Updates an existing custodia.
     *
     * @param custodia the custodia to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated custodia,
     * or with status 400 (Bad Request) if the custodia is not valid,
     * or with status 500 (Internal Server Error) if the custodia couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/custodias")
    @Timed
    public ResponseEntity<Custodia> updateCustodia(@RequestBody Custodia custodia) throws URISyntaxException {
        log.debug("REST request to update Custodia : {}", custodia);
        if (custodia.getId() == null) {
            return createCustodia(custodia);
        }
        Custodia result = custodiaRepository.save(custodia);
        custodiaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, custodia.getId().toString()))
            .body(result);
    }

    /**
     * GET  /custodias : get all the custodias.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of custodias in body
     */
    @GetMapping("/custodias")
    @Timed
    public List<Custodia> getAllCustodias() {
        log.debug("REST request to get all Custodias");
        return custodiaRepository.findAll();
        }

    /**
     * GET  /custodias/:id : get the "id" custodia.
     *
     * @param id the id of the custodia to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the custodia, or with status 404 (Not Found)
     */
    @GetMapping("/custodias/{id}")
    @Timed
    public ResponseEntity<Custodia> getCustodia(@PathVariable Long id) {
        log.debug("REST request to get Custodia : {}", id);
        Custodia custodia = custodiaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(custodia));
    }

    /**
     * DELETE  /custodias/:id : delete the "id" custodia.
     *
     * @param id the id of the custodia to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/custodias/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustodia(@PathVariable Long id) {
        log.debug("REST request to delete Custodia : {}", id);
        custodiaRepository.delete(id);
        custodiaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/custodias?query=:query : search for the custodia corresponding
     * to the query.
     *
     * @param query the query of the custodia search
     * @return the result of the search
     */
    @GetMapping("/_search/custodias")
    @Timed
    public List<Custodia> searchCustodias(@RequestParam String query) {
        log.debug("REST request to search Custodias for query {}", query);
        return StreamSupport
            .stream(custodiaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
