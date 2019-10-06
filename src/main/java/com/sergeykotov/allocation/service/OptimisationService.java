package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.*;
import com.sergeykotov.allocation.exception.DataModificationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class OptimisationService {
    private static final Logger log = Logger.getLogger(OptimisationService.class);
    private static final AtomicBoolean generating = new AtomicBoolean();
    private final AllocationService allocationService;
    private final VertexService vertexService;

    @Autowired
    public OptimisationService(AllocationService allocationService, VertexService vertexService) {
        this.allocationService = allocationService;
        this.vertexService = vertexService;
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
        //TODO: evaluate metrics
        return null;
    }

    public Path findShortestPath(Path path) {
        log.info("extracting vertices to find shortest path...");
        Set<Vertex> vertices = new HashSet<>(vertexService.getAll());
        Actor actor = path.getActor();
        Vertex source = path.getSource();
        Vertex destination = path.getDestination();
        log.info("finding shortest path for " + actor + " from " + source + " to " + destination + "...");
        evaluatePaths(actor, path.getSource(), vertices);
        //TODO: build shortest path
        log.info("path has been found");
        return path;
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
        Set<Vertex> vertices = allocations.stream().map(Allocation::getVertex).collect(Collectors.toSet());
        allocations.forEach(a -> a.setVertexRank(evaluateVertexRank(a.getActor(), a.getVertex(), vertices)));
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

    private double evaluateVertexRank(Actor actor, Vertex vertex, Set<Vertex> vertices) {
        evaluatePaths(actor, vertex, vertices);
        int pathCount = vertices.size() - 1;
        double pathSum = vertices.stream().mapToDouble(Vertex::getPath).sum();
        double meanPath = pathSum / pathCount;
        double deviationSum = vertices.stream()
                .filter(v -> !v.equals(vertex))
                .mapToDouble(v -> Math.abs(v.getPath() - meanPath))
                .sum();
        return deviationSum / pathCount;
    }

    private void evaluatePaths(Actor actor, Vertex vertex, Set<Vertex> vertices) {
        vertices.forEach(v -> {
            v.setPath(Double.MAX_VALUE);
            v.setVisited(false);
        });
        vertex.setPath(0.0);
        while (true) {
            Optional<Vertex> nextVertex = vertices.stream()
                    .filter(v -> !v.isVisited())
                    .min(Comparator.comparingDouble(Vertex::getPath));
            if (!nextVertex.isPresent()) {
                break;
            }
            nextVertex.get().getEdges().forEach(e -> resetPath(actor, e));
        }
    }

    private void resetPath(Actor actor, Edge edge) {
        Vertex source = edge.getSource();
        Vertex vertex = edge.getDestination();
        if (vertex.isVisited()) {
            return;
        }
        vertex.setVisited(true);
        double speed = Math.min(actor.getSpeed(), edge.getSpeedLimit());
        double time = edge.getDistance() / speed;
        double weight = time / vertex.getRank();
        double path = source.getPath() + weight;
        if (path < vertex.getPath()) {
            vertex.setPath(path);
            vertex.setSource(source);
        }
    }
}