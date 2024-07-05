package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SevGraficaData;
import com.mycompany.myapp.repository.SevGraficaDataRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SevGraficaData}.
 */
@RestController
@RequestMapping("/api/sev-grafica-data")
@Transactional
public class SevGraficaDataResource {

    private static final Logger log = LoggerFactory.getLogger(SevGraficaDataResource.class);

    private static final String ENTITY_NAME = "sevGraficaData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SevGraficaDataRepository sevGraficaDataRepository;

    public SevGraficaDataResource(SevGraficaDataRepository sevGraficaDataRepository) {
        this.sevGraficaDataRepository = sevGraficaDataRepository;
    }

    /**
     * {@code POST  /sev-grafica-data} : Create a new sevGraficaData.
     *
     * @param sevGraficaData the sevGraficaData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sevGraficaData, or with status {@code 400 (Bad Request)} if the sevGraficaData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SevGraficaData>> createSevGraficaData(@Valid @RequestBody SevGraficaData sevGraficaData)
        throws URISyntaxException {
        log.debug("REST request to save SevGraficaData : {}", sevGraficaData);
        if (sevGraficaData.getId() != null) {
            throw new BadRequestAlertException("A new sevGraficaData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sevGraficaDataRepository
            .save(sevGraficaData)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/sev-grafica-data/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sev-grafica-data/:id} : Updates an existing sevGraficaData.
     *
     * @param id the id of the sevGraficaData to save.
     * @param sevGraficaData the sevGraficaData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevGraficaData,
     * or with status {@code 400 (Bad Request)} if the sevGraficaData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sevGraficaData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SevGraficaData>> updateSevGraficaData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SevGraficaData sevGraficaData
    ) throws URISyntaxException {
        log.debug("REST request to update SevGraficaData : {}, {}", id, sevGraficaData);
        if (sevGraficaData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sevGraficaData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sevGraficaDataRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sevGraficaDataRepository
                    .save(sevGraficaData)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /sev-grafica-data/:id} : Partial updates given fields of an existing sevGraficaData, field will ignore if it is null
     *
     * @param id the id of the sevGraficaData to save.
     * @param sevGraficaData the sevGraficaData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevGraficaData,
     * or with status {@code 400 (Bad Request)} if the sevGraficaData is not valid,
     * or with status {@code 404 (Not Found)} if the sevGraficaData is not found,
     * or with status {@code 500 (Internal Server Error)} if the sevGraficaData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SevGraficaData>> partialUpdateSevGraficaData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SevGraficaData sevGraficaData
    ) throws URISyntaxException {
        log.debug("REST request to partial update SevGraficaData partially : {}, {}", id, sevGraficaData);
        if (sevGraficaData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sevGraficaData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sevGraficaDataRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SevGraficaData> result = sevGraficaDataRepository
                    .findById(sevGraficaData.getId())
                    .map(existingSevGraficaData -> {
                        if (sevGraficaData.getIdRow() != null) {
                            existingSevGraficaData.setIdRow(sevGraficaData.getIdRow());
                        }
                        if (sevGraficaData.getFechaObjetivo() != null) {
                            existingSevGraficaData.setFechaObjetivo(sevGraficaData.getFechaObjetivo());
                        }
                        if (sevGraficaData.getValorObjetivo() != null) {
                            existingSevGraficaData.setValorObjetivo(sevGraficaData.getValorObjetivo());
                        }
                        if (sevGraficaData.getValorLogrado() != null) {
                            existingSevGraficaData.setValorLogrado(sevGraficaData.getValorLogrado());
                        }

                        return existingSevGraficaData;
                    })
                    .flatMap(sevGraficaDataRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /sev-grafica-data} : get all the sevGraficaData.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sevGraficaData in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<SevGraficaData>> getAllSevGraficaData(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all SevGraficaData");
        if (eagerload) {
            return sevGraficaDataRepository.findAllWithEagerRelationships().collectList();
        } else {
            return sevGraficaDataRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /sev-grafica-data} : get all the sevGraficaData as a stream.
     * @return the {@link Flux} of sevGraficaData.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SevGraficaData> getAllSevGraficaDataAsStream() {
        log.debug("REST request to get all SevGraficaData as a stream");
        return sevGraficaDataRepository.findAll();
    }

    /**
     * {@code GET  /sev-grafica-data/:id} : get the "id" sevGraficaData.
     *
     * @param id the id of the sevGraficaData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sevGraficaData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SevGraficaData>> getSevGraficaData(@PathVariable("id") Long id) {
        log.debug("REST request to get SevGraficaData : {}", id);
        Mono<SevGraficaData> sevGraficaData = sevGraficaDataRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(sevGraficaData);
    }

    /**
     * {@code DELETE  /sev-grafica-data/:id} : delete the "id" sevGraficaData.
     *
     * @param id the id of the sevGraficaData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSevGraficaData(@PathVariable("id") Long id) {
        log.debug("REST request to delete SevGraficaData : {}", id);
        return sevGraficaDataRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
