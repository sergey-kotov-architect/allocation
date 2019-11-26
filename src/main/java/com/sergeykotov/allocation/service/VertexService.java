package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.dao.VertexDao;
import com.sergeykotov.allocation.domain.Vertex;
import com.sergeykotov.allocation.exception.DataModificationException;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import com.sergeykotov.allocation.exception.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class VertexService {
    private static final Logger log = Logger.getLogger(VertexService.class);

    private final VertexDao vertexDao;
    private final GraphService graphService;

    @Autowired
    public VertexService(VertexDao vertexDao, GraphService graphService) {
        this.vertexDao = vertexDao;
        this.graphService = graphService;
    }

    public void create(Vertex vertex) {
        log.info("creating vertex... " + vertex);
        try {
            vertexDao.create(vertex);
        } catch (SQLException e) {
            log.error("failed to create vertex " + vertex + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("vertex has been created " + vertex);
    }

    public List<Vertex> getAll() {
        log.info("extracting vertices...");
        List<Vertex> vertices;
        try {
            vertices = vertexDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract vertices, error code: " + e.getErrorCode(), e);
            throw new ExtractionException();
        }
        log.info(vertices.size() + " vertices have been extracted");
        return vertices;
    }

    public Vertex getById(long id) {
        try {
            return vertexDao.getAll().stream()
                    .filter(v -> v.getId() == id)
                    .findAny().orElseThrow(NotFoundException::new);
        } catch (SQLException e) {
            log.error("failed to extract vertex by id " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
    }

    public void update(Vertex vertex) {
        if (graphService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating vertex... " + vertex);
        try {
            vertexDao.update(vertex);
        } catch (SQLException e) {
            log.error("failed to update vertex " + vertex + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("vertex has been updated " + vertex);
    }

    public void deleteById(long id) {
        log.info("deleting vertex by id " + id);
        try {
            vertexDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete vertex by id " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("vertex has been deleted by id " + id);
    }
}