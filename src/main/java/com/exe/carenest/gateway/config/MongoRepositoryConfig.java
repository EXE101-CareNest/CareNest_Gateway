package com.exe.carenest.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.exe.carenest.gateway.model")
public class MongoRepositoryConfig {
}