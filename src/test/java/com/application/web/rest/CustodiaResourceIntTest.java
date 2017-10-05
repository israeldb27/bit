package com.application.web.rest;

import com.application.BcbApp;

import com.application.domain.Custodia;
import com.application.repository.CustodiaRepository;
import com.application.repository.search.CustodiaSearchRepository;
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

import com.application.domain.enumeration.StatusCustodiaEmpresa;
/**
 * Test class for the CustodiaResource REST controller.
 *
 * @see CustodiaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BcbApp.class)
public class CustodiaResourceIntTest {

    private static final StatusCustodiaEmpresa DEFAULT_STATUS = StatusCustodiaEmpresa.ABERTA;
    private static final StatusCustodiaEmpresa UPDATED_STATUS = StatusCustodiaEmpresa.FECHADA;

    private static final ZonedDateTime DEFAULT_DATA_ABERTURA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ABERTURA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_FECHAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_FECHAMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_QUANTIDADE_TOTAL = 1L;
    private static final Long UPDATED_QUANTIDADE_TOTAL = 2L;

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;

    @Autowired
    private CustodiaRepository custodiaRepository;

    @Autowired
    private CustodiaSearchRepository custodiaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustodiaMockMvc;

    private Custodia custodia;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustodiaResource custodiaResource = new CustodiaResource(custodiaRepository, custodiaSearchRepository);
        this.restCustodiaMockMvc = MockMvcBuilders.standaloneSetup(custodiaResource)
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
    public static Custodia createEntity(EntityManager em) {
        Custodia custodia = new Custodia()
            .status(DEFAULT_STATUS)
            .dataAbertura(DEFAULT_DATA_ABERTURA)
            .dataFechamento(DEFAULT_DATA_FECHAMENTO)
            .quantidadeTotal(DEFAULT_QUANTIDADE_TOTAL)
            .valorTotal(DEFAULT_VALOR_TOTAL);
        return custodia;
    }

    @Before
    public void initTest() {
        custodiaSearchRepository.deleteAll();
        custodia = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustodia() throws Exception {
        int databaseSizeBeforeCreate = custodiaRepository.findAll().size();

        // Create the Custodia
        restCustodiaMockMvc.perform(post("/api/custodias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(custodia)))
            .andExpect(status().isCreated());

        // Validate the Custodia in the database
        List<Custodia> custodiaList = custodiaRepository.findAll();
        assertThat(custodiaList).hasSize(databaseSizeBeforeCreate + 1);
        Custodia testCustodia = custodiaList.get(custodiaList.size() - 1);
        assertThat(testCustodia.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCustodia.getDataAbertura()).isEqualTo(DEFAULT_DATA_ABERTURA);
        assertThat(testCustodia.getDataFechamento()).isEqualTo(DEFAULT_DATA_FECHAMENTO);
        assertThat(testCustodia.getQuantidadeTotal()).isEqualTo(DEFAULT_QUANTIDADE_TOTAL);
        assertThat(testCustodia.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);

        // Validate the Custodia in Elasticsearch
        Custodia custodiaEs = custodiaSearchRepository.findOne(testCustodia.getId());
        assertThat(custodiaEs).isEqualToComparingFieldByField(testCustodia);
    }

    @Test
    @Transactional
    public void createCustodiaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = custodiaRepository.findAll().size();

        // Create the Custodia with an existing ID
        custodia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustodiaMockMvc.perform(post("/api/custodias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(custodia)))
            .andExpect(status().isBadRequest());

        // Validate the Custodia in the database
        List<Custodia> custodiaList = custodiaRepository.findAll();
        assertThat(custodiaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustodias() throws Exception {
        // Initialize the database
        custodiaRepository.saveAndFlush(custodia);

        // Get all the custodiaList
        restCustodiaMockMvc.perform(get("/api/custodias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custodia.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dataAbertura").value(hasItem(sameInstant(DEFAULT_DATA_ABERTURA))))
            .andExpect(jsonPath("$.[*].dataFechamento").value(hasItem(sameInstant(DEFAULT_DATA_FECHAMENTO))))
            .andExpect(jsonPath("$.[*].quantidadeTotal").value(hasItem(DEFAULT_QUANTIDADE_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getCustodia() throws Exception {
        // Initialize the database
        custodiaRepository.saveAndFlush(custodia);

        // Get the custodia
        restCustodiaMockMvc.perform(get("/api/custodias/{id}", custodia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(custodia.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dataAbertura").value(sameInstant(DEFAULT_DATA_ABERTURA)))
            .andExpect(jsonPath("$.dataFechamento").value(sameInstant(DEFAULT_DATA_FECHAMENTO)))
            .andExpect(jsonPath("$.quantidadeTotal").value(DEFAULT_QUANTIDADE_TOTAL.intValue()))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustodia() throws Exception {
        // Get the custodia
        restCustodiaMockMvc.perform(get("/api/custodias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustodia() throws Exception {
        // Initialize the database
        custodiaRepository.saveAndFlush(custodia);
        custodiaSearchRepository.save(custodia);
        int databaseSizeBeforeUpdate = custodiaRepository.findAll().size();

        // Update the custodia
        Custodia updatedCustodia = custodiaRepository.findOne(custodia.getId());
        updatedCustodia
            .status(UPDATED_STATUS)
            .dataAbertura(UPDATED_DATA_ABERTURA)
            .dataFechamento(UPDATED_DATA_FECHAMENTO)
            .quantidadeTotal(UPDATED_QUANTIDADE_TOTAL)
            .valorTotal(UPDATED_VALOR_TOTAL);

        restCustodiaMockMvc.perform(put("/api/custodias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustodia)))
            .andExpect(status().isOk());

        // Validate the Custodia in the database
        List<Custodia> custodiaList = custodiaRepository.findAll();
        assertThat(custodiaList).hasSize(databaseSizeBeforeUpdate);
        Custodia testCustodia = custodiaList.get(custodiaList.size() - 1);
        assertThat(testCustodia.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCustodia.getDataAbertura()).isEqualTo(UPDATED_DATA_ABERTURA);
        assertThat(testCustodia.getDataFechamento()).isEqualTo(UPDATED_DATA_FECHAMENTO);
        assertThat(testCustodia.getQuantidadeTotal()).isEqualTo(UPDATED_QUANTIDADE_TOTAL);
        assertThat(testCustodia.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);

        // Validate the Custodia in Elasticsearch
        Custodia custodiaEs = custodiaSearchRepository.findOne(testCustodia.getId());
        assertThat(custodiaEs).isEqualToComparingFieldByField(testCustodia);
    }

    @Test
    @Transactional
    public void updateNonExistingCustodia() throws Exception {
        int databaseSizeBeforeUpdate = custodiaRepository.findAll().size();

        // Create the Custodia

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustodiaMockMvc.perform(put("/api/custodias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(custodia)))
            .andExpect(status().isCreated());

        // Validate the Custodia in the database
        List<Custodia> custodiaList = custodiaRepository.findAll();
        assertThat(custodiaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustodia() throws Exception {
        // Initialize the database
        custodiaRepository.saveAndFlush(custodia);
        custodiaSearchRepository.save(custodia);
        int databaseSizeBeforeDelete = custodiaRepository.findAll().size();

        // Get the custodia
        restCustodiaMockMvc.perform(delete("/api/custodias/{id}", custodia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean custodiaExistsInEs = custodiaSearchRepository.exists(custodia.getId());
        assertThat(custodiaExistsInEs).isFalse();

        // Validate the database is empty
        List<Custodia> custodiaList = custodiaRepository.findAll();
        assertThat(custodiaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustodia() throws Exception {
        // Initialize the database
        custodiaRepository.saveAndFlush(custodia);
        custodiaSearchRepository.save(custodia);

        // Search the custodia
        restCustodiaMockMvc.perform(get("/api/_search/custodias?query=id:" + custodia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custodia.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dataAbertura").value(hasItem(sameInstant(DEFAULT_DATA_ABERTURA))))
            .andExpect(jsonPath("$.[*].dataFechamento").value(hasItem(sameInstant(DEFAULT_DATA_FECHAMENTO))))
            .andExpect(jsonPath("$.[*].quantidadeTotal").value(hasItem(DEFAULT_QUANTIDADE_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Custodia.class);
        Custodia custodia1 = new Custodia();
        custodia1.setId(1L);
        Custodia custodia2 = new Custodia();
        custodia2.setId(custodia1.getId());
        assertThat(custodia1).isEqualTo(custodia2);
        custodia2.setId(2L);
        assertThat(custodia1).isNotEqualTo(custodia2);
        custodia1.setId(null);
        assertThat(custodia1).isNotEqualTo(custodia2);
    }
}
