package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Vertex;
import com.sergeykotov.allocation.service.VertexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vertex")
public class VertexController {
    private final VertexService vertexService;

    @Autowired
    public VertexController(VertexService vertexService) {
        this.vertexService = vertexService;
    }

    @PostMapping
    public void create(@RequestBody Vertex vertex) {
        vertexService.create(vertex);
    }

    @GetMapping
    public List<Vertex> getAll() {
        return vertexService.getAll();
    }

    @GetMapping("/{id}")
    public Vertex getById(@PathVariable long id) {
        return vertexService.getById(id);
    }

    @PutMapping
    public void update(@RequestBody Vertex vertex) {
        vertexService.update(vertex);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        vertexService.deleteById(id);
    }
}