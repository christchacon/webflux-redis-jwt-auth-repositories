package com.tutorial.tutorialwebflux.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;

@Configuration
public class WebFluxSecurityComfig {

    private final Logger logger = LoggerFactory.getLogger(WebFluxSecurityComfig.class);

    @Bean
    public SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http,
                                                  Converter<Jwt, Flux<GrantedAuthority>> customAuthoritiesConverter) {

        logger.info("Configurando seguridad con roles");
        ReactiveJwtAuthenticationConverter jwtConverter = new ReactiveJwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(customAuthoritiesConverter);
        http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET, "/contacts/**").hasRole("READ")
                .pathMatchers(HttpMethod.GET, "/beneficiaries/**").hasRole("READ")
                .pathMatchers(HttpMethod.POST, "/beneficiaries/**").hasRole("WRITE")
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtConverter))
                .authenticationEntryPoint(jwtErrorEntryPoint())
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler(accessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    public ServerAuthenticationEntryPoint jwtErrorEntryPoint() {
        return (exchange, ex) -> {
            logger.error("Error de autenticación JWT: {}", ex.getMessage(), ex.getCause());
            String mensaje = "{\"error\": \"Token inválido o expirado\"}";
            byte[] bytes = mensaje.getBytes(StandardCharsets.UTF_8);

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        };
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            logger.warn("Acceso denegado: {}", denied.getMessage(), denied.getCause());
            String mensaje = "{\"error\": \"No tienes permisos para acceder a este recurso\"}";
            byte[] bytes = mensaje.getBytes(StandardCharsets.UTF_8);

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        };
    }

    @Bean
    public Converter<Jwt, Flux<GrantedAuthority>> customAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();
        delegate.setAuthoritiesClaimName("roles");
        delegate.setAuthorityPrefix("ROLE_");
        return jwt -> {
                //logger.info(">> Claims del JWT: {}", jwt.getClaims());
                return Flux.fromIterable(delegate.convert(jwt));
            };
        }

}
