package com.ensd.database;

import com.mongodb.client.MongoCollection;
import org.bson.*;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;

public class Collection {
    private final MongoCollection<Document> collection;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Collection.class);

    public Collection(MongoCollection<Document> collection) {
        this.collection = collection;

    }

    public JSONObject findOne(String fieldName, String value) {
        Document doc = collection.find(eq(fieldName, value)).first();
        assert doc != null;
        return conversation(doc);
    }

    public void insertOne(JSONObject obj) {

       Document _doc = Document.parse(obj.toString());
        collection.insertOne(_doc);
    }

    public JSONObject conversation(Document _doc) {
        return new JSONObject(_doc.toJson());
    }
}
