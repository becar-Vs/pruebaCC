package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SevGraficaData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SevGraficaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SevGraficaDataRepository extends ReactiveCrudRepository<SevGraficaData, Long>, SevGraficaDataRepositoryInternal {
    @Override
    Mono<SevGraficaData> findOneWithEagerRelationships(Long id);

    @Override
    Flux<SevGraficaData> findAllWithEagerRelationships();

    @Override
    Flux<SevGraficaData> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM sev_grafica_data entity WHERE entity.grafica_id = :id")
    Flux<SevGraficaData> findByGrafica(Long id);

    @Query("SELECT * FROM sev_grafica_data entity WHERE entity.grafica_id IS NULL")
    Flux<SevGraficaData> findAllWhereGraficaIsNull();

    @Override
    <S extends SevGraficaData> Mono<S> save(S entity);

    @Override
    Flux<SevGraficaData> findAll();

    @Override
    Mono<SevGraficaData> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SevGraficaDataRepositoryInternal {
    <S extends SevGraficaData> Mono<S> save(S entity);

    Flux<SevGraficaData> findAllBy(Pageable pageable);

    Flux<SevGraficaData> findAll();

    Mono<SevGraficaData> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SevGraficaData> findAllBy(Pageable pageable, Criteria criteria);

    Mono<SevGraficaData> findOneWithEagerRelationships(Long id);

    Flux<SevGraficaData> findAllWithEagerRelationships();

    Flux<SevGraficaData> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
