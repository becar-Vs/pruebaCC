package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SevGraficaDataAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SevGraficaData;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.SevGraficaDataRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link SevGraficaDataResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SevGraficaDataResourceIT {

    private static final Integer DEFAULT_ID_ROW = 1;
    private static final Integer UPDATED_ID_ROW = 2;

    private static final LocalDate DEFAULT_FECHA_OBJETIVO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_OBJETIVO = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_VALOR_OBJETIVO = 1D;
    private static final Double UPDATED_VALOR_OBJETIVO = 2D;

    private static final Double DEFAULT_VALOR_LOGRADO = 1D;
    private static final Double UPDATED_VALOR_LOGRADO = 2D;

    private static final String ENTITY_API_URL = "/api/sev-grafica-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SevGraficaDataRepository sevGraficaDataRepository;

    @Mock
    private SevGraficaDataRepository sevGraficaDataRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SevGraficaData sevGraficaData;

    private SevGraficaData insertedSevGraficaData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SevGraficaData createEntity(EntityManager em) {
        SevGraficaData sevGraficaData = new SevGraficaData()
            .idRow(DEFAULT_ID_ROW)
            .fechaObjetivo(DEFAULT_FECHA_OBJETIVO)
            .valorObjetivo(DEFAULT_VALOR_OBJETIVO)
            .valorLogrado(DEFAULT_VALOR_LOGRADO);
        return sevGraficaData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SevGraficaData createUpdatedEntity(EntityManager em) {
        SevGraficaData sevGraficaData = new SevGraficaData()
            .idRow(UPDATED_ID_ROW)
            .fechaObjetivo(UPDATED_FECHA_OBJETIVO)
            .valorObjetivo(UPDATED_VALOR_OBJETIVO)
            .valorLogrado(UPDATED_VALOR_LOGRADO);
        return sevGraficaData;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SevGraficaData.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        sevGraficaData = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSevGraficaData != null) {
            sevGraficaDataRepository.delete(insertedSevGraficaData).block();
            insertedSevGraficaData = null;
        }
        deleteEntities(em);
    }

    @Test
    void createSevGraficaData() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SevGraficaData
        var returnedSevGraficaData = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SevGraficaData.class)
            .returnResult()
            .getResponseBody();

        // Validate the SevGraficaData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSevGraficaDataUpdatableFieldsEquals(returnedSevGraficaData, getPersistedSevGraficaData(returnedSevGraficaData));

        insertedSevGraficaData = returnedSevGraficaData;
    }

    @Test
    void createSevGraficaDataWithExistingId() throws Exception {
        // Create the SevGraficaData with an existing ID
        sevGraficaData.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGraficaData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdRowIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevGraficaData.setIdRow(null);

        // Create the SevGraficaData, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaObjetivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevGraficaData.setFechaObjetivo(null);

        // Create the SevGraficaData, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkValorObjetivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevGraficaData.setValorObjetivo(null);

        // Create the SevGraficaData, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkValorLogradoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevGraficaData.setValorLogrado(null);

        // Create the SevGraficaData, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSevGraficaDataAsStream() {
        // Initialize the database
        sevGraficaDataRepository.save(sevGraficaData).block();

        List<SevGraficaData> sevGraficaDataList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SevGraficaData.class)
            .getResponseBody()
            .filter(sevGraficaData::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sevGraficaDataList).isNotNull();
        assertThat(sevGraficaDataList).hasSize(1);
        SevGraficaData testSevGraficaData = sevGraficaDataList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertSevGraficaDataAllPropertiesEquals(sevGraficaData, testSevGraficaData);
        assertSevGraficaDataUpdatableFieldsEquals(sevGraficaData, testSevGraficaData);
    }

    @Test
    void getAllSevGraficaData() {
        // Initialize the database
        insertedSevGraficaData = sevGraficaDataRepository.save(sevGraficaData).block();

        // Get all the sevGraficaDataList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(sevGraficaData.getId().intValue()))
            .jsonPath("$.[*].idRow")
            .value(hasItem(DEFAULT_ID_ROW))
            .jsonPath("$.[*].fechaObjetivo")
            .value(hasItem(DEFAULT_FECHA_OBJETIVO.toString()))
            .jsonPath("$.[*].valorObjetivo")
            .value(hasItem(DEFAULT_VALOR_OBJETIVO.doubleValue()))
            .jsonPath("$.[*].valorLogrado")
            .value(hasItem(DEFAULT_VALOR_LOGRADO.doubleValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSevGraficaDataWithEagerRelationshipsIsEnabled() {
        when(sevGraficaDataRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(sevGraficaDataRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSevGraficaDataWithEagerRelationshipsIsNotEnabled() {
        when(sevGraficaDataRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(sevGraficaDataRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSevGraficaData() {
        // Initialize the database
        insertedSevGraficaData = sevGraficaDataRepository.save(sevGraficaData).block();

        // Get the sevGraficaData
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sevGraficaData.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sevGraficaData.getId().intValue()))
            .jsonPath("$.idRow")
            .value(is(DEFAULT_ID_ROW))
            .jsonPath("$.fechaObjetivo")
            .value(is(DEFAULT_FECHA_OBJETIVO.toString()))
            .jsonPath("$.valorObjetivo")
            .value(is(DEFAULT_VALOR_OBJETIVO.doubleValue()))
            .jsonPath("$.valorLogrado")
            .value(is(DEFAULT_VALOR_LOGRADO.doubleValue()));
    }

    @Test
    void getNonExistingSevGraficaData() {
        // Get the sevGraficaData
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSevGraficaData() throws Exception {
        // Initialize the database
        insertedSevGraficaData = sevGraficaDataRepository.save(sevGraficaData).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevGraficaData
        SevGraficaData updatedSevGraficaData = sevGraficaDataRepository.findById(sevGraficaData.getId()).block();
        updatedSevGraficaData
            .idRow(UPDATED_ID_ROW)
            .fechaObjetivo(UPDATED_FECHA_OBJETIVO)
            .valorObjetivo(UPDATED_VALOR_OBJETIVO)
            .valorLogrado(UPDATED_VALOR_LOGRADO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSevGraficaData.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedSevGraficaData))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevGraficaData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSevGraficaDataToMatchAllProperties(updatedSevGraficaData);
    }

    @Test
    void putNonExistingSevGraficaData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGraficaData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sevGraficaData.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGraficaData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSevGraficaData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGraficaData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGraficaData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSevGraficaData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGraficaData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SevGraficaData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSevGraficaDataWithPatch() throws Exception {
        // Initialize the database
        insertedSevGraficaData = sevGraficaDataRepository.save(sevGraficaData).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevGraficaData using partial update
        SevGraficaData partialUpdatedSevGraficaData = new SevGraficaData();
        partialUpdatedSevGraficaData.setId(sevGraficaData.getId());

        partialUpdatedSevGraficaData.idRow(UPDATED_ID_ROW).valorLogrado(UPDATED_VALOR_LOGRADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSevGraficaData.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSevGraficaData))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevGraficaData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSevGraficaDataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSevGraficaData, sevGraficaData),
            getPersistedSevGraficaData(sevGraficaData)
        );
    }

    @Test
    void fullUpdateSevGraficaDataWithPatch() throws Exception {
        // Initialize the database
        insertedSevGraficaData = sevGraficaDataRepository.save(sevGraficaData).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevGraficaData using partial update
        SevGraficaData partialUpdatedSevGraficaData = new SevGraficaData();
        partialUpdatedSevGraficaData.setId(sevGraficaData.getId());

        partialUpdatedSevGraficaData
            .idRow(UPDATED_ID_ROW)
            .fechaObjetivo(UPDATED_FECHA_OBJETIVO)
            .valorObjetivo(UPDATED_VALOR_OBJETIVO)
            .valorLogrado(UPDATED_VALOR_LOGRADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSevGraficaData.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSevGraficaData))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevGraficaData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSevGraficaDataUpdatableFieldsEquals(partialUpdatedSevGraficaData, getPersistedSevGraficaData(partialUpdatedSevGraficaData));
    }

    @Test
    void patchNonExistingSevGraficaData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGraficaData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sevGraficaData.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGraficaData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSevGraficaData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGraficaData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGraficaData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSevGraficaData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGraficaData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevGraficaData))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SevGraficaData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSevGraficaData() {
        // Initialize the database
        insertedSevGraficaData = sevGraficaDataRepository.save(sevGraficaData).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sevGraficaData
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sevGraficaData.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sevGraficaDataRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SevGraficaData getPersistedSevGraficaData(SevGraficaData sevGraficaData) {
        return sevGraficaDataRepository.findById(sevGraficaData.getId()).block();
    }

    protected void assertPersistedSevGraficaDataToMatchAllProperties(SevGraficaData expectedSevGraficaData) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSevGraficaDataAllPropertiesEquals(expectedSevGraficaData, getPersistedSevGraficaData(expectedSevGraficaData));
        assertSevGraficaDataUpdatableFieldsEquals(expectedSevGraficaData, getPersistedSevGraficaData(expectedSevGraficaData));
    }

    protected void assertPersistedSevGraficaDataToMatchUpdatableProperties(SevGraficaData expectedSevGraficaData) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSevGraficaDataAllUpdatablePropertiesEquals(expectedSevGraficaData, getPersistedSevGraficaData(expectedSevGraficaData));
        assertSevGraficaDataUpdatableFieldsEquals(expectedSevGraficaData, getPersistedSevGraficaData(expectedSevGraficaData));
    }
}
