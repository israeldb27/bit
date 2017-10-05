package com.application.web.rest;

import com.application.BcbApp;

import com.application.domain.Empresa;
import com.application.repository.EmpresaRepository;
import com.application.repository.search.EmpresaSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.application.domain.enumeration.StatusEmpresaEnum;
/**
 * Test class for the EmpresaResource REST controller.
 *
 * @see EmpresaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BcbApp.class)
public class EmpresaResourceIntTest {

    private static final String DEFAULT_RAZAO_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZAO_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CADASTRO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_ULTIMA_ATUALIZACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_ULTIMA_ATUALIZACAO = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_CNPJ = 1L;
    private static final Long UPDATED_CNPJ = 2L;

    private static final StatusEmpresaEnum DEFAULT_STATUS = StatusEmpresaEnum.CRIADA;
    private static final StatusEmpresaEnum UPDATED_STATUS = StatusEmpresaEnum.CANCELADA;

    private static final Long DEFAULT_QUANTIDADE_ACOES = 1L;
    private static final Long UPDATED_QUANTIDADE_ACOES = 2L;

    private static final Double DEFAULT_VALOR_INICIAL = 1D;
    private static final Double UPDATED_VALOR_INICIAL = 2D;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaSearchRepository empresaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmpresaMockMvc;

    private Empresa empresa;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmpresaResource empresaResource = new EmpresaResource(empresaRepository, empresaSearchRepository);
        this.restEmpresaMockMvc = MockMvcBuilders.standaloneSetup(empresaResource)
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
    public static Empresa createEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .razaoSocial(DEFAULT_RAZAO_SOCIAL)
            .nome(DEFAULT_NOME)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .dataUltimaAtualizacao(DEFAULT_DATA_ULTIMA_ATUALIZACAO)
            .cnpj(DEFAULT_CNPJ)
            .status(DEFAULT_STATUS)
            .quantidadeAcoes(DEFAULT_QUANTIDADE_ACOES)
            .valorInicial(DEFAULT_VALOR_INICIAL);
        return empresa;
    }

    @Before
    public void initTest() {
        empresaSearchRepository.deleteAll();
        empresa = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmpresa() throws Exception {
        int databaseSizeBeforeCreate = empresaRepository.findAll().size();

        // Create the Empresa
        restEmpresaMockMvc.perform(post("/api/empresas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isCreated());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate + 1);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testEmpresa.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEmpresa.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testEmpresa.getDataUltimaAtualizacao()).isEqualTo(DEFAULT_DATA_ULTIMA_ATUALIZACAO);
        assertThat(testEmpresa.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testEmpresa.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEmpresa.getQuantidadeAcoes()).isEqualTo(DEFAULT_QUANTIDADE_ACOES);
        assertThat(testEmpresa.getValorInicial()).isEqualTo(DEFAULT_VALOR_INICIAL);

        // Validate the Empresa in Elasticsearch
        Empresa empresaEs = empresaSearchRepository.findOne(testEmpresa.getId());
        assertThat(empresaEs).isEqualToComparingFieldByField(testEmpresa);
    }

    @Test
    @Transactional
    public void createEmpresaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = empresaRepository.findAll().size();

        // Create the Empresa with an existing ID
        empresa.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpresaMockMvc.perform(post("/api/empresas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEmpresas() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList
        restEmpresaMockMvc.perform(get("/api/empresas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].dataUltimaAtualizacao").value(hasItem(DEFAULT_DATA_ULTIMA_ATUALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].quantidadeAcoes").value(hasItem(DEFAULT_QUANTIDADE_ACOES.intValue())))
            .andExpect(jsonPath("$.[*].valorInicial").value(hasItem(DEFAULT_VALOR_INICIAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get the empresa
        restEmpresaMockMvc.perform(get("/api/empresas/{id}", empresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(empresa.getId().intValue()))
            .andExpect(jsonPath("$.razaoSocial").value(DEFAULT_RAZAO_SOCIAL.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO.toString()))
            .andExpect(jsonPath("$.dataUltimaAtualizacao").value(DEFAULT_DATA_ULTIMA_ATUALIZACAO.toString()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.quantidadeAcoes").value(DEFAULT_QUANTIDADE_ACOES.intValue()))
            .andExpect(jsonPath("$.valorInicial").value(DEFAULT_VALOR_INICIAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEmpresa() throws Exception {
        // Get the empresa
        restEmpresaMockMvc.perform(get("/api/empresas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        empresaSearchRepository.save(empresa);
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa
        Empresa updatedEmpresa = empresaRepository.findOne(empresa.getId());
        updatedEmpresa
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .nome(UPDATED_NOME)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataUltimaAtualizacao(UPDATED_DATA_ULTIMA_ATUALIZACAO)
            .cnpj(UPDATED_CNPJ)
            .status(UPDATED_STATUS)
            .quantidadeAcoes(UPDATED_QUANTIDADE_ACOES)
            .valorInicial(UPDATED_VALOR_INICIAL);

        restEmpresaMockMvc.perform(put("/api/empresas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmpresa)))
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testEmpresa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEmpresa.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testEmpresa.getDataUltimaAtualizacao()).isEqualTo(UPDATED_DATA_ULTIMA_ATUALIZACAO);
        assertThat(testEmpresa.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testEmpresa.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmpresa.getQuantidadeAcoes()).isEqualTo(UPDATED_QUANTIDADE_ACOES);
        assertThat(testEmpresa.getValorInicial()).isEqualTo(UPDATED_VALOR_INICIAL);

        // Validate the Empresa in Elasticsearch
        Empresa empresaEs = empresaSearchRepository.findOne(testEmpresa.getId());
        assertThat(empresaEs).isEqualToComparingFieldByField(testEmpresa);
    }

    @Test
    @Transactional
    public void updateNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Create the Empresa

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmpresaMockMvc.perform(put("/api/empresas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isCreated());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        empresaSearchRepository.save(empresa);
        int databaseSizeBeforeDelete = empresaRepository.findAll().size();

        // Get the empresa
        restEmpresaMockMvc.perform(delete("/api/empresas/{id}", empresa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean empresaExistsInEs = empresaSearchRepository.exists(empresa.getId());
        assertThat(empresaExistsInEs).isFalse();

        // Validate the database is empty
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        empresaSearchRepository.save(empresa);

        // Search the empresa
        restEmpresaMockMvc.perform(get("/api/_search/empresas?query=id:" + empresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].dataUltimaAtualizacao").value(hasItem(DEFAULT_DATA_ULTIMA_ATUALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].quantidadeAcoes").value(hasItem(DEFAULT_QUANTIDADE_ACOES.intValue())))
            .andExpect(jsonPath("$.[*].valorInicial").value(hasItem(DEFAULT_VALOR_INICIAL.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empresa.class);
        Empresa empresa1 = new Empresa();
        empresa1.setId(1L);
        Empresa empresa2 = new Empresa();
        empresa2.setId(empresa1.getId());
        assertThat(empresa1).isEqualTo(empresa2);
        empresa2.setId(2L);
        assertThat(empresa1).isNotEqualTo(empresa2);
        empresa1.setId(null);
        assertThat(empresa1).isNotEqualTo(empresa2);
    }
}
