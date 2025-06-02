package com.tutorial.tutorialwebflux.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tutorial.tutorialwebflux.models.ContactEntity;
import com.tutorial.tutorialwebflux.repositories.ContactsRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Component
public class ContactService {
    
    @Autowired
    ContactsRepository repository;

    public Flux<ContactEntity> getAll(){
        return repository.findAll();
    }

    @Transactional
    public Mono<ContactEntity> getById(Long id){
        
        return repository.findById(id);
    }

    public Mono<ContactEntity> save(ContactEntity contact){
        return repository.save(contact);
    }

    public Mono<ContactEntity> update(Long id, ContactEntity contact){
        ContactEntity newContact = new ContactEntity();
        newContact.setId(id);
        newContact.setFirstName(contact.getFirstName());
        newContact.setLastName(contact.getLastName());
        newContact.setAddress(contact.getAddress());
        newContact.setEmail(contact.getEmail());
        newContact.setGender(contact.getGender());
        newContact.setPhoneNumber(contact.getPhoneNumber());
        return repository.save(newContact);
    }
}
