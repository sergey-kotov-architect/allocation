package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.service.AllocationService;
import com.sergeykotov.allocation.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/allocation")
public class AllocationController {
    private final AuthorizationService authorizationService;
    private final AllocationService allocationService;

    @Autowired
    public AllocationController(AuthorizationService authorizationService, AllocationService allocationService) {
        this.authorizationService = authorizationService;
        this.allocationService = allocationService;
    }

    @PostMapping
    public void create(@RequestHeader String authorization, @RequestBody Allocation allocation) {
        authorizationService.authorize(authorization);
        allocationService.create(allocation);
    }

    @GetMapping
    public List<Allocation> getAll(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return allocationService.getAll();
    }

    @GetMapping("/{id}")
    public Allocation getById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        return allocationService.getById(id);
    }

    @PutMapping
    public void update(@RequestHeader String authorization, @RequestBody Allocation allocation) {
        authorizationService.authorize(authorization);
        allocationService.update(allocation);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        allocationService.deleteById(id);
    }
}