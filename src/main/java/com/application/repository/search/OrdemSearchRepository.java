package com.application.repository.search;

import com.application.domain.Ordem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ordem entity.
 */
public interface OrdemSearchRepository extends ElasticsearchRepository<Ordem, Long> {
}
