package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SevGrafica;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SevGrafica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SevGraficaRepository extends ReactiveCrudRepository<SevGrafica, Long>, SevGraficaRepositoryInternal {
    @Override
    Mono<SevGrafica> findOneWithEagerRelationships(Long id);

    @Override
    Flux<SevGrafica> findAllWithEagerRelationships();

    @Override
    Flux<SevGrafica> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM sev_grafica entity WHERE entity.proceso_id = :id")
    Flux<SevGrafica> findByProceso(Long id);

    @Query("SELECT * FROM sev_grafica entity WHERE entity.proceso_id IS NULL")
    Flux<SevGrafica> findAllWhereProcesoIsNull();

    @Override
    <S extends SevGrafica> Mono<S> save(S entity);

    @Override
    Flux<SevGrafica> findAll();

    @Override
    Mono<SevGrafica> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SevGraficaRepositoryInternal {
    <S extends SevGrafica> Mono<S> save(S entity);

    Flux<SevGrafica> findAllBy(Pageable pageable);

    Flux<SevGrafica> findAll();

    Mono<SevGrafica> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SevGrafica> findAllBy(Pageable pageable, Criteria criteria);

    Mono<SevGrafica> findOneWithEagerRelationships(Long id);

    Flux<SevGrafica> findAllWithEagerRelationships();

    Flux<SevGrafica> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
