package com.pinwox.tvmazeapi.repository;

import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository 
public class ShowCacheRepository {

    private static final String COLLECTION_NAME = "shows";

    private final MongoTemplate mongoTemplate;

    public ShowCacheRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Map<String, Object>> findById(Long showId) {
        Document found = mongoTemplate.findById(showId, Document.class, COLLECTION_NAME);
        if (found == null) {
            return Optional.empty();
        }
        found.remove("_id");
        return Optional.of(found);
    }

    public void save(Long showId, Map<String, Object> show) {
        Document document = new Document(show);
        document.put("_id", showId);
        mongoTemplate.save(document, COLLECTION_NAME);
    }

}
