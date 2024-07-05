package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SevGrafica;
import com.mycompany.myapp.repository.SevGraficaRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SevGrafica}.
 */
@RestController
@RequestMapping("/api/sev-graficas")
@Transactional
public class SevGraficaResource {

    private static final Logger log = LoggerFactory.getLogger(SevGraficaResource.class);

    private static final String ENTITY_NAME = "sevGrafica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SevGraficaRepository sevGraficaRepository;

    public SevGraficaResource(SevGraficaRepository sevGraficaRepository) {
        this.sevGraficaRepository = sevGraficaRepository;
    }

    /**
     * {@code POST  /sev-graficas} : Create a new sevGrafica.
     *
     * @param sevGrafica the sevGrafica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sevGrafica, or with status {@code 400 (Bad Request)} if the sevGrafica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SevGrafica>> createSevGrafica(@Valid @RequestBody SevGrafica sevGrafica) throws URISyntaxException {
        log.debug("REST request to save SevGrafica : {}", sevGrafica);
        if (sevGrafica.getId() != null) {
            throw new BadRequestAlertException("A new sevGrafica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sevGraficaRepository
            .save(sevGrafica)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/sev-graficas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sev-graficas/:id} : Updates an existing sevGrafica.
     *
     * @param id the id of the sevGrafica to save.
     * @param sevGrafica the sevGrafica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevGrafica,
     * or with status {@code 400 (Bad Request)} if the sevGrafica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sevGrafica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SevGrafica>> updateSevGrafica(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SevGrafica sevGrafica
    ) throws URISyntaxException {
        log.debug("REST request to update SevGrafica : {}, {}", id, sevGrafica);
        if (sevGrafica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sevGrafica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sevGraficaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sevGraficaRepository
                    .save(sevGrafica)
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
     * {@code PATCH  /sev-graficas/:id} : Partial updates given fields of an existing sevGrafica, field will ignore if it is null
     *
     * @param id the id of the sevGrafica to save.
     * @param sevGrafica the sevGrafica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevGrafica,
     * or with status {@code 400 (Bad Request)} if the sevGrafica is not valid,
     * or with status {@code 404 (Not Found)} if the sevGrafica is not found,
     * or with status {@code 500 (Internal Server Error)} if the sevGrafica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SevGrafica>> partialUpdateSevGrafica(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SevGrafica sevGrafica
    ) throws URISyntaxException {
        log.debug("REST request to partial update SevGrafica partially : {}, {}", id, sevGrafica);
        if (sevGrafica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sevGrafica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sevGraficaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SevGrafica> result = sevGraficaRepository
                    .findById(sevGrafica.getId())
                    .map(existingSevGrafica -> {
                        if (sevGrafica.getIdGrafica() != null) {
                            existingSevGrafica.setIdGrafica(sevGrafica.getIdGrafica());
                        }
                        if (sevGrafica.getNombre() != null) {
                            existingSevGrafica.setNombre(sevGrafica.getNombre());
                        }
                        if (sevGrafica.getFkIdResponsable() != null) {
                            existingSevGrafica.setFkIdResponsable(sevGrafica.getFkIdResponsable());
                        }

                        return existingSevGrafica;
                    })
                    .flatMap(sevGraficaRepository::save);

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
     * {@code GET  /sev-graficas} : get all the sevGraficas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sevGraficas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<SevGrafica>> getAllSevGraficas(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all SevGraficas");
        if (eagerload) {
            return sevGraficaRepository.findAllWithEagerRelationships().collectList();
        } else {
            return sevGraficaRepository.findAll().collectList();
        }
    }

    /**
     * {@code GET  /sev-graficas} : get all the sevGraficas as a stream.
     * @return the {@link Flux} of sevGraficas.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SevGrafica> getAllSevGraficasAsStream() {
        log.debug("REST request to get all SevGraficas as a stream");
        return sevGraficaRepository.findAll();
    }

    /**
     * {@code GET  /sev-graficas/:id} : get the "id" sevGrafica.
     *
     * @param id the id of the sevGrafica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sevGrafica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SevGrafica>> getSevGrafica(@PathVariable("id") Long id) {
        log.debug("REST request to get SevGrafica : {}", id);
        Mono<SevGrafica> sevGrafica = sevGraficaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(sevGrafica);
    }

    /**
     * {@code DELETE  /sev-graficas/:id} : delete the "id" sevGrafica.
     *
     * @param id the id of the sevGrafica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSevGrafica(@PathVariable("id") Long id) {
        log.debug("REST request to delete SevGrafica : {}", id);
        return sevGraficaRepository
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
