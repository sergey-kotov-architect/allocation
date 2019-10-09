package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.domain.Edge;
import com.sergeykotov.allocation.domain.Vertex;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OptimisationService {
    public void makeStableMatches(List<Allocation> allocations) {
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

    public void evaluateVertexRanks(List<Allocation> allocations) {
        Set<Vertex> vertices = allocations.stream().map(Allocation::getVertex).collect(Collectors.toSet());
        allocations.forEach(a -> a.setVertexRank(evaluateVertexRank(a.getActor(), a.getVertex(), vertices)));
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

    public void evaluatePaths(Actor actor, Vertex vertex, Set<Vertex> vertices) {
        vertices.forEach(v -> {
            v.setPath(Double.MAX_VALUE);
            v.setVisited(false);
        });
        vertex.setPath(0.0);
        vertex.setVisited(true);
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