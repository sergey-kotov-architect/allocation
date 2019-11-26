package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.dao.EdgeDao;
import com.sergeykotov.allocation.domain.Edge;
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
public class EdgeService {
    private static final Logger log = Logger.getLogger(EdgeService.class);

    private final EdgeDao edgeDao;
    private final GraphService graphService;

    @Autowired
    public EdgeService(EdgeDao edgeDao, GraphService graphService) {
        this.edgeDao = edgeDao;
        this.graphService = graphService;
    }

    public void create(Edge edge) {
        log.info("creating edge... " + edge);
        try {
            edgeDao.create(edge);
        } catch (SQLException e) {
            log.error("failed to create edge " + edge + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("edge has been created " + edge);
    }

    public List<Edge> getAll() {
        log.info("extracting edges...");
        List<Edge> edges;
        try {
            edges = edgeDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract edges, error code: " + e.getErrorCode(), e);
            throw new ExtractionException();
        }
        log.info(edges.size() + " edges have been extracted");
        return edges;
    }

    public Edge getById(long id) {
        try {
            return edgeDao.getAll().stream()
                    .filter(e -> e.getId() == id)
                    .findAny().orElseThrow(NotFoundException::new);
        } catch (SQLException e) {
            log.error("failed to extract edge by id " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
    }

    public void update(Edge edge) {
        if (graphService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating edge... " + edge);
        try {
            edgeDao.update(edge);
        } catch (SQLException e) {
            log.error("failed to update edge " + edge + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("edge has been updated " + edge);
    }

    public void deleteById(long id) {
        log.info("deleting edge by id " + id);
        try {
            edgeDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete edge by id " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("edge has been deleted by id " + id);
    }
}