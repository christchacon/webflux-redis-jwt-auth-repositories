package com.tutorial.tutorialwebflux.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.tutorial.tutorialwebflux.models.BeneficiaryEntity;
import com.tutorial.tutorialwebflux.models.ContactEntity;

@Repository
public interface BeneficiariesRepository extends ReactiveCrudRepository<BeneficiaryEntity, Long>{
    
}
