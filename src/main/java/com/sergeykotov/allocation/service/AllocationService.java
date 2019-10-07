package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.dao.AllocationDao;
import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.exception.DataModificationException;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AllocationService {
    private static final Logger log = Logger.getLogger(AllocationService.class);
    private final AllocationDao allocationDao;
    private final OptimisationService optimisationService;

    @Autowired
    public AllocationService(AllocationDao allocationDao, OptimisationService optimisationService) {
        this.allocationDao = allocationDao;
        this.optimisationService = optimisationService;
    }

    public void create(Allocation allocation) {
        if (optimisationService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("creating allocation... " + allocation);
        try {
            allocationDao.create(allocation);
        } catch (SQLException e) {
            log.error("failed to create allocation " + allocation, e);
            throw new InvalidDataException();
        }
        log.info("allocation has been created " + allocation);
    }

    public List<Allocation> getAll() {
        try {
            return allocationDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract allocations", e);
            throw new ExtractionException();
        }
    }

    public Allocation getById(long id) {
        try {
            return allocationDao.getAll().stream()
                    .filter(a -> a.getId() == id)
                    .findAny().orElseThrow(InvalidDataException::new);
        } catch (SQLException e) {
            log.error("failed to extract allocation by id " + id, e);
            throw new InvalidDataException();
        }
    }

    public void update(Allocation allocation) {
        if (optimisationService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating allocation... " + allocation);
        try {
            allocationDao.update(allocation);
        } catch (SQLException e) {
            log.error("failed to update allocation " + allocation, e);
            throw new InvalidDataException();
        }
        log.info("allocation has been updated " + allocation);
    }

    public void update(List<Allocation> allocations) {
        if (optimisationService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating allocations... " + allocations);
        try {
            allocationDao.update(allocations);
        } catch (SQLException e) {
            log.error("failed to update allocations " + allocations, e);
            throw new InvalidDataException();
        }
        log.info("allocations have been updated " + allocations);
    }

    public void deleteById(long id) {
        if (optimisationService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("deleting allocation by id " + id);
        try {
            allocationDao.deleteById(id);
        } catch (Exception e) {
            log.error("failed to delete allocation by id " + id, e);
            throw new InvalidDataException();
        }
        log.info("allocation has been deleted by id " + id);
    }
}