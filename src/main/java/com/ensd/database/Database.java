package com.ensd.database;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import org.bson.*;

import com.mongodb.client.*;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;


public class Database {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Database.class);
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static final String uri = "mongodb://localhost:27017/";

    public static void connect() {
        turnOffLogging();
        LOGGER.info("Connecting to database:: {}", uri);
        // Replace the placeholder with your MongoDB deployment's connection string
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("local");
        LOGGER.info("Database connected:: {}", uri);
    }

    public static MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }

    private static void turnOffLogging() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);
    }
}