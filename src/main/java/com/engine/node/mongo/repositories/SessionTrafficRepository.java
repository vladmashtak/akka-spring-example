package com.engine.node.mongo.repositories;

import com.engine.node.mongo.entities.SessionTraffic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionTrafficRepository extends MongoRepository<SessionTraffic, Long> {
}
