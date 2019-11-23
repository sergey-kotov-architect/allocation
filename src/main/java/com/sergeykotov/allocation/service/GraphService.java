package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.*;
import com.sergeykotov.allocation.exception.DataModificationException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class GraphService {
    private static final Logger log = Logger.getLogger(GraphService.class);
    private static final AtomicBoolean generating = new AtomicBoolean();

    private final AllocationService allocationService;
    private final VertexService vertexService;
    private final OptimisationService optimisationService;

    @Autowired
    public GraphService(AllocationService allocationService,
                        VertexService vertexService,
                        OptimisationService optimisationService) {
        this.allocationService = allocationService;
        this.vertexService = vertexService;
        this.optimisationService = optimisationService;
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

    public List<Allocation> getActiveAllocations() {
        return allocationService.getAll().stream().filter(Allocation::isActive).collect(Collectors.toList());
    }

    public Metrics evaluateMetrics() {
        log.info("evaluating metrics...");
        Set<Vertex> vertices = new HashSet<>(vertexService.getAll());
        List<Allocation> allocations = getActiveAllocations();
        Metrics metrics = new Metrics();
        Map<Actor, ActorMetrics> actorMetricsMap = metrics.getActorMetricsMap();
        for (Allocation allocation : allocations) {
            Actor actor = allocation.getActor();
            Vertex vertex = allocation.getVertex();

            optimisationService.evaluatePaths(actor, vertex, vertices);
            int pathCount = vertices.size() - 1;
            double pathSum = vertices.stream().mapToDouble(Vertex::getPath).sum();
            double meanPath = pathSum / pathCount;
            double deviationSum = vertices.stream()
                    .filter(v -> !v.equals(vertex))
                    .mapToDouble(v -> Math.abs(v.getPath() - meanPath))
                    .sum();

            ActorMetrics actorMetrics = new ActorMetrics();
            actorMetrics.setVertex(vertex);
            actorMetrics.setMeanPath(meanPath);
            actorMetrics.setMeanDeviation(deviationSum / pathCount);
            actorMetricsMap.put(actor, actorMetrics);
        }
        log.info("metrics have been evaluated");
        return metrics;
    }

    public Path findShortestPath(Path path) {
        log.info("extracting vertices to find shortest path...");
        Set<Vertex> vertices = new HashSet<>(vertexService.getAll());
        Actor actor = path.getActor();
        if (actor == null) {
            throw new InvalidDataException();
        }
        Vertex source = vertices.stream()
                .filter(v -> v.equals(path.getSource()))
                .findAny().orElseThrow(InvalidDataException::new);
        Vertex destination = vertices.stream()
                .filter(v -> v.equals(path.getDestination()))
                .findAny().orElseThrow(InvalidDataException::new);
        log.info("finding shortest path for " + actor + " from " + source + " to " + destination + "...");
        optimisationService.evaluatePaths(actor, path.getSource(), vertices);
        path.setValue(destination.getPath());
        Vertex vertex = destination;
        while (vertex != null && !vertex.equals(source)) {
            path.getVertices().add(vertex);
            vertex = vertex.getSource();
        }
        path.getVertices().add(source);
        Collections.reverse(path.getVertices());
        log.info("path has been found");
        return path;
    }

    private String generate() {
        log.info("extracting allocations to optimise...");
        List<Allocation> allocations = allocationService.getAll();
        log.info(allocations.size() + " allocations have been extracted");
        allocations.forEach(a -> a.setActive(false));

        long start = System.currentTimeMillis();
        optimisationService.evaluateVertexRanks(allocations);
        log.info("making stable matches...");
        optimisationService.makeStableMatches(allocations);
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
}