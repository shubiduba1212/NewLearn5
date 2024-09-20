package com.newrun5.save_pdf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.newrun5.save_pdf.repository")
public class MongoConfig {
}

