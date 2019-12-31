package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.dao.AllocationDao;
import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.exception.DataModificationException;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import com.sergeykotov.allocation.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AllocationService {
    private static final Logger log = LoggerFactory.getLogger(AllocationService.class);

    private final AllocationDao allocationDao;
    private final GraphService graphService;

    @Autowired
    public AllocationService(AllocationDao allocationDao, GraphService graphService) {
        this.allocationDao = allocationDao;
        this.graphService = graphService;
    }

    public void create(Allocation allocation) {
        if (graphService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("creating allocation " + allocation + "...");
        try {
            allocationDao.create(allocation);
        } catch (SQLException e) {
            log.error("failed to create allocation " + allocation + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("allocation " + allocation + " has been created");
    }

    public List<Allocation> getAll() {
        log.info("extracting allocations...");
        List<Allocation> allocations;
        try {
            allocations = allocationDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract allocations, error code: " + e.getErrorCode(), e);
            throw new ExtractionException();
        }
        log.info(allocations.size() + " allocations have been extracted");
        return allocations;
    }

    public Allocation getById(long id) {
        try {
            return allocationDao.getAll().stream()
                    .filter(a -> a.getId() == id)
                    .findAny().orElseThrow(NotFoundException::new);
        } catch (SQLException e) {
            log.error("failed to extract allocation by ID " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
    }

    public void updateById(long id, Allocation allocation) {
        if (graphService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating allocation by ID " + id + "...");
        try {
            allocationDao.updateById(id, allocation);
        } catch (SQLException e) {
            log.error("failed to update allocation by ID " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("allocation has been updated by ID " + id);
    }

    public void update(List<Allocation> allocations) {
        if (graphService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating allocations... " + allocations);
        try {
            allocationDao.update(allocations);
        } catch (SQLException e) {
            log.error("failed to update allocations " + allocations + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("allocations have been updated " + allocations);
    }

    public void deleteById(long id) {
        if (graphService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("deleting allocation by ID " + id);
        try {
            allocationDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete allocation by ID " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("allocation has been deleted by ID " + id);
    }
}