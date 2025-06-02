package com.tutorial.tutorialwebflux.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.core.DatabaseClient;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableR2dbcRepositories
public class DbConfigurations extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    private String dbUrl;

    @Override
    public ConnectionFactory connectionFactory() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'connectionFactory'");
        return ConnectionFactories.get(dbUrl);
    }
    
    @Bean
    public ConnectionFactoryInitializer initializer (ConnectionFactory connectionFactory){
        ConnectionFactoryInitializer connectionFactoryInitializer = new ConnectionFactoryInitializer();
        connectionFactoryInitializer.setConnectionFactory(connectionFactory);
        return connectionFactoryInitializer;
    }

    // @Bean
    // public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
    //     return new R2dbcEntityTemplate(connectionFactory);
    // }
}
