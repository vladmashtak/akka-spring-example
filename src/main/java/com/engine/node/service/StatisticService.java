package com.engine.node.service;

import com.engine.node.mongo.entities.SessionTraffic;
import com.engine.node.mongo.repositories.SessionTrafficRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService {
    private int sessionTrafficStartRange;
    private final int sessionTrafficShift = 100;
    private final SessionTrafficRepository sessionTrafficRepository;

    @Autowired
    public StatisticService(SessionTrafficRepository sessionTrafficRepository) {
        this.sessionTrafficRepository = sessionTrafficRepository;
    }

    public List<SessionTraffic> getAllSessionTraffic() {
        List<SessionTraffic> sessionTraffic = sessionTrafficRepository
                .findAll()
                .subList(sessionTrafficStartRange, sessionTrafficStartRange + sessionTrafficShift);

        sessionTrafficStartRange += sessionTrafficShift;

        return sessionTraffic;
    }
}
