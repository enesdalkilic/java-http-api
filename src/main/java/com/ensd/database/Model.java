package com.ensd.database;

import com.mongodb.lang.Nullable;
import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *
 * This code is piece of shit, but it works well.
 *
 */

public class Model {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Model.class);
    private final Map<String, FieldType> fields = new HashMap<>();
    private final Document _doc = new Document();
    private Collection _collection = null;

    public Model() {
//        LOGGER.info("New model created.");
    }

    public void registerField(String key, FieldType type) {
        fields.put(key, type);
//        LOGGER.debug("Field registered:: {} Type:: {}", key, type);
    }


    public <T> void put(String key, Object a) throws ModelTypeException {
        Class<T> fieldDataType = getTypeClass(getFieldType(key));

        if (fieldDataType == a.getClass()) {
            _doc.put(key, a);
        } else {
            throw new ModelTypeException("Bad data type for '" + key + "' field. Expected: " + getFieldType(key));
        }

    }


    public void validate() throws ModelValidateException {
        List<String> failedFields = new ArrayList<>();
        _doc.forEach((key, value) -> {
            boolean checked = checkField(key, value);
            if (!checked) {
                failedFields.add(key);
            }
        });

        // If there are any failed fields, throw an exception with details
        if (!failedFields.isEmpty()) {
            throw new ModelValidateException("Validation failed for fields: " + String.join(", ", failedFields));
        }
    }

    public Document get_doc() {
        return _doc;
    }

    public void set_collection(String collectionName) {
        this._collection = new Collection(collectionName);
    }


    public void save() {
        _collection.insertOne(_doc);
    }

    //Helpers
    private <T> boolean checkField(String key, Object a) {
        Class<T> fieldDataType = getTypeClass(getFieldType(key));
        return fieldDataType == a.getClass();
    }

    private FieldType getFieldType(String key) {
        return fields.get(key);
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getTypeClass(FieldType type) {
        if (type.equals(FieldType.STRING)) return (Class<T>) String.class;
        if (type.equals(FieldType.INTEGER)) return (Class<T>) Integer.class;
        if (type.equals(FieldType.LONG)) return (Class<T>) Long.class;
        if (type.equals(FieldType.BOOLEAN)) return (Class<T>) Boolean.class;
        return null;
    }

    public JSONObject findOne(String query, String value) {
        return _collection.findOne(query, value);
    }

    public List<JSONObject> findAll() {
        return _collection.findAll();
    }
}