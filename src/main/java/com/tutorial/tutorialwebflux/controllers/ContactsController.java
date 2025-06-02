package com.tutorial.tutorialwebflux.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tutorial.tutorialwebflux.messages.MessageContactResponse;
import com.tutorial.tutorialwebflux.models.ContactEntity;
import com.tutorial.tutorialwebflux.services.ContactService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RestController
@RequestMapping("/contacts")
public class ContactsController {
    
    private Logger logger = LoggerFactory.getLogger(ContactsController.class);
    
    @Autowired
    ContactService cs;

    @GetMapping
    public Flux<ContactEntity> getAll(){
        return cs.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MessageContactResponse>> getById(        
        @PathVariable("id") Long id
           ){
        // Contact contact = new Contact();
        MessageContactResponse cr = new MessageContactResponse();
        // try {
        //     contact = cs.getById(id).block();
        //     if (contact != null) {
        //         cr.setContact(contact);
        //         cr.setMessage("Contact found");
        //         cr.setStatus("200");
        //         logger.info("Contact found id {}", id);
        //         return ResponseEntity.status(HttpStatus.OK).body(cr) ;
        //     }else{
        //         cr.setMessage("Contact not found");
        //         cr.setStatus("404");
        //         logger.info("Contact not found id {}", id);
        //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cr);
        //     }
        // } catch (Exception e) {
            
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error ocurred with message: "+e.getMessage());
        // }
        
            return cs.getById(id)
                .switchIfEmpty(Mono.error(new Exception("Contact id: "+id+ " not found")))
                .map(contact ->{
                    logger.info("Contact found id {}", id);
                    cr.setContact(contact);
                    cr.setMessage("Contact found");
                    cr.setStatus("200");
                    return new ResponseEntity<>(cr, HttpStatus.OK);
                 }).onErrorResume(e ->{
                    if (e.getMessage().contains("not found")) {
                        logger.info("Contact not found id {}", id);
                        cr.setMessage("Contact not found");
                        cr.setStatus("404");
                        return Mono.just(
                            new ResponseEntity<>(cr, HttpStatus.NOT_FOUND)
                        );
                    }else{
                        logger.info("An error ocurred on search contact, with message {}", e.getMessage());
                        cr.setMessage("An error ocurred on search contact, with message: "+ e.getMessage());
                        cr.setStatus("500");
                        return Mono.just(
                            new ResponseEntity<>(cr, HttpStatus.INTERNAL_SERVER_ERROR)
                        );
                    }
                    
                });
       
    }

    @PostMapping
    public Mono<ContactEntity> save(@RequestBody ContactEntity contact){
        return cs.save(contact);
    }

    @PutMapping("/{id}")
    public Mono<ContactEntity> update(@RequestBody ContactEntity contact, Long id){
        return cs.update(id, contact);
    }

}
