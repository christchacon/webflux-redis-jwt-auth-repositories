package com.tutorial.tutorialwebflux.services;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import com.tutorial.tutorialwebflux.messages.MessageBeneficiariesResponse;

import reactor.core.publisher.Mono;

@Component
public class JwtUserAuthenticationService {

    private Logger logger = LoggerFactory.getLogger(JwtUserAuthenticationService.class);

    /**
     * Valida que el nombre de usuario incluido en el form-data coincida con el del JWT.
     *
     * @param request El ServerRequest que contiene el token y los datos multipart/form-data.
     * @param onSuccessHandler Función a ejecutar si la validación del usuario es exitosa.
     * @return Mono<ServerResponse> con el resultado de la validación o la ejecución del handler.
     */
    public Mono<ServerResponse> validateUsernameFromParam(ServerRequest request,
                                                      Function<String, Mono<ServerResponse>> onSuccessHandler) {

    MessageBeneficiariesResponse br = new MessageBeneficiariesResponse();

    // Extrae el parámetro 'username' de la URL
    String userParam = request.queryParam("username").orElse(null);

    if (userParam == null || userParam.isBlank()) {
        logger.error("Missing 'username' parameter in URL");
        br.setMessage("Missing 'username' parameter in URL");
        br.setStatus("400");
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(br);
    }

    return request.principal()
        .cast(JwtAuthenticationToken.class)
        .flatMap(jwtAuth -> {
            String usernameFromToken = jwtAuth.getToken().getClaimAsString("preferred_username");

            // Comparar username del JWT con el pasado por query param
            if (!usernameFromToken.equals(userParam)) {
                logger.error("Access denied for user: {}", userParam);
                br.setMessage("Access denied for user: " + userParam);
                br.setStatus("403");
                return ServerResponse.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(br);
            }

            // Validación exitosa
            return onSuccessHandler.apply(userParam);
        });
}
    // public Mono<ServerResponse> validateUsernameFromForm(ServerRequest request,
    //                                                      Function<String, Mono<ServerResponse>> onSuccessHandler) {

    //     // Objeto de respuesta para construir los mensajes personalizados
    //     MessageBeneficiariesResponse br = new MessageBeneficiariesResponse();

    //     String usrRequest = re

    //     return request.principal()
    //         // Se castea el principal a JwtAuthenticationToken para acceder al token JWT
    //         .cast(JwtAuthenticationToken.class)
    //         .flatMap(jwtAuth -> {
    //             // Se extrae el nombre de usuario del JWT (campo: preferred_username)
    //             String usernameFromToken = jwtAuth.getToken().getClaimAsString("preferred_username");

    //             // Se accede a los datos enviados como multipart/form-data
    //             return request.multipartData()
    //                 .flatMap(parts -> {
    //                     // Se extrae la parte del formulario correspondiente al username
    //                     Part userPart = parts.toSingleValueMap().get("username");

    //                     // Verificación: debe existir y ser del tipo FormFieldPart (no un archivo)
    //                     if (!(userPart instanceof FormFieldPart)) {
    //                         logger.error("Missing username in form data");
    //                         br.setMessage("Missing username in form data");
    //                         br.setStatus("400");
    //                         return ServerResponse.status(HttpStatus.BAD_REQUEST)
    //                                 .contentType(MediaType.APPLICATION_JSON)
    //                                 .bodyValue(br);
    //                     }

    //                     // Se obtiene el valor del campo de texto (username)
    //                     String user = ((FormFieldPart) userPart).value();

    //                     // Verificación: el usuario en el JWT debe coincidir con el del form
    //                     if (!usernameFromToken.equals(user)) {
    //                         logger.error("Access denied for user: {}", user);
    //                         br.setMessage("Access denied for user: " + user);
    //                         br.setStatus("403");
    //                         return ServerResponse.status(HttpStatus.FORBIDDEN)
    //                                 .contentType(MediaType.APPLICATION_JSON)
    //                                 .bodyValue(br);
    //                     }

    //                     // Si la validación es correcta, se ejecuta el handler definido
    //                     return onSuccessHandler.apply(user);
    //                 });
    //         });
    // }
}