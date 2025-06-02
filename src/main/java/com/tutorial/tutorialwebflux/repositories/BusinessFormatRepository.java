package com.tutorial.tutorialwebflux.repositories;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.tutorial.tutorialwebflux.models.BussinessFormatConfigEntity;
import reactor.core.publisher.Flux;
import java.util.List;

@Repository
public interface BusinessFormatRepository
    extends ReactiveCrudRepository<BussinessFormatConfigEntity, Long> {

    //@Modifying
    @Query("SELECT * FROM tbl_bussiness_format_config WHERE id_config_format = :id")
    Flux<BussinessFormatConfigEntity> getAllByIdConfigFormat(Long id);
    
}
