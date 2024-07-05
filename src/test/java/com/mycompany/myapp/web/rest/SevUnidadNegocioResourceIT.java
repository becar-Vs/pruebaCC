package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SevUnidadNegocioAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SevUnidadNegocio;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.SevUnidadNegocioRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SevUnidadNegocioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SevUnidadNegocioResourceIT {

    private static final Integer DEFAULT_ID_UNIDAD_NEGOCIO = 1;
    private static final Integer UPDATED_ID_UNIDAD_NEGOCIO = 2;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FK_ID_RESPONSABLE = 1;
    private static final Integer UPDATED_FK_ID_RESPONSABLE = 2;

    private static final String ENTITY_API_URL = "/api/sev-unidad-negocios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SevUnidadNegocioRepository sevUnidadNegocioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SevUnidadNegocio sevUnidadNegocio;

    private SevUnidadNegocio insertedSevUnidadNegocio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SevUnidadNegocio createEntity(EntityManager em) {
        SevUnidadNegocio sevUnidadNegocio = new SevUnidadNegocio()
            .idUnidadNegocio(DEFAULT_ID_UNIDAD_NEGOCIO)
            .nombre(DEFAULT_NOMBRE)
            .fkIdResponsable(DEFAULT_FK_ID_RESPONSABLE);
        return sevUnidadNegocio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SevUnidadNegocio createUpdatedEntity(EntityManager em) {
        SevUnidadNegocio sevUnidadNegocio = new SevUnidadNegocio()
            .idUnidadNegocio(UPDATED_ID_UNIDAD_NEGOCIO)
            .nombre(UPDATED_NOMBRE)
            .fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);
        return sevUnidadNegocio;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SevUnidadNegocio.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        sevUnidadNegocio = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSevUnidadNegocio != null) {
            sevUnidadNegocioRepository.delete(insertedSevUnidadNegocio).block();
            insertedSevUnidadNegocio = null;
        }
        deleteEntities(em);
    }

    @Test
    void createSevUnidadNegocio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SevUnidadNegocio
        var returnedSevUnidadNegocio = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SevUnidadNegocio.class)
            .returnResult()
            .getResponseBody();

        // Validate the SevUnidadNegocio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSevUnidadNegocioUpdatableFieldsEquals(returnedSevUnidadNegocio, getPersistedSevUnidadNegocio(returnedSevUnidadNegocio));

        insertedSevUnidadNegocio = returnedSevUnidadNegocio;
    }

    @Test
    void createSevUnidadNegocioWithExistingId() throws Exception {
        // Create the SevUnidadNegocio with an existing ID
        sevUnidadNegocio.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevUnidadNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdUnidadNegocioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevUnidadNegocio.setIdUnidadNegocio(null);

        // Create the SevUnidadNegocio, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevUnidadNegocio.setNombre(null);

        // Create the SevUnidadNegocio, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFkIdResponsableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sevUnidadNegocio.setFkIdResponsable(null);

        // Create the SevUnidadNegocio, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSevUnidadNegociosAsStream() {
        // Initialize the database
        sevUnidadNegocioRepository.save(sevUnidadNegocio).block();

        List<SevUnidadNegocio> sevUnidadNegocioList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SevUnidadNegocio.class)
            .getResponseBody()
            .filter(sevUnidadNegocio::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sevUnidadNegocioList).isNotNull();
        assertThat(sevUnidadNegocioList).hasSize(1);
        SevUnidadNegocio testSevUnidadNegocio = sevUnidadNegocioList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertSevUnidadNegocioAllPropertiesEquals(sevUnidadNegocio, testSevUnidadNegocio);
        assertSevUnidadNegocioUpdatableFieldsEquals(sevUnidadNegocio, testSevUnidadNegocio);
    }

    @Test
    void getAllSevUnidadNegocios() {
        // Initialize the database
        insertedSevUnidadNegocio = sevUnidadNegocioRepository.save(sevUnidadNegocio).block();

        // Get all the sevUnidadNegocioList
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
            .value(hasItem(sevUnidadNegocio.getId().intValue()))
            .jsonPath("$.[*].idUnidadNegocio")
            .value(hasItem(DEFAULT_ID_UNIDAD_NEGOCIO))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].fkIdResponsable")
            .value(hasItem(DEFAULT_FK_ID_RESPONSABLE));
    }

    @Test
    void getSevUnidadNegocio() {
        // Initialize the database
        insertedSevUnidadNegocio = sevUnidadNegocioRepository.save(sevUnidadNegocio).block();

        // Get the sevUnidadNegocio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sevUnidadNegocio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sevUnidadNegocio.getId().intValue()))
            .jsonPath("$.idUnidadNegocio")
            .value(is(DEFAULT_ID_UNIDAD_NEGOCIO))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.fkIdResponsable")
            .value(is(DEFAULT_FK_ID_RESPONSABLE));
    }

    @Test
    void getNonExistingSevUnidadNegocio() {
        // Get the sevUnidadNegocio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSevUnidadNegocio() throws Exception {
        // Initialize the database
        insertedSevUnidadNegocio = sevUnidadNegocioRepository.save(sevUnidadNegocio).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevUnidadNegocio
        SevUnidadNegocio updatedSevUnidadNegocio = sevUnidadNegocioRepository.findById(sevUnidadNegocio.getId()).block();
        updatedSevUnidadNegocio
            .idUnidadNegocio(UPDATED_ID_UNIDAD_NEGOCIO)
            .nombre(UPDATED_NOMBRE)
            .fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSevUnidadNegocio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedSevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevUnidadNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSevUnidadNegocioToMatchAllProperties(updatedSevUnidadNegocio);
    }

    @Test
    void putNonExistingSevUnidadNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevUnidadNegocio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sevUnidadNegocio.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevUnidadNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSevUnidadNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevUnidadNegocio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevUnidadNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSevUnidadNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevUnidadNegocio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SevUnidadNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSevUnidadNegocioWithPatch() throws Exception {
        // Initialize the database
        insertedSevUnidadNegocio = sevUnidadNegocioRepository.save(sevUnidadNegocio).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevUnidadNegocio using partial update
        SevUnidadNegocio partialUpdatedSevUnidadNegocio = new SevUnidadNegocio();
        partialUpdatedSevUnidadNegocio.setId(sevUnidadNegocio.getId());

        partialUpdatedSevUnidadNegocio.idUnidadNegocio(UPDATED_ID_UNIDAD_NEGOCIO).fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSevUnidadNegocio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevUnidadNegocio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSevUnidadNegocioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSevUnidadNegocio, sevUnidadNegocio),
            getPersistedSevUnidadNegocio(sevUnidadNegocio)
        );
    }

    @Test
    void fullUpdateSevUnidadNegocioWithPatch() throws Exception {
        // Initialize the database
        insertedSevUnidadNegocio = sevUnidadNegocioRepository.save(sevUnidadNegocio).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sevUnidadNegocio using partial update
        SevUnidadNegocio partialUpdatedSevUnidadNegocio = new SevUnidadNegocio();
        partialUpdatedSevUnidadNegocio.setId(sevUnidadNegocio.getId());

        partialUpdatedSevUnidadNegocio
            .idUnidadNegocio(UPDATED_ID_UNIDAD_NEGOCIO)
            .nombre(UPDATED_NOMBRE)
            .fkIdResponsable(UPDATED_FK_ID_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSevUnidadNegocio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SevUnidadNegocio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSevUnidadNegocioUpdatableFieldsEquals(
            partialUpdatedSevUnidadNegocio,
            getPersistedSevUnidadNegocio(partialUpdatedSevUnidadNegocio)
        );
    }

    @Test
    void patchNonExistingSevUnidadNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevUnidadNegocio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sevUnidadNegocio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevUnidadNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSevUnidadNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevUnidadNegocio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SevUnidadNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSevUnidadNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sevUnidadNegocio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sevUnidadNegocio))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SevUnidadNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSevUnidadNegocio() {
        // Initialize the database
        insertedSevUnidadNegocio = sevUnidadNegocioRepository.save(sevUnidadNegocio).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sevUnidadNegocio
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sevUnidadNegocio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sevUnidadNegocioRepository.count().block();
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

    protected SevUnidadNegocio getPersistedSevUnidadNegocio(SevUnidadNegocio sevUnidadNegocio) {
        return sevUnidadNegocioRepository.findById(sevUnidadNegocio.getId()).block();
    }

    protected void assertPersistedSevUnidadNegocioToMatchAllProperties(SevUnidadNegocio expectedSevUnidadNegocio) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSevUnidadNegocioAllPropertiesEquals(expectedSevUnidadNegocio, getPersistedSevUnidadNegocio(expectedSevUnidadNegocio));
        assertSevUnidadNegocioUpdatableFieldsEquals(expectedSevUnidadNegocio, getPersistedSevUnidadNegocio(expectedSevUnidadNegocio));
    }

    protected void assertPersistedSevUnidadNegocioToMatchUpdatableProperties(SevUnidadNegocio expectedSevUnidadNegocio) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSevUnidadNegocioAllUpdatablePropertiesEquals(expectedSevUnidadNegocio, getPersistedSevUnidadNegocio(expectedSevUnidadNegocio));
        assertSevUnidadNegocioUpdatableFieldsEquals(expectedSevUnidadNegocio, getPersistedSevUnidadNegocio(expectedSevUnidadNegocio));
    }
}
