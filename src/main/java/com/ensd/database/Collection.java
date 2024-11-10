package com.ensd.database;

import com.mongodb.client.MongoCollection;
import org.bson.*;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;

public class Collection {
    private final MongoCollection<Document> collection;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Collection.class);

    public Collection(String collectionName) {
        collection = Database.getCollection(collectionName);


    }

    public JSONObject findOne(String fieldName, String value) {
        Document doc = collection.find(eq(fieldName, value)).first();
        assert doc != null;
        return conversation(doc);
    }

    public void insertOne(Document obj) {

        collection.insertOne(obj);
    }

    public JSONObject conversation(Document _doc) {
        return new JSONObject(_doc.toJson());
    }
}
