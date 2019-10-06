package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.domain.Metrics;
import com.sergeykotov.allocation.domain.Path;
import com.sergeykotov.allocation.service.OptimisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<Allocation> getActiveAllocations() {
        return optimisationService.getActiveAllocations();
    }

    @GetMapping("metrics")
    public Metrics evaluateMetrics() {
        return optimisationService.evaluateMetrics();
    }

    @GetMapping("path")
    public Path findShortestPath(@RequestBody Path path) {
        return optimisationService.findShortestPath(path);
    }
}