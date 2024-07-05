package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SevProceso;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SevProceso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SevProcesoRepository extends ReactiveCrudRepository<SevProceso, Long>, SevProcesoRepositoryInternal {
    @Override
    Mono<SevProceso> findOneWithEagerRelationships(Long id);

    @Override
    Flux<SevProceso> findAllWithEagerRelationships();

    @Override
    Flux<SevProceso> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM sev_proceso entity WHERE entity.unidad_negocio_id = :id")
    Flux<SevProceso> findByUnidadNegocio(Long id);

    @Query("SELECT * FROM sev_proceso entity WHERE entity.unidad_negocio_id IS NULL")
    Flux<SevProceso> findAllWhereUnidadNegocioIsNull();

    @Override
    <S extends SevProceso> Mono<S> save(S entity);

    @Override
    Flux<SevProceso> findAll();

    @Override
    Mono<SevProceso> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SevProcesoRepositoryInternal {
    <S extends SevProceso> Mono<S> save(S entity);

    Flux<SevProceso> findAllBy(Pageable pageable);

    Flux<SevProceso> findAll();

    Mono<SevProceso> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SevProceso> findAllBy(Pageable pageable, Criteria criteria);

    Mono<SevProceso> findOneWithEagerRelationships(Long id);

    Flux<SevProceso> findAllWithEagerRelationships();

    Flux<SevProceso> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
