package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SevProceso;
import com.mycompany.myapp.repository.SevProcesoRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SevProceso}.
 */
@RestController
@RequestMapping("/api/sev-procesos")
@Transactional
public class SevProcesoResource {

    private static final Logger log = LoggerFactory.getLogger(SevProcesoResource.class);

    private static final String ENTITY_NAME = "sevProceso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SevProcesoRepository sevProcesoRepository;

    public SevProcesoResource(SevProcesoRepository sevProcesoRepository) {
        this.sevProcesoRepository = sevProcesoRepository;
    }

    /**
     * {@code POST  /sev-procesos} : Create a new sevProceso.
     *
     * @param sevProceso the sevProceso to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sevProceso, or with status {@code 400 (Bad Request)} if the sevProceso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SevProceso>> createSevProceso(@Valid @RequestBody SevProceso sevProceso) throws URISyntaxException {
        log.debug("REST request to save SevProceso : {}", sevProceso);
        if (sevProceso.getId() != null) {
            throw new BadRequestAlertException("A new sevProceso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sevProcesoRepository
            .save(sevProceso)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/sev-procesos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sev-procesos/:id} : Updates an existing sevProceso.
     *
     * @param id the id of the sevProceso to save.
     * @param sevProceso the sevProceso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevProceso,
     * or with status {@code 400 (Bad Request)} if the sevProceso is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sevProceso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SevProceso>> updateSevProceso(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SevProceso sevProceso
    ) throws URISyntaxException {
        log.debug("REST request to update SevProceso : {}, {}", id, sevProceso);
        if (sevProceso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sevProceso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sevProcesoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sevProcesoRepository
                    .save(sevProceso)
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
     * {@code PATCH  /sev-procesos/:id} : Partial updates given fields of an existing sevProceso, field will ignore if it is null
     *
     * @param id the id of the sevProceso to save.
     * @param sevProceso the sevProceso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevProceso,
     * or with status {@code 400 (Bad Request)} if the sevProceso is not valid,
     * or with status {@code 404 (Not Found)} if the sevProceso is not found,
     * or with status {@code 500 (Internal Server Error)} if the sevProceso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SevProceso>> partialUpdateSevProceso(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SevProceso sevProceso
    ) throws URISyntaxException {
        log.debug("REST request to partial update SevProceso partially : {}, {}", id, sevProceso);
        if (sevProceso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sevProceso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sevProcesoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SevProceso> result = sevProcesoRepository
                    .findById(sevProceso.getId())
                    .map(existingSevProceso -> {
                        if (sevProceso.getIdProceso() != null) {
                            existingSevProceso.setIdProceso(sevProceso.getIdProceso());
                        }
                        if (sevProceso.getNombre() != null) {
                            existingSevProceso.setNombre(sevProceso.getNombre());
                        }
                        if (sevProceso.getFkIdResponsable() != null) {
                            existingSevProceso.setFkIdResponsable(sevProceso.getFkIdResponsable());
                        }

                        return existingSevProceso;
                    })
                    .flatMap(sevProcesoRepository::save);

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
     * {@code GET  /sev-procesos} : get all the sevProcesos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sevProcesos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<SevProceso>> getAllSevProcesos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all SevProcesos");
        if (eagerload) {
            return sevProcesoRepository.findAllWithEagerRelationships().collectList();
        } else {
            return sevProcesoRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /sev-procesos} : get all the sevProcesos as a stream.
     * @return the {@link Flux} of sevProcesos.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SevProceso> getAllSevProcesosAsStream() {
        log.debug("REST request to get all SevProcesos as a stream");
        return sevProcesoRepository.findAll();
    }

    /**
     * {@code GET  /sev-procesos/:id} : get the "id" sevProceso.
     *
     * @param id the id of the sevProceso to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sevProceso, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SevProceso>> getSevProceso(@PathVariable("id") Long id) {
        log.debug("REST request to get SevProceso : {}", id);
        Mono<SevProceso> sevProceso = sevProcesoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(sevProceso);
    }

    /**
     * {@code DELETE  /sev-procesos/:id} : delete the "id" sevProceso.
     *
     * @param id the id of the sevProceso to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSevProceso(@PathVariable("id") Long id) {
        log.debug("REST request to delete SevProceso : {}", id);
        return sevProcesoRepository
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
