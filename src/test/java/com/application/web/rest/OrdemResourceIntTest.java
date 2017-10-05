package com.application.web.rest;

import com.application.BcbApp;

import com.application.domain.Ordem;
import com.application.repository.OrdemRepository;
import com.application.repository.search.OrdemSearchRepository;
import com.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.application.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.application.domain.enumeration.TipoOrdermEnum;
import com.application.domain.enumeration.StatusOrdemEnum;
/**
 * Test class for the OrdemResource REST controller.
 *
 * @see OrdemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BcbApp.class)
public class OrdemResourceIntTest {

    private static final TipoOrdermEnum DEFAULT_TIPO = TipoOrdermEnum.COMPRA;
    private static final TipoOrdermEnum UPDATED_TIPO = TipoOrdermEnum.VENDA;

    private static final Double DEFAULT_VALOR_ORDEM = 1D;
    private static final Double UPDATED_VALOR_ORDEM = 2D;

    private static final Long DEFAULT_QUANTIDADE = 1L;
    private static final Long UPDATED_QUANTIDADE = 2L;

    private static final ZonedDateTime DEFAULT_DATA_ORDEM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ORDEM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final StatusOrdemEnum DEFAULT_STATUS = StatusOrdemEnum.CRIADO;
    private static final StatusOrdemEnum UPDATED_STATUS = StatusOrdemEnum.EXECUTADO;

    @Autowired
    private OrdemRepository ordemRepository;

    @Autowired
    private OrdemSearchRepository ordemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdemMockMvc;

    private Ordem ordem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdemResource ordemResource = new OrdemResource(ordemRepository, ordemSearchRepository);
        this.restOrdemMockMvc = MockMvcBuilders.standaloneSetup(ordemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordem createEntity(EntityManager em) {
        Ordem ordem = new Ordem()
            .tipo(DEFAULT_TIPO)
            .valorOrdem(DEFAULT_VALOR_ORDEM)
            .quantidade(DEFAULT_QUANTIDADE)
            .dataOrdem(DEFAULT_DATA_ORDEM)
            .status(DEFAULT_STATUS);
        return ordem;
    }

    @Before
    public void initTest() {
        ordemSearchRepository.deleteAll();
        ordem = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdem() throws Exception {
        int databaseSizeBeforeCreate = ordemRepository.findAll().size();

        // Create the Ordem
        restOrdemMockMvc.perform(post("/api/ordems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordem)))
            .andExpect(status().isCreated());

        // Validate the Ordem in the database
        List<Ordem> ordemList = ordemRepository.findAll();
        assertThat(ordemList).hasSize(databaseSizeBeforeCreate + 1);
        Ordem testOrdem = ordemList.get(ordemList.size() - 1);
        assertThat(testOrdem.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testOrdem.getValorOrdem()).isEqualTo(DEFAULT_VALOR_ORDEM);
        assertThat(testOrdem.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testOrdem.getDataOrdem()).isEqualTo(DEFAULT_DATA_ORDEM);
        assertThat(testOrdem.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Ordem in Elasticsearch
        Ordem ordemEs = ordemSearchRepository.findOne(testOrdem.getId());
        assertThat(ordemEs).isEqualToComparingFieldByField(testOrdem);
    }

    @Test
    @Transactional
    public void createOrdemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordemRepository.findAll().size();

        // Create the Ordem with an existing ID
        ordem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdemMockMvc.perform(post("/api/ordems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordem)))
            .andExpect(status().isBadRequest());

        // Validate the Ordem in the database
        List<Ordem> ordemList = ordemRepository.findAll();
        assertThat(ordemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrdems() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);

        // Get all the ordemList
        restOrdemMockMvc.perform(get("/api/ordems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordem.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].valorOrdem").value(hasItem(DEFAULT_VALOR_ORDEM.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE.intValue())))
            .andExpect(jsonPath("$.[*].dataOrdem").value(hasItem(sameInstant(DEFAULT_DATA_ORDEM))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getOrdem() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);

        // Get the ordem
        restOrdemMockMvc.perform(get("/api/ordems/{id}", ordem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ordem.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.valorOrdem").value(DEFAULT_VALOR_ORDEM.doubleValue()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE.intValue()))
            .andExpect(jsonPath("$.dataOrdem").value(sameInstant(DEFAULT_DATA_ORDEM)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrdem() throws Exception {
        // Get the ordem
        restOrdemMockMvc.perform(get("/api/ordems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdem() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);
        ordemSearchRepository.save(ordem);
        int databaseSizeBeforeUpdate = ordemRepository.findAll().size();

        // Update the ordem
        Ordem updatedOrdem = ordemRepository.findOne(ordem.getId());
        updatedOrdem
            .tipo(UPDATED_TIPO)
            .valorOrdem(UPDATED_VALOR_ORDEM)
            .quantidade(UPDATED_QUANTIDADE)
            .dataOrdem(UPDATED_DATA_ORDEM)
            .status(UPDATED_STATUS);

        restOrdemMockMvc.perform(put("/api/ordems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrdem)))
            .andExpect(status().isOk());

        // Validate the Ordem in the database
        List<Ordem> ordemList = ordemRepository.findAll();
        assertThat(ordemList).hasSize(databaseSizeBeforeUpdate);
        Ordem testOrdem = ordemList.get(ordemList.size() - 1);
        assertThat(testOrdem.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testOrdem.getValorOrdem()).isEqualTo(UPDATED_VALOR_ORDEM);
        assertThat(testOrdem.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testOrdem.getDataOrdem()).isEqualTo(UPDATED_DATA_ORDEM);
        assertThat(testOrdem.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Ordem in Elasticsearch
        Ordem ordemEs = ordemSearchRepository.findOne(testOrdem.getId());
        assertThat(ordemEs).isEqualToComparingFieldByField(testOrdem);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdem() throws Exception {
        int databaseSizeBeforeUpdate = ordemRepository.findAll().size();

        // Create the Ordem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrdemMockMvc.perform(put("/api/ordems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordem)))
            .andExpect(status().isCreated());

        // Validate the Ordem in the database
        List<Ordem> ordemList = ordemRepository.findAll();
        assertThat(ordemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrdem() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);
        ordemSearchRepository.save(ordem);
        int databaseSizeBeforeDelete = ordemRepository.findAll().size();

        // Get the ordem
        restOrdemMockMvc.perform(delete("/api/ordems/{id}", ordem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ordemExistsInEs = ordemSearchRepository.exists(ordem.getId());
        assertThat(ordemExistsInEs).isFalse();

        // Validate the database is empty
        List<Ordem> ordemList = ordemRepository.findAll();
        assertThat(ordemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrdem() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);
        ordemSearchRepository.save(ordem);

        // Search the ordem
        restOrdemMockMvc.perform(get("/api/_search/ordems?query=id:" + ordem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordem.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].valorOrdem").value(hasItem(DEFAULT_VALOR_ORDEM.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE.intValue())))
            .andExpect(jsonPath("$.[*].dataOrdem").value(hasItem(sameInstant(DEFAULT_DATA_ORDEM))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ordem.class);
        Ordem ordem1 = new Ordem();
        ordem1.setId(1L);
        Ordem ordem2 = new Ordem();
        ordem2.setId(ordem1.getId());
        assertThat(ordem1).isEqualTo(ordem2);
        ordem2.setId(2L);
        assertThat(ordem1).isNotEqualTo(ordem2);
        ordem1.setId(null);
        assertThat(ordem1).isNotEqualTo(ordem2);
    }
}
