package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SevProceso;
import com.mycompany.myapp.repository.rowmapper.SevProcesoRowMapper;
import com.mycompany.myapp.repository.rowmapper.SevUnidadNegocioRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the SevProceso entity.
 */
@SuppressWarnings("unused")
class SevProcesoRepositoryInternalImpl extends SimpleR2dbcRepository<SevProceso, Long> implements SevProcesoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SevUnidadNegocioRowMapper sevunidadnegocioMapper;
    private final SevProcesoRowMapper sevprocesoMapper;

    private static final Table entityTable = Table.aliased("sev_proceso", EntityManager.ENTITY_ALIAS);
    private static final Table unidadNegocioTable = Table.aliased("sev_unidad_negocio", "unidadNegocio");

    public SevProcesoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SevUnidadNegocioRowMapper sevunidadnegocioMapper,
        SevProcesoRowMapper sevprocesoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SevProceso.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sevunidadnegocioMapper = sevunidadnegocioMapper;
        this.sevprocesoMapper = sevprocesoMapper;
    }

    @Override
    public Flux<SevProceso> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SevProceso> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SevProcesoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SevUnidadNegocioSqlHelper.getColumns(unidadNegocioTable, "unidadNegocio"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(unidadNegocioTable)
            .on(Column.create("unidad_negocio_id", entityTable))
            .equals(Column.create("id", unidadNegocioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SevProceso.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SevProceso> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SevProceso> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<SevProceso> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<SevProceso> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<SevProceso> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private SevProceso process(Row row, RowMetadata metadata) {
        SevProceso entity = sevprocesoMapper.apply(row, "e");
        entity.setUnidadNegocio(sevunidadnegocioMapper.apply(row, "unidadNegocio"));
        return entity;
    }

    @Override
    public <S extends SevProceso> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
