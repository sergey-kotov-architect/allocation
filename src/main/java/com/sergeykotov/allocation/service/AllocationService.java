package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import com.sergeykotov.allocation.repository.AllocationRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllocationService {
    private static final Logger log = Logger.getLogger(AllocationService.class);
    private final AllocationRepository allocationRepository;

    @Autowired
    public AllocationService(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public void create(Allocation allocation) {
        log.info("creating allocation... " + allocation);
        try {
            allocationRepository.save(allocation);
        } catch (Exception e) {
            log.error("failed to create allocation " + allocation, e);
            throw new InvalidDataException();
        }
        log.info("allocation has been created " + allocation);
    }

    public List<Allocation> getAll() {
        List<Allocation> allocations = new ArrayList<>();
        try {
            allocationRepository.findAll().forEach(allocations::add);
        } catch (Exception e) {
            log.error("failed to extract allocations", e);
            throw new ExtractionException();
        }
        return allocations;
    }

    public Allocation getById(long id) {
        try {
            return allocationRepository.findById(id).orElseThrow(InvalidDataException::new);
        } catch (Exception e) {
            log.error("failed to extract allocation by id " + id, e);
            throw new InvalidDataException();
        }
    }

    public void update(Allocation allocation) {
        log.info("updating allocation... " + allocation);
        try {
            allocationRepository.save(allocation);
        } catch (Exception e) {
            log.error("failed to update allocation " + allocation, e);
            throw new InvalidDataException();
        }
        log.info("allocation has been updated " + allocation);
    }

    public void deleteById(long id) {
        log.info("deleting allocation by id " + id);
        try {
            allocationRepository.deleteById(id);
        } catch (Exception e) {
            log.error("failed to delete allocation by id " + id, e);
            throw new InvalidDataException();
        }
        log.info("allocation has been deleted by id " + id);
    }
}