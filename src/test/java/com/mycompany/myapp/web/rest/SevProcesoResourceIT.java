package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SevProcesoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SevProceso;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.SevProcesoRepository;
import java.time.Duration;
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
 * Integration tests for the {@link SevProcesoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SevProcesoResourceIT {

    private static final Integer DEFAULT_ID_PROCESO = 1;
    private static final Integer UPDATED_ID_PROCESO = 2;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FK_ID_RESPONSABLE = 1;
    private static final Integer UPDATED_FK_ID_RESPONSABLE = 2;

    private static final String ENTITY_API_URL = "/api/sev-procesos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SevProcesoRepository sevProcesoRepository;

    @Mock
    private SevProcesoRepository sevProcesoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SevProceso sevProceso;

    private SevProceso insertedSevProceso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SevProceso createEntity(EntityManager em) {
        SevProceso sevProceso = new SevProceso()
            .idProceso(DEFAULT_ID_PROCESO)
            .nombre(DEFAULT_NOMBRE)
            .fkIdResponsable(DEFAULT_FK_ID_RESPONSABLE);
        return sevProceso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SevProceso createUpdatedEntity(EntityManager em) {
        SevProceso sevProceso = new SevProceso()
            .idProceso(UPDATED_ID_PROCESO)
            .nombre(UPDATED_NOMBRE)
            .fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);
        return sevProceso;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SevProceso.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        sevProceso = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSevProceso != null) {
            sevProcesoRepository.delete(insertedSevProceso).block();
            insertedSevProceso = null;
        }
        deleteEntities(em);
    }

    @Test
    void createSevProceso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SevProceso
        var returnedSevProceso = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SevProceso.class)
            .returnResult()
            .getResponseBody();

        // Validate the SevProceso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSevProcesoUpdatableFieldsEquals(returnedSevProceso, getPersistedSevProceso(returnedSevProceso));

        insertedSevProceso = returnedSevProceso;
    }

    @Test
    void createSevProcesoWithExistingId() throws Exception {
        // Create the SevProceso with an existing ID
        sevProceso.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevProceso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdProcesoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevProceso.setIdProceso(null);

        // Create the SevProceso, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevProceso.setNombre(null);

        // Create the SevProceso, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFkIdResponsableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevProceso.setFkIdResponsable(null);

        // Create the SevProceso, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSevProcesosAsStream() {
        // Initialize the database
        sevProcesoRepository.save(sevProceso).block();

        List<SevProceso> sevProcesoList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SevProceso.class)
            .getResponseBody()
            .filter(sevProceso::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sevProcesoList).isNotNull();
        assertThat(sevProcesoList).hasSize(1);
        SevProceso testSevProceso = sevProcesoList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertSevProcesoAllPropertiesEquals(sevProceso, testSevProceso);
        assertSevProcesoUpdatableFieldsEquals(sevProceso, testSevProceso);
    }

    @Test
    void getAllSevProcesos() {
        // Initialize the database
        insertedSevProceso = sevProcesoRepository.save(sevProceso).block();

        // Get all the sevProcesoList
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
            .value(hasItem(sevProceso.getId().intValue()))
            .jsonPath("$.[*].idProceso")
            .value(hasItem(DEFAULT_ID_PROCESO))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].fkIdResponsable")
            .value(hasItem(DEFAULT_FK_ID_RESPONSABLE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSevProcesosWithEagerRelationshipsIsEnabled() {
        when(sevProcesoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(sevProcesoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSevProcesosWithEagerRelationshipsIsNotEnabled() {
        when(sevProcesoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(sevProcesoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSevProceso() {
        // Initialize the database
        insertedSevProceso = sevProcesoRepository.save(sevProceso).block();

        // Get the sevProceso
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sevProceso.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sevProceso.getId().intValue()))
            .jsonPath("$.idProceso")
            .value(is(DEFAULT_ID_PROCESO))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.fkIdResponsable")
            .value(is(DEFAULT_FK_ID_RESPONSABLE));
    }

    @Test
    void getNonExistingSevProceso() {
        // Get the sevProceso
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSevProceso() throws Exception {
        // Initialize the database
        insertedSevProceso = sevProcesoRepository.save(sevProceso).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevProceso
        SevProceso updatedSevProceso = sevProcesoRepository.findById(sevProceso.getId()).block();
        updatedSevProceso.idProceso(UPDATED_ID_PROCESO).nombre(UPDATED_NOMBRE).fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSevProceso.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedSevProceso))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevProceso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSevProcesoToMatchAllProperties(updatedSevProceso);
    }

    @Test
    void putNonExistingSevProceso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevProceso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sevProceso.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevProceso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSevProceso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevProceso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevProceso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSevProceso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevProceso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SevProceso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSevProcesoWithPatch() throws Exception {
        // Initialize the database
        insertedSevProceso = sevProcesoRepository.save(sevProceso).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevProceso using partial update
        SevProceso partialUpdatedSevProceso = new SevProceso();
        partialUpdatedSevProceso.setId(sevProceso.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSevProceso.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSevProceso))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevProceso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSevProcesoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSevProceso, sevProceso),
            getPersistedSevProceso(sevProceso)
        );
    }

    @Test
    void fullUpdateSevProcesoWithPatch() throws Exception {
        // Initialize the database
        insertedSevProceso = sevProcesoRepository.save(sevProceso).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevProceso using partial update
        SevProceso partialUpdatedSevProceso = new SevProceso();
        partialUpdatedSevProceso.setId(sevProceso.getId());

        partialUpdatedSevProceso.idProceso(UPDATED_ID_PROCESO).nombre(UPDATED_NOMBRE).fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSevProceso.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSevProceso))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevProceso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSevProcesoUpdatableFieldsEquals(partialUpdatedSevProceso, getPersistedSevProceso(partialUpdatedSevProceso));
    }

    @Test
    void patchNonExistingSevProceso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevProceso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sevProceso.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevProceso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSevProceso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevProceso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevProceso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSevProceso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevProceso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevProceso))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SevProceso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSevProceso() {
        // Initialize the database
        insertedSevProceso = sevProcesoRepository.save(sevProceso).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sevProceso
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sevProceso.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sevProcesoRepository.count().block();
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

    protected SevProceso getPersistedSevProceso(SevProceso sevProceso) {
        return sevProcesoRepository.findById(sevProceso.getId()).block();
    }

    protected void assertPersistedSevProcesoToMatchAllProperties(SevProceso expectedSevProceso) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSevProcesoAllPropertiesEquals(expectedSevProceso, getPersistedSevProceso(expectedSevProceso));
        assertSevProcesoUpdatableFieldsEquals(expectedSevProceso, getPersistedSevProceso(expectedSevProceso));
    }

    protected void assertPersistedSevProcesoToMatchUpdatableProperties(SevProceso expectedSevProceso) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSevProcesoAllUpdatablePropertiesEquals(expectedSevProceso, getPersistedSevProceso(expectedSevProceso));
        assertSevProcesoUpdatableFieldsEquals(expectedSevProceso, getPersistedSevProceso(expectedSevProceso));
    }
}
