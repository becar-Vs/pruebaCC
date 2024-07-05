package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SevUnidadNegocio;
import com.mycompany.myapp.repository.SevUnidadNegocioRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SevUnidadNegocio}.
 */
@RestController
@RequestMapping("/api/sev-unidad-negocios")
@Transactional
public class SevUnidadNegocioResource {

    private static final Logger log = LoggerFactory.getLogger(SevUnidadNegocioResource.class);

    private static final String ENTITY_NAME = "sevUnidadNegocio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SevUnidadNegocioRepository sevUnidadNegocioRepository;

    public SevUnidadNegocioResource(SevUnidadNegocioRepository sevUnidadNegocioRepository) {
        this.sevUnidadNegocioRepository = sevUnidadNegocioRepository;
    }

    /**
     * {@code POST  /sev-unidad-negocios} : Create a new sevUnidadNegocio.
     *
     * @param sevUnidadNegocio the sevUnidadNegocio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sevUnidadNegocio, or with status {@code 400 (Bad Request)} if the sevUnidadNegocio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SevUnidadNegocio>> createSevUnidadNegocio(@Valid @RequestBody SevUnidadNegocio sevUnidadNegocio)
        throws URISyntaxException {
        log.debug("REST request to save SevUnidadNegocio : {}", sevUnidadNegocio);
        if (sevUnidadNegocio.getId() != null) {
            throw new BadRequestAlertException("A new sevUnidadNegocio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sevUnidadNegocioRepository
            .save(sevUnidadNegocio)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/sev-unidad-negocios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sev-unidad-negocios/:id} : Updates an existing sevUnidadNegocio.
     *
     * @param id the id of the sevUnidadNegocio to save.
     * @param sevUnidadNegocio the sevUnidadNegocio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevUnidadNegocio,
     * or with status {@code 400 (Bad Request)} if the sevUnidadNegocio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sevUnidadNegocio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SevUnidadNegocio>> updateSevUnidadNegocio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SevUnidadNegocio sevUnidadNegocio
    ) throws URISyntaxException {
        log.debug("REST request to update SevUnidadNegocio : {}, {}", id, sevUnidadNegocio);
        if (sevUnidadNegocio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sevUnidadNegocio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sevUnidadNegocioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sevUnidadNegocioRepository
                    .save(sevUnidadNegocio)
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
     * {@code PATCH  /sev-unidad-negocios/:id} : Partial updates given fields of an existing sevUnidadNegocio, field will ignore if it is null
     *
     * @param id the id of the sevUnidadNegocio to save.
     * @param sevUnidadNegocio the sevUnidadNegocio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevUnidadNegocio,
     * or with status {@code 400 (Bad Request)} if the sevUnidadNegocio is not valid,
     * or with status {@code 404 (Not Found)} if the sevUnidadNegocio is not found,
     * or with status {@code 500 (Internal Server Error)} if the sevUnidadNegocio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SevUnidadNegocio>> partialUpdateSevUnidadNegocio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SevUnidadNegocio sevUnidadNegocio
    ) throws URISyntaxException {
        log.debug("REST request to partial update SevUnidadNegocio partially : {}, {}", id, sevUnidadNegocio);
        if (sevUnidadNegocio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sevUnidadNegocio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sevUnidadNegocioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SevUnidadNegocio> result = sevUnidadNegocioRepository
                    .findById(sevUnidadNegocio.getId())
                    .map(existingSevUnidadNegocio -> {
                        if (sevUnidadNegocio.getIdUnidadNegocio() != null) {
                            existingSevUnidadNegocio.setIdUnidadNegocio(sevUnidadNegocio.getIdUnidadNegocio());
                        }
                        if (sevUnidadNegocio.getNombre() != null) {
                            existingSevUnidadNegocio.setNombre(sevUnidadNegocio.getNombre());
                        }
                        if (sevUnidadNegocio.getFkIdResponsable() != null) {
                            existingSevUnidadNegocio.setFkIdResponsable(sevUnidadNegocio.getFkIdResponsable());
                        }

                        return existingSevUnidadNegocio;
                    })
                    .flatMap(sevUnidadNegocioRepository::save);

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
     * {@code GET  /sev-unidad-negocios} : get all the sevUnidadNegocios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sevUnidadNegocios in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<SevUnidadNegocio>> getAllSevUnidadNegocios() {
        log.debug("REST request to get all SevUnidadNegocios");
        return sevUnidadNegocioRepository.findAll().collectList();
    }

    /**
     * {@code GET  /sev-unidad-negocios} : get all the sevUnidadNegocios as a stream.
     * @return the {@link Flux} of sevUnidadNegocios.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SevUnidadNegocio> getAllSevUnidadNegociosAsStream() {
        log.debug("REST request to get all SevUnidadNegocios as a stream");
        return sevUnidadNegocioRepository.findAll();
    }

    /**
     * {@code GET  /sev-unidad-negocios/:id} : get the "id" sevUnidadNegocio.
     *
     * @param id the id of the sevUnidadNegocio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sevUnidadNegocio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SevUnidadNegocio>> getSevUnidadNegocio(@PathVariable("id") Long id) {
        log.debug("REST request to get SevUnidadNegocio : {}", id);
        Mono<SevUnidadNegocio> sevUnidadNegocio = sevUnidadNegocioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sevUnidadNegocio);
    }

    /**
     * {@code DELETE  /sev-unidad-negocios/:id} : delete the "id" sevUnidadNegocio.
     *
     * @param id the id of the sevUnidadNegocio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSevUnidadNegocio(@PathVariable("id") Long id) {
        log.debug("REST request to delete SevUnidadNegocio : {}", id);
        return sevUnidadNegocioRepository
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
