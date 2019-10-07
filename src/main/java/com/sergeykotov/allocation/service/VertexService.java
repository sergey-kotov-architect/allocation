package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.dao.VertexDao;
import com.sergeykotov.allocation.domain.Vertex;
import com.sergeykotov.allocation.exception.DataModificationException;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class VertexService {
    private static final Logger log = Logger.getLogger(VertexService.class);
    private final VertexDao vertexDao;
    private final OptimisationService optimisationService;

    @Autowired
    public VertexService(VertexDao vertexDao, OptimisationService optimisationService) {
        this.vertexDao = vertexDao;
        this.optimisationService = optimisationService;
    }

    public void create(Vertex vertex) {
        log.info("creating vertex... " + vertex);
        try {
            vertexDao.create(vertex);
        } catch (SQLException e) {
            log.error("failed to create vertex " + vertex, e);
            throw new InvalidDataException();
        }
        log.info("vertex has been created " + vertex);
    }

    public List<Vertex> getAll() {
        try {
            return vertexDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract vertices", e);
            throw new ExtractionException();
        }
    }

    public Vertex getById(long id) {
        try {
            return vertexDao.getAll().stream()
                    .filter(v -> v.getId() == id)
                    .findAny().orElseThrow(InvalidDataException::new);
        } catch (SQLException e) {
            log.error("failed to extract vertex by id " + id, e);
            throw new InvalidDataException();
        }
    }

    public void update(Vertex vertex) {
        if (optimisationService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating vertex... " + vertex);
        try {
            vertexDao.update(vertex);
        } catch (SQLException e) {
            log.error("failed to update vertex " + vertex, e);
            throw new InvalidDataException();
        }
        log.info("vertex has been updated " + vertex);
    }

    public void deleteById(long id) {
        log.info("deleting vertex by id " + id);
        try {
            vertexDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete vertex by id " + id, e);
            throw new InvalidDataException();
        }
        log.info("vertex has been deleted by id " + id);
    }
}