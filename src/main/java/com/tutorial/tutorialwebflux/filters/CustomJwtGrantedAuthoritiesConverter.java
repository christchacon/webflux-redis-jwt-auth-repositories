package com.tutorial.tutorialwebflux.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.tutorial.tutorialwebflux.configurations.WebFluxSecurityComfig;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final Logger logger = LoggerFactory.getLogger(CustomJwtGrantedAuthoritiesConverter.class);

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");

        if (roles == null) {
            logger.error("El token no contiene el campo 'roles' o está vacío.");
            return List.of();  // Devuelve una lista vacía si no hay roles
        }

        logger.info("Roles obtenidos del JWT: {}", roles);
        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Asegúrate de agregar "ROLE_" como prefijo
                    .collect(Collectors.toList());
    }
}
