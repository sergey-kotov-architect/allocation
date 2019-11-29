package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Vertex;
import com.sergeykotov.allocation.service.AuthorizationService;
import com.sergeykotov.allocation.service.VertexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vertex")
public class VertexController {
    private final AuthorizationService authorizationService;
    private final VertexService vertexService;

    @Autowired
    public VertexController(AuthorizationService authorizationService, VertexService vertexService) {
        this.authorizationService = authorizationService;
        this.vertexService = vertexService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestHeader String authorization, @RequestBody @Valid Vertex vertex) {
        authorizationService.authorize(authorization);
        vertexService.create(vertex);
    }

    @GetMapping
    public List<Vertex> getAll(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return vertexService.getAll();
    }

    @GetMapping("/{id}")
    public Vertex getById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        return vertexService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@RequestHeader String authorization,
                           @PathVariable long id,
                           @RequestBody @Valid Vertex vertex) {
        authorizationService.authorize(authorization);
        vertexService.updateById(id, vertex);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        vertexService.deleteById(id);
    }
}