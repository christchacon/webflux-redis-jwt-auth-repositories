package com.tutorial.tutorialwebflux.configurations;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.context.annotation.Primary;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.resource.DefaultClientResources;

@Configuration
//@ImportRuntimeHints(LettuceHints.class)
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
 
    // @Value("${spring.data.redis.password}")
    // private String password;
    
    @Value("${spring.data.redis.port}")
    private int port;
    
    // @Value("${spring.data.redis.username}")
    // private String username;
    //########Funciona######################
    // @Bean
    // @Primary
    // public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisConfiguration defaultRedisConfig) {
    //     LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().build();
    //     return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
    // }

    // @Bean
    // public RedisConfiguration defaultRedisConfig() {
    //     RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    //     config.setHostName(host);
    //     config.setPort(port);
    //     // config.setUsername(username);
    //     // config.setPassword(RedisPassword.of(password));
    //     return config;
    // }
    //#######################################

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, Object> context = RedisSerializationContext
                .<String, Object>newSerializationContext(new StringRedisSerializer())
                .value(new GenericJackson2JsonRedisSerializer())
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}

// class LettuceHints implements RuntimeHintsRegistrar {
//     @Override
//     public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
//         hints.reflection().registerType(DefaultClientResources.class);
//     }

// }
