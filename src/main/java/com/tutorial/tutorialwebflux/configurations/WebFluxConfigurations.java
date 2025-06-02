package com.tutorial.tutorialwebflux.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfigurations implements WebFluxConfigurer{
    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        //Aumentar databuffer hasta 500 KB para leer Multipart Files
        configurer.defaultCodecs().maxInMemorySize(600 * 1024 * 1024);
    }
    
}
