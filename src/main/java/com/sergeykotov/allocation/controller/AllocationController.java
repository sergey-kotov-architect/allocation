package com.sergeykotov.allocation.controller;

import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.service.AllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/allocation")
public class AllocationController {
    private final AllocationService allocationService;

    @Autowired
    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping
    public void create(@RequestBody Allocation allocation) {
        allocationService.create(allocation);
    }

    @GetMapping
    public List<Allocation> getAll() {
        return allocationService.getAll();
    }

    @GetMapping("/{id}")
    public Allocation getById(@PathVariable long id) {
        return allocationService.getById(id);
    }

    @PutMapping
    public void update(@RequestBody Allocation allocation) {
        allocationService.update(allocation);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        allocationService.deleteById(id);
    }
}