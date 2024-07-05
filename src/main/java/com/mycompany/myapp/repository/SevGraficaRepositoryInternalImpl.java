package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SevGrafica;
import com.mycompany.myapp.repository.rowmapper.SevGraficaRowMapper;
import com.mycompany.myapp.repository.rowmapper.SevProcesoRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the SevGrafica entity.
 */
@SuppressWarnings("unused")
class SevGraficaRepositoryInternalImpl extends SimpleR2dbcRepository<SevGrafica, Long> implements SevGraficaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SevProcesoRowMapper sevprocesoMapper;
    private final SevGraficaRowMapper sevgraficaMapper;

    private static final Table entityTable = Table.aliased("sev_grafica", EntityManager.ENTITY_ALIAS);
    private static final Table procesoTable = Table.aliased("sev_proceso", "proceso");

    public SevGraficaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SevProcesoRowMapper sevprocesoMapper,
        SevGraficaRowMapper sevgraficaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SevGrafica.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sevprocesoMapper = sevprocesoMapper;
        this.sevgraficaMapper = sevgraficaMapper;
    }

    @Override
    public Flux<SevGrafica> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SevGrafica> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SevGraficaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SevProcesoSqlHelper.getColumns(procesoTable, "proceso"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(procesoTable)
            .on(Column.create("proceso_id", entityTable))
            .equals(Column.create("id", procesoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SevGrafica.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SevGrafica> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SevGrafica> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<SevGrafica> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<SevGrafica> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<SevGrafica> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private SevGrafica process(Row row, RowMetadata metadata) {
        SevGrafica entity = sevgraficaMapper.apply(row, "e");
        entity.setProceso(sevprocesoMapper.apply(row, "proceso"));
        return entity;
    }

    @Override
    public <S extends SevGrafica> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
