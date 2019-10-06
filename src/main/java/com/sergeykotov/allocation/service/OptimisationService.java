package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.*;
import com.sergeykotov.allocation.exception.DataModificationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
        log.info("making stable matches...");
        while (true) {
            Allocation allocation = allocations.stream()
                    .filter(a -> a.getActor().getVertex() == null && !a.isProposed())
                    .findAny().orElse(null);
            if (allocation == null) {
                break;
            }
            Actor actor = allocation.getActor();
            List<Allocation> candidates = allocations.stream()
                    .filter(a -> a.getActor().equals(actor) && !a.isProposed())
                    .sorted(Comparator.comparingDouble(Allocation::getVertexRank))
                    .collect(Collectors.toList());
            for (Allocation candidate : candidates) {
                candidate.setProposed(true);
                Vertex vertex = candidate.getVertex();
                Actor rival = vertex.getActor();
                if (rival == null) {
                    vertex.setActor(actor);
                    actor.setVertex(vertex);
                    candidate.setActive(true);
                    break;
                }
                Allocation rivalAllocation = allocations.stream()
                        .filter(a -> a.getActor().equals(rival) && a.getVertex().equals(vertex))
                        .findAny().orElse(null);
                if (rivalAllocation != null && rivalAllocation.getActorRank() < allocation.getActorRank()) {
                    rival.setVertex(null);
                    rivalAllocation.setActive(false);
                    vertex.setActor(actor);
                    actor.setVertex(vertex);
                    candidate.setActive(true);
                    break;
                }
            }
        }
    }
}