package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.domain.Metrics;
import com.sergeykotov.allocation.domain.Path;
import com.sergeykotov.allocation.service.AuthorizationService;
import com.sergeykotov.allocation.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class OptimisationController {
    private final AuthorizationService authorizationService;
    private final GraphService graphService;

    @Autowired
    public OptimisationController(AuthorizationService authorizationService, GraphService graphService) {
        this.authorizationService = authorizationService;
        this.graphService = graphService;
    }

    @PutMapping
    public String generateOptimalAllocation(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return graphService.generateOptimalAllocation();
    }

    @GetMapping
    public List<Allocation> getActiveAllocations(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return graphService.getActiveAllocations();
    }

    @GetMapping("metrics")
    public Metrics evaluateMetrics(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return graphService.evaluateMetrics();
    }

    @GetMapping("path")
    public Path findShortestPath(@RequestHeader String authorization, @RequestBody Path path) {
        authorizationService.authorize(authorization);
        return graphService.findShortestPath(path);
    }
}