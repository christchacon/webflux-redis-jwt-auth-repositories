package com.tutorial.tutorialwebflux.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.tutorial.tutorialwebflux.models.ContactEntity;

@Repository
public interface ContactsRepository extends ReactiveCrudRepository<ContactEntity, Long>{
    
}
