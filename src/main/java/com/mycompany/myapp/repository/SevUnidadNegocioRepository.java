package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SevUnidadNegocio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SevUnidadNegocio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SevUnidadNegocioRepository extends ReactiveCrudRepository<SevUnidadNegocio, Long>, SevUnidadNegocioRepositoryInternal {
    @Override
    <S extends SevUnidadNegocio> Mono<S> save(S entity);

    @Override
    Flux<SevUnidadNegocio> findAll();

    @Override
    Mono<SevUnidadNegocio> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SevUnidadNegocioRepositoryInternal {
    <S extends SevUnidadNegocio> Mono<S> save(S entity);

    Flux<SevUnidadNegocio> findAllBy(Pageable pageable);

    Flux<SevUnidadNegocio> findAll();

    Mono<SevUnidadNegocio> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SevUnidadNegocio> findAllBy(Pageable pageable, Criteria criteria);
}
