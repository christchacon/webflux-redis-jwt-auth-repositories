package com.tutorial.tutorialwebflux.handlers;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.tutorial.tutorialwebflux.messages.MessageBeneficiariesResponse;
import com.tutorial.tutorialwebflux.models.BeneficiaryEntity;
import com.tutorial.tutorialwebflux.models.BussinessFormatConfigEntity;
import com.tutorial.tutorialwebflux.repositories.BusinessFormatRepository;
import com.tutorial.tutorialwebflux.services.BeneficiariesService;
import com.tutorial.tutorialwebflux.services.JwtUserAuthenticationService;

import reactor.core.publisher.Mono;

@Component
public class BeneficiariesHandlers {
    private Logger logger = LoggerFactory.getLogger(BeneficiariesHandlers.class);

    @Autowired
    BeneficiariesService bService;

    @Autowired
    BusinessFormatRepository bfr;

    @Autowired
    JwtUserAuthenticationService jwtUserAuthenticationService;

    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> saveBeneficiariesByFile(ServerRequest request){

        MessageBeneficiariesResponse br = new MessageBeneficiariesResponse();
        Long id = Long.valueOf(request.pathVariable("id"));
        List<BeneficiaryEntity> lb = new ArrayList<>();
        
        Mono<List<BussinessFormatConfigEntity>> bfe = bService.getBFCE(id).collectList();
        
        return jwtUserAuthenticationService.validateUsernameFromParam(request, validatedUser -> {
        logger.info("User: {} authenticated", validatedUser);
        
            return bService.readFile(request)
                .next()
                .flatMap(linesFile -> {
                    lb.addAll(bService.getBeneficiary(linesFile, bfe));
                    logger.info("Begin save list beneficiaries");
                    List<BeneficiaryEntity> lbe = bService.saveBeneficiaries(lb).collectList().block();
                    logger.info("End save list beneficiaries");
                    br.setMessage("File read seccesful");
                    br.setStatus("200");
                    br.setlBeneficiaryEntities(lbe);
                    return ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(br);
                }).onErrorResume(e ->{
                    logger.error("An error ocurred on: {}",e.getMessage());
                    br.setMessage("An error ocurred on read File: "+ e.getMessage());
                    br.setStatus("200");
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(br);  
                }
                );
        });
    }

    //  public Mono<ServerResponse> saveBeneficiariesByFileBatch(ServerRequest request){

    //     MessageBeneficiariesResponse br = new MessageBeneficiariesResponse();
    //     Long id = Long.valueOf(request.pathVariable("id"));
    //     List<BeneficiaryEntity> lb = new ArrayList<>();
        
    //     Mono<List<BussinessFormatConfigEntity>> bfe = bService.getBFCE(id).collectList();
        
    //     return jwtUserAuthenticationService.validateUsernameFromForm(request, validatedUser -> {
    //     logger.info("User: {} authenticated", validatedUser);
        
    //         return bService.readFile(request)
    //             .next()
    //             .flatMap(linesFile -> {
    //                 lb.addAll(bService.getBeneficiary(linesFile, bfe));
    //                 logger.info("Begin save list beneficiaries");
    //                 Integer tbe = Integer.parseInt(bService.saveBeneficiariesBatch(lb).block().toString());
    //                 logger.info("End save list beneficiaries");
    //                 br.setMessage("File read seccesful");
    //                 br.setStatus("200");
    //                 br.setTotalBatch(tbe);
    //                 return ServerResponse.status(HttpStatus.OK)
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .bodyValue(br);
    //             }).onErrorResume(e ->{
    //                 logger.error("An error ocurred on: {}",e.getMessage());
    //                 br.setMessage("An error ocurred on read File: "+ e.getMessage());
    //                 br.setStatus("200");
    //                 return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .bodyValue(br);  
    //             }
    //             );
    //     });
    // }


    public Mono<ServerResponse> getBeneficiaryById(ServerRequest request){

        MessageBeneficiariesResponse br = new MessageBeneficiariesResponse();
        Long id = Long.valueOf(request.pathVariable("id"));
      
        return jwtUserAuthenticationService.validateUsernameFromParam(request, validatedUser -> {
            logger.info("User: {} authenticated", validatedUser);
            return bService.getById(id)
                .switchIfEmpty(Mono.error(new Exception("Beneficiary id: " + id + " not found")))
                .flatMap(beneficiary -> {
                    logger.info("Beneficiary found id {}", id);
                    br.setMessage("Beneficiary found");
                    br.setStatus("200");
                    br.setBeneficiaryEntity(beneficiary);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(br);
                })
                .onErrorResume(e -> {
                    if (e.getMessage().contains("not found")) {
                        logger.info("Beneficiary not found id {}", id);
                        br.setMessage("Beneficiary not found");
                        br.setStatus("404");
                        return ServerResponse.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(br);
                    } else {
                        logger.error("An error occurred on search Beneficiary: {}", e.getMessage());
                        br.setMessage("An error occurred on search beneficiary: " + e.getMessage());
                        br.setStatus("500");
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(br);
                    }
                });
        });
    }
}

