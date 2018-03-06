package com.engine.node.config;


import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.Collections;


@Configuration
@EntityScan(basePackages = "com.engine.node.mongo.entities")
@EnableMongoRepositories(basePackages={"com.engine.node.mongo.repositories"})
public class MongoConfiguration extends AbstractMongoConfiguration {
    @Value("${spring.data.mongodb.db}")
    private String databaseName;

    @Value("${spring.data.mongodb.user}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Bean
    public Mongo mongo() throws UnknownHostException {
        // Set credentials
        MongoCredential credential = MongoCredential.createCredential(username, getDatabaseName(), password.toCharArray());
        ServerAddress serverAddress = new ServerAddress(host, port);

        return new MongoClient(serverAddress, Collections.singletonList(credential));
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
}