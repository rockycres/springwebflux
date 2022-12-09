package com.example.springwebflux;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DBConfig extends AbstractR2dbcConfiguration {

    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get("r2dbc:postgresql://localhost:5432/webfluxdb");
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory){
        var initialiser = new ConnectionFactoryInitializer();
        initialiser.setConnectionFactory(connectionFactory);
        var databasPopulator = new CompositeDatabasePopulator();
        databasPopulator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schemas.sql")));
        databasPopulator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
        initialiser.setDatabasePopulator(databasPopulator);
        return initialiser;
    }


}
