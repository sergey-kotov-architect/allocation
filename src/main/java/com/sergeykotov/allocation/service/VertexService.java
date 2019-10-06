package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.Vertex;
import com.sergeykotov.allocation.exception.DataModificationException;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import com.sergeykotov.allocation.repository.VertexRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VertexService {
    private static final Logger log = Logger.getLogger(VertexService.class);
    private final VertexRepository vertexRepository;
    private final OptimisationService optimisationService;

    @Autowired
    public VertexService(VertexRepository vertexRepository, OptimisationService optimisationService) {
        this.vertexRepository = vertexRepository;
        this.optimisationService = optimisationService;
    }

    public void create(Vertex vertex) {
        log.info("creating vertex... " + vertex);
        try {
            vertexRepository.save(vertex);
        } catch (Exception e) {
            log.error("failed to create vertex " + vertex, e);
            throw new InvalidDataException();
        }
        log.info("vertex has been created " + vertex);
    }

    public List<Vertex> getAll() {
        List<Vertex> vertices = new ArrayList<>();
        try {
            vertexRepository.findAll().forEach(vertices::add);
        } catch (Exception e) {
            log.error("failed to extract vertices", e);
            throw new ExtractionException();
        }
        return vertices;
    }

    public Vertex getById(long id) {
        try {
            return vertexRepository.findById(id).orElseThrow(InvalidDataException::new);
        } catch (Exception e) {
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
            vertexRepository.save(vertex);
        } catch (Exception e) {
            log.error("failed to update vertex " + vertex, e);
            throw new InvalidDataException();
        }
        log.info("vertex has been updated " + vertex);
    }

    public void deleteById(long id) {
        log.info("deleting vertex by id " + id);
        try {
            vertexRepository.deleteById(id);
        } catch (Exception e) {
            log.error("failed to delete vertex by id " + id, e);
            throw new InvalidDataException();
        }
        log.info("vertex has been deleted by id " + id);
    }
}