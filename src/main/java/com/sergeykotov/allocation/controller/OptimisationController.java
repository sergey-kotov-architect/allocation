package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.domain.Metrics;
import com.sergeykotov.allocation.domain.Path;
import com.sergeykotov.allocation.domain.Vertex;
import com.sergeykotov.allocation.service.OptimisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class OptimisationController {
    private final OptimisationService optimisationService;

    @Autowired
    public OptimisationController(OptimisationService optimisationService) {
        this.optimisationService = optimisationService;
    }

    @PutMapping
    public String generateOptimalAllocation() {
        return optimisationService.generateOptimalAllocation();
    }

    @GetMapping("metrics")
    public Metrics evaluateMetrics() {
        return optimisationService.evaluateMetrics();
    }

    @GetMapping("path")
    public Path findShortestPath(Actor actor, Vertex source, Vertex destination) {
        return optimisationService.findShortestPath(actor, source, destination);
    }
}