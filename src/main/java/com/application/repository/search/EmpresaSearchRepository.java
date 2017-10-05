package com.application.repository.search;

import com.application.domain.Empresa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Empresa entity.
 */
public interface EmpresaSearchRepository extends ElasticsearchRepository<Empresa, Long> {
}
