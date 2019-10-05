package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.domain.Metrics;
import com.sergeykotov.allocation.domain.Path;
import com.sergeykotov.allocation.domain.Vertex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptimisationService {
    private static final Logger log = Logger.getLogger(OptimisationService.class);
    private final AllocationService allocationService;

    @Autowired
    public OptimisationService(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    public String generateOptimalAllocation() {
        return null;
    }

    public Metrics evaluateMetrics() {
        return null;
    }

    public Path findShortestPath(Actor actor, Vertex source, Vertex destination) {
        return null;
    }
}