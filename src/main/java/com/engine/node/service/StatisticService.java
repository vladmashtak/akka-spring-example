package com.engine.node.service;

import com.engine.node.mongo.entities.SessionTraffic;
import com.engine.node.mongo.repositories.SessionTrafficRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService {
    private final SessionTrafficRepository sessionTrafficRepository;

    @Autowired
    public StatisticService(SessionTrafficRepository sessionTrafficRepository) {
        this.sessionTrafficRepository = sessionTrafficRepository;
    }

    public List<SessionTraffic> getAllSessionTraffic() {
        return sessionTrafficRepository
                .findAll()
                .subList(0, 100);
    }
}
