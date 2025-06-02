package com.tutorial.tutorialwebflux.daos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import com.tutorial.tutorialwebflux.models.BeneficiaryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BeneficiariesDao {
    
    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private R2dbcEntityTemplate template;

    public Mono<BeneficiaryEntity> getBeneficiaryById(Long id) {
        Logger logger = LoggerFactory.getLogger(BeneficiariesDao.class);
        logger.info("Begin search entity: {}", id);
        return template
            .select(BeneficiaryEntity.class) // entidad a mapear
            .matching(Query.query(Criteria.where("id").is(id))) // condición WHERE
            .one() // esperamos un solo resultado
            .doOnNext(be -> logger.info("✔ Found entity with ID: {}", id))
            .doOnError(e -> logger.error("❌ Error fetching entity: {}", e.getMessage()));
    
        
        // return databaseClient.sql("SELECT * FROM tbl_beneficiaries WHERE id = :id")
        //     .bind("id", id)
        //     .map((row, metadata) -> {
        //         BeneficiaryEntity be = new BeneficiaryEntity();
        //         be.setId(row.get("id", Long.class));
        //         be.setAmount(row.get("amount", BigDecimal.class));
        //         be.setAccountNumber(row.get("account_number", String.class));
        //         be.setAccountType(row.get("account_type",String.class));
        //         be.setBank(row.get("bank", String.class));
        //         be.setNames(row.get("names", String.class));
        //         be.setLastNames(row.get("last_names", String.class));
        //         be.setRut(row.get("rut", String.class));
        //         be.setAge(row.get("age", int.class));
        //         // Asigna los demás campos...
        //         return be;
        //     })
        //     .one()
        //     .doOnNext(be -> logger.info("End search entity: {}", id))
        //     .doOnError(e -> logger.error("An error occurred: {}", e.getMessage()));
    }

    public Flux<BeneficiaryEntity> saveAll(List<BeneficiaryEntity> beneficiaries) {
        return Flux.fromIterable(beneficiaries)
            .concatMap(this::insertAndReturnId);
        }

    public Mono<BeneficiaryEntity> insertAndReturnId(BeneficiaryEntity b) {
        return databaseClient
            .sql("INSERT INTO tbl_beneficiaries (rut, names, last_names, age, bank, account_type, account_number, amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
            .bind(0, b.getRut())
            .bind(1, b.getNames())
            .bind(2, b.getLastNames())
            .bind(3, b.getAge())
            .bind(4, b.getBank())
            .bind(5, b.getAccountType())
            .bind(6, b.getAccountNumber())
            .bind(7, b.getAmount())
            .then() // Esperamos a que el INSERT se complete
            .then(databaseClient.sql("SELECT LAST_INSERT_ID() AS id")
                .map((row, meta) -> row.get("id", Long.class))
                .one()
            )
            .map(id -> {
                b.setId(id);
                return b;
            });
    }

    // public Mono<Integer> batchInsert(List<BeneficiaryEntity> list) {
    //     if (list.isEmpty()) return Mono.just(0);

    //     StringBuilder sql = new StringBuilder("""
    //         INSERT INTO tbl_beneficiaries 
    //         (rut, names, last_names, age, bank, account_type, account_number, amount)
    //         VALUES 
    //     """);

    //     List<Object> params = new ArrayList<>();
    //     for (int i = 0; i < list.size(); i++) {
    //         sql.append("(?, ?, ?, ?, ?, ?, ?, ?)");
    //         if (i < list.size() - 1) {
    //             sql.append(", ");
    //         }

    //         BeneficiaryEntity b = list.get(i);
    //         params.addAll(List.of(
    //             b.getRut(),
    //             b.getNames(),
    //             b.getLastNames(),
    //             b.getAge(),
    //             b.getBank(),
    //             b.getAccountType(),
    //             b.getAccountNumber(),
    //             b.getAmount()
    //         ));
    //     }

    //     DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sql.toString());
    //     for (int i = 0; i < params.size(); i++) {
    //         spec = spec.bind(i, params.get(i));
    //     }

    //     return spec.fetch().rowsUpdated().map(Long::intValue);
    // }

}
