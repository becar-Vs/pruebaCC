package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SevGraficaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SevGrafica;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.SevGraficaRepository;
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
 * Integration tests for the {@link SevGraficaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SevGraficaResourceIT {

    private static final Integer DEFAULT_ID_GRAFICA = 1;
    private static final Integer UPDATED_ID_GRAFICA = 2;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FK_ID_RESPONSABLE = 1;
    private static final Integer UPDATED_FK_ID_RESPONSABLE = 2;

    private static final String ENTITY_API_URL = "/api/sev-graficas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SevGraficaRepository sevGraficaRepository;

    @Mock
    private SevGraficaRepository sevGraficaRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SevGrafica sevGrafica;

    private SevGrafica insertedSevGrafica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SevGrafica createEntity(EntityManager em) {
        SevGrafica sevGrafica = new SevGrafica()
            .idGrafica(DEFAULT_ID_GRAFICA)
            .nombre(DEFAULT_NOMBRE)
            .fkIdResponsable(DEFAULT_FK_ID_RESPONSABLE);
        return sevGrafica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SevGrafica createUpdatedEntity(EntityManager em) {
        SevGrafica sevGrafica = new SevGrafica()
            .idGrafica(UPDATED_ID_GRAFICA)
            .nombre(UPDATED_NOMBRE)
            .fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);
        return sevGrafica;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SevGrafica.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        sevGrafica = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSevGrafica != null) {
            sevGraficaRepository.delete(insertedSevGrafica).block();
            insertedSevGrafica = null;
        }
        deleteEntities(em);
    }

    @Test
    void createSevGrafica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SevGrafica
        var returnedSevGrafica = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SevGrafica.class)
            .returnResult()
            .getResponseBody();

        // Validate the SevGrafica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSevGraficaUpdatableFieldsEquals(returnedSevGrafica, getPersistedSevGrafica(returnedSevGrafica));

        insertedSevGrafica = returnedSevGrafica;
    }

    @Test
    void createSevGraficaWithExistingId() throws Exception {
        // Create the SevGrafica with an existing ID
        sevGrafica.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGrafica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdGraficaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevGrafica.setIdGrafica(null);

        // Create the SevGrafica, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevGrafica.setNombre(null);

        // Create the SevGrafica, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFkIdResponsableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevGrafica.setFkIdResponsable(null);

        // Create the SevGrafica, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSevGraficasAsStream() {
        // Initialize the database
        sevGraficaRepository.save(sevGrafica).block();

        List<SevGrafica> sevGraficaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SevGrafica.class)
            .getResponseBody()
            .filter(sevGrafica::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sevGraficaList).isNotNull();
        assertThat(sevGraficaList).hasSize(1);
        SevGrafica testSevGrafica = sevGraficaList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertSevGraficaAllPropertiesEquals(sevGrafica, testSevGrafica);
        assertSevGraficaUpdatableFieldsEquals(sevGrafica, testSevGrafica);
    }

    @Test
    void getAllSevGraficas() {
        // Initialize the database
        insertedSevGrafica = sevGraficaRepository.save(sevGrafica).block();

        // Get all the sevGraficaList
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
            .value(hasItem(sevGrafica.getId().intValue()))
            .jsonPath("$.[*].idGrafica")
            .value(hasItem(DEFAULT_ID_GRAFICA))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].fkIdResponsable")
            .value(hasItem(DEFAULT_FK_ID_RESPONSABLE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSevGraficasWithEagerRelationshipsIsEnabled() {
        when(sevGraficaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(sevGraficaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSevGraficasWithEagerRelationshipsIsNotEnabled() {
        when(sevGraficaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(sevGraficaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSevGrafica() {
        // Initialize the database
        insertedSevGrafica = sevGraficaRepository.save(sevGrafica).block();

        // Get the sevGrafica
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sevGrafica.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sevGrafica.getId().intValue()))
            .jsonPath("$.idGrafica")
            .value(is(DEFAULT_ID_GRAFICA))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.fkIdResponsable")
            .value(is(DEFAULT_FK_ID_RESPONSABLE));
    }

    @Test
    void getNonExistingSevGrafica() {
        // Get the sevGrafica
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSevGrafica() throws Exception {
        // Initialize the database
        insertedSevGrafica = sevGraficaRepository.save(sevGrafica).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevGrafica
        SevGrafica updatedSevGrafica = sevGraficaRepository.findById(sevGrafica.getId()).block();
        updatedSevGrafica.idGrafica(UPDATED_ID_GRAFICA).nombre(UPDATED_NOMBRE).fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSevGrafica.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedSevGrafica))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevGrafica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSevGraficaToMatchAllProperties(updatedSevGrafica);
    }

    @Test
    void putNonExistingSevGrafica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGrafica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sevGrafica.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGrafica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSevGrafica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGrafica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGrafica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSevGrafica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGrafica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SevGrafica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSevGraficaWithPatch() throws Exception {
        // Initialize the database
        insertedSevGrafica = sevGraficaRepository.save(sevGrafica).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevGrafica using partial update
        SevGrafica partialUpdatedSevGrafica = new SevGrafica();
        partialUpdatedSevGrafica.setId(sevGrafica.getId());

        partialUpdatedSevGrafica.idGrafica(UPDATED_ID_GRAFICA).nombre(UPDATED_NOMBRE).fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSevGrafica.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSevGrafica))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevGrafica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSevGraficaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSevGrafica, sevGrafica),
            getPersistedSevGrafica(sevGrafica)
        );
    }

    @Test
    void fullUpdateSevGraficaWithPatch() throws Exception {
        // Initialize the database
        insertedSevGrafica = sevGraficaRepository.save(sevGrafica).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevGrafica using partial update
        SevGrafica partialUpdatedSevGrafica = new SevGrafica();
        partialUpdatedSevGrafica.setId(sevGrafica.getId());

        partialUpdatedSevGrafica.idGrafica(UPDATED_ID_GRAFICA).nombre(UPDATED_NOMBRE).fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSevGrafica.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSevGrafica))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevGrafica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSevGraficaUpdatableFieldsEquals(partialUpdatedSevGrafica, getPersistedSevGrafica(partialUpdatedSevGrafica));
    }

    @Test
    void patchNonExistingSevGrafica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGrafica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sevGrafica.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGrafica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSevGrafica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGrafica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevGrafica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSevGrafica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevGrafica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevGrafica))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SevGrafica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSevGrafica() {
        // Initialize the database
        insertedSevGrafica = sevGraficaRepository.save(sevGrafica).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sevGrafica
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sevGrafica.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sevGraficaRepository.count().block();
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

    protected SevGrafica getPersistedSevGrafica(SevGrafica sevGrafica) {
        return sevGraficaRepository.findById(sevGrafica.getId()).block();
    }

    protected void assertPersistedSevGraficaToMatchAllProperties(SevGrafica expectedSevGrafica) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSevGraficaAllPropertiesEquals(expectedSevGrafica, getPersistedSevGrafica(expectedSevGrafica));
        assertSevGraficaUpdatableFieldsEquals(expectedSevGrafica, getPersistedSevGrafica(expectedSevGrafica));
    }

    protected void assertPersistedSevGraficaToMatchUpdatableProperties(SevGrafica expectedSevGrafica) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSevGraficaAllUpdatablePropertiesEquals(expectedSevGrafica, getPersistedSevGrafica(expectedSevGrafica));
        assertSevGraficaUpdatableFieldsEquals(expectedSevGrafica, getPersistedSevGrafica(expectedSevGrafica));
    }
}
