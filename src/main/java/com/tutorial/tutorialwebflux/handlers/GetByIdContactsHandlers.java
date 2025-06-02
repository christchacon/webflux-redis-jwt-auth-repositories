package com.tutorial.tutorialwebflux.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.tutorial.tutorialwebflux.controllers.ContactsController;
import com.tutorial.tutorialwebflux.messages.MessageContactResponse;
import com.tutorial.tutorialwebflux.models.ContactEntity;
import com.tutorial.tutorialwebflux.services.ContactService;

import reactor.core.publisher.Mono;

@Component
public class GetByIdContactsHandlers {
    private Logger logger = LoggerFactory.getLogger(GetByIdContactsHandlers.class);

    @Autowired
    ContactService cs;

    public Mono<ServerResponse> getById(ServerRequest request){

        MessageContactResponse cr = new MessageContactResponse();
        //String sid = request.pathVariable("id");
        Long id = Long.valueOf(request.pathVariable("id"));

        return cs.getById(id)
        .switchIfEmpty(Mono.error(new Exception("Contact id: "+id+ " not found")))
        .flatMap(contact ->{
            logger.info("Contact found id {}", id);
            cr.setContact(contact);
            cr.setMessage("Contact found");
            cr.setStatus("200");
            return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).bodyValue(cr);
         }).onErrorResume(e ->{
            if (e.getMessage().contains("not found")) {
                logger.info("Contact not found id {}", id);
                cr.setMessage("Contact not found");
                cr.setStatus("404");
                return  ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(cr);
            }else{
                logger.info("An error ocurred on search contact, with message {}", e.getMessage());
                cr.setMessage("An error ocurred on search contact, with message: "+ e.getMessage());
                cr.setStatus("500");
                return ServerResponse
                                 .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .bodyValue(cr);
            }
            
        });
     
    }

}
        
