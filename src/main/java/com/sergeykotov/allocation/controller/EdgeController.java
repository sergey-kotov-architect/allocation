package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Edge;
import com.sergeykotov.allocation.service.EdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/edge")
public class EdgeController {
    private final EdgeService edgeService;

    @Autowired
    public EdgeController(EdgeService edgeService) {
        this.edgeService = edgeService;
    }

    @PostMapping
    public void create(@RequestBody Edge edge) {
        edgeService.create(edge);
    }

    @GetMapping
    public List<Edge> getAll() {
        return edgeService.getAll();
    }

    @GetMapping("/{id}")
    public Edge getById(@PathVariable long id) {
        return edgeService.getById(id);
    }

    @PutMapping
    public void update(@RequestBody Edge edge) {
        edgeService.update(edge);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        edgeService.deleteById(id);
    }
}