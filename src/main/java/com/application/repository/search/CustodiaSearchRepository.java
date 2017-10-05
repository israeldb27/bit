package com.application.repository.search;

import com.application.domain.Custodia;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Custodia entity.
 */
public interface CustodiaSearchRepository extends ElasticsearchRepository<Custodia, Long> {
}
