package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.domain.Metrics;
import com.sergeykotov.allocation.domain.Path;
import com.sergeykotov.allocation.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class OptimisationController {
    private final GraphService graphService;

    @Autowired
    public OptimisationController(GraphService graphService) {
        this.graphService = graphService;
    }

    @PutMapping
    public String generateOptimalAllocation() {
        return graphService.generateOptimalAllocation();
    }

    @GetMapping
    public List<Allocation> getActiveAllocations() {
        return graphService.getActiveAllocations();
    }

    @GetMapping("metrics")
    public Metrics evaluateMetrics() {
        return graphService.evaluateMetrics();
    }

    @GetMapping("path")
    public Path findShortestPath(@RequestBody Path path) {
        return graphService.findShortestPath(path);
    }
}