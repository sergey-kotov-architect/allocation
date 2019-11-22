package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.service.ActorService;
import com.sergeykotov.allocation.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actor")
public class ActorController {
    private final AuthorizationService authorizationService;
    private final ActorService actorService;

    @Autowired
    public ActorController(AuthorizationService authorizationService, ActorService actorService) {
        this.authorizationService = authorizationService;
        this.actorService = actorService;
    }

    @PostMapping
    public void create(@RequestHeader String authorization, @RequestBody Actor actor) {
        authorizationService.authorize(authorization);
        actorService.create(actor);
    }

    @GetMapping
    public List<Actor> getAll(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return actorService.getAll();
    }

    @GetMapping("/{id}")
    public Actor getById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        return actorService.getById(id);
    }

    @PutMapping
    public void update(@RequestHeader String authorization, @RequestBody Actor actor) {
        authorizationService.authorize(authorization);
        actorService.update(actor);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        actorService.deleteById(id);
    }
}