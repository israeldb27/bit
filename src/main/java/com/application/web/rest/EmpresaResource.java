package com.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.application.domain.Empresa;

import com.application.repository.EmpresaRepository;
import com.application.repository.search.EmpresaSearchRepository;
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
 * REST controller for managing Empresa.
 */
@RestController
@RequestMapping("/api")
public class EmpresaResource {

    private final Logger log = LoggerFactory.getLogger(EmpresaResource.class);

    private static final String ENTITY_NAME = "empresa";

    private final EmpresaRepository empresaRepository;

    private final EmpresaSearchRepository empresaSearchRepository;

    public EmpresaResource(EmpresaRepository empresaRepository, EmpresaSearchRepository empresaSearchRepository) {
        this.empresaRepository = empresaRepository;
        this.empresaSearchRepository = empresaSearchRepository;
    }

    /**
     * POST  /empresas : Create a new empresa.
     *
     * @param empresa the empresa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new empresa, or with status 400 (Bad Request) if the empresa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/empresas")
    @Timed
    public ResponseEntity<Empresa> createEmpresa(@RequestBody Empresa empresa) throws URISyntaxException {
        log.debug("REST request to save Empresa : {}", empresa);
        if (empresa.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new empresa cannot already have an ID")).body(null);
        }
        Empresa result = empresaRepository.save(empresa);
        empresaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/empresas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /empresas : Updates an existing empresa.
     *
     * @param empresa the empresa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated empresa,
     * or with status 400 (Bad Request) if the empresa is not valid,
     * or with status 500 (Internal Server Error) if the empresa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/empresas")
    @Timed
    public ResponseEntity<Empresa> updateEmpresa(@RequestBody Empresa empresa) throws URISyntaxException {
        log.debug("REST request to update Empresa : {}", empresa);
        if (empresa.getId() == null) {
            return createEmpresa(empresa);
        }
        Empresa result = empresaRepository.save(empresa);
        empresaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, empresa.getId().toString()))
            .body(result);
    }

    /**
     * GET  /empresas : get all the empresas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of empresas in body
     */
    @GetMapping("/empresas")
    @Timed
    public List<Empresa> getAllEmpresas() {
        log.debug("REST request to get all Empresas");
        return empresaRepository.findAll();
        }

    /**
     * GET  /empresas/:id : get the "id" empresa.
     *
     * @param id the id of the empresa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the empresa, or with status 404 (Not Found)
     */
    @GetMapping("/empresas/{id}")
    @Timed
    public ResponseEntity<Empresa> getEmpresa(@PathVariable Long id) {
        log.debug("REST request to get Empresa : {}", id);
        Empresa empresa = empresaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(empresa));
    }

    /**
     * DELETE  /empresas/:id : delete the "id" empresa.
     *
     * @param id the id of the empresa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/empresas/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        log.debug("REST request to delete Empresa : {}", id);
        empresaRepository.delete(id);
        empresaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/empresas?query=:query : search for the empresa corresponding
     * to the query.
     *
     * @param query the query of the empresa search
     * @return the result of the search
     */
    @GetMapping("/_search/empresas")
    @Timed
    public List<Empresa> searchEmpresas(@RequestParam String query) {
        log.debug("REST request to search Empresas for query {}", query);
        return StreamSupport
            .stream(empresaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
