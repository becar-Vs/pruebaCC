package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SevGraficaData;
import com.mycompany.myapp.repository.rowmapper.SevGraficaDataRowMapper;
import com.mycompany.myapp.repository.rowmapper.SevGraficaRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the SevGraficaData entity.
 */
@SuppressWarnings("unused")
class SevGraficaDataRepositoryInternalImpl extends SimpleR2dbcRepository<SevGraficaData, Long> implements SevGraficaDataRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SevGraficaRowMapper sevgraficaMapper;
    private final SevGraficaDataRowMapper sevgraficadataMapper;

    private static final Table entityTable = Table.aliased("sev_grafica_data", EntityManager.ENTITY_ALIAS);
    private static final Table graficaTable = Table.aliased("sev_grafica", "grafica");

    public SevGraficaDataRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SevGraficaRowMapper sevgraficaMapper,
        SevGraficaDataRowMapper sevgraficadataMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SevGraficaData.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sevgraficaMapper = sevgraficaMapper;
        this.sevgraficadataMapper = sevgraficadataMapper;
    }

    @Override
    public Flux<SevGraficaData> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SevGraficaData> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SevGraficaDataSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SevGraficaSqlHelper.getColumns(graficaTable, "grafica"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(graficaTable)
            .on(Column.create("grafica_id", entityTable))
            .equals(Column.create("id", graficaTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SevGraficaData.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SevGraficaData> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SevGraficaData> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<SevGraficaData> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<SevGraficaData> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<SevGraficaData> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private SevGraficaData process(Row row, RowMetadata metadata) {
        SevGraficaData entity = sevgraficadataMapper.apply(row, "e");
        entity.setGrafica(sevgraficaMapper.apply(row, "grafica"));
        return entity;
    }

    @Override
    public <S extends SevGraficaData> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
