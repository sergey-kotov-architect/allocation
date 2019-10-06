package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.domain.Metrics;
import com.sergeykotov.allocation.domain.Path;
import com.sergeykotov.allocation.exception.DataModificationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class OptimisationService {
    private static final Logger log = Logger.getLogger(OptimisationService.class);
    private static final AtomicBoolean generating = new AtomicBoolean();
    private final AllocationService allocationService;

    @Autowired
    public OptimisationService(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    public boolean isGenerating() {
        return generating.get();
    }

    public String generateOptimalAllocation() {
        if (generating.get()) {
            throw new DataModificationException();
        }
        generating.set(true);
        try {
            return generate();
        } finally {
            generating.set(false);
        }
    }

    public Metrics evaluateMetrics() {
        return null;
    }

    public Path findShortestPath(Path path) {
        return null;
    }

    private String generate() {
        log.info("extracting allocations to optimise...");
        List<Allocation> allocations = allocationService.getAll();
        log.info(allocations.size() + " allocations have been extracted");
        allocations.forEach(a -> a.setActive(false));

        long start = System.currentTimeMillis();
        evaluateVertexRanks(allocations);
        makeStableMatches(allocations);
        long elapsed = System.currentTimeMillis() - start;

        log.info("saving generated optimal allocation to the database...");
        try {
            allocationService.update(allocations);
        } catch (Exception e) {
            String message = "failed to save generated optimal allocation to db, elapsed " + elapsed + " milliseconds";
            log.error(message, e);
            return message;
        }
        String message = "optimal allocation has been generated, elapsed " + elapsed + " milliseconds";
        log.info(message);
        return message;
    }

    private void evaluateVertexRanks(List<Allocation> allocations) {

    }

    private void makeStableMatches(List<Allocation> allocations) {

    }
}