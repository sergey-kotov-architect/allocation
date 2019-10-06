package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.Edge;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import com.sergeykotov.allocation.repository.EdgeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EdgeService {
    private static final Logger log = Logger.getLogger(EdgeService.class);
    private final EdgeRepository edgeRepository;

    @Autowired
    public EdgeService(EdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }

    public void create(Edge edge) {
        log.info("creating edge... " + edge);
        try {
            edgeRepository.save(edge);
        } catch (Exception e) {
            log.error("failed to create edge " + edge, e);
            throw new InvalidDataException();
        }
        log.info("edge has been created " + edge);
    }

    public List<Edge> getAll() {
        List<Edge> edges = new ArrayList<>();
        try {
            edgeRepository.findAll().forEach(edges::add);
        } catch (Exception e) {
            log.error("failed to extract edges", e);
            throw new ExtractionException();
        }
        return edges;
    }

    public Edge getById(long id) {
        try {
            return edgeRepository.findById(id).orElseThrow(InvalidDataException::new);
        } catch (Exception e) {
            log.error("failed to extract edge by id " + id, e);
            throw new InvalidDataException();
        }
    }

    public void update(Edge edge) {
        log.info("updating edge... " + edge);
        try {
            edgeRepository.save(edge);
        } catch (Exception e) {
            log.error("failed to update edge " + edge, e);
            throw new InvalidDataException();
        }
        log.info("edge has been updated " + edge);
    }

    public void deleteById(long id) {
        log.info("deleting edge by id " + id);
        try {
            edgeRepository.deleteById(id);
        } catch (Exception e) {
            log.error("failed to delete edge by id " + id, e);
            throw new InvalidDataException();
        }
        log.info("edge has been deleted by id " + id);
    }
}