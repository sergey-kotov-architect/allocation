package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Edge;
import com.sergeykotov.allocation.service.AuthorizationService;
import com.sergeykotov.allocation.service.EdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/edge")
public class EdgeController {
    private final AuthorizationService authorizationService;
    private final EdgeService edgeService;

    @Autowired
    public EdgeController(AuthorizationService authorizationService, EdgeService edgeService) {
        this.authorizationService = authorizationService;
        this.edgeService = edgeService;
    }

    @PostMapping
    public void create(@RequestHeader String authorization, @RequestBody Edge edge) {
        authorizationService.authorize(authorization);
        edgeService.create(edge);
    }

    @GetMapping
    public List<Edge> getAll(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return edgeService.getAll();
    }

    @GetMapping("/{id}")
    public Edge getById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        return edgeService.getById(id);
    }

    @PutMapping
    public void update(@RequestHeader String authorization, @RequestBody Edge edge) {
        authorizationService.authorize(authorization);
        edgeService.update(edge);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        edgeService.deleteById(id);
    }
}